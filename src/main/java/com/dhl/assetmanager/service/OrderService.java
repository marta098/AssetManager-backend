package com.dhl.assetmanager.service;

import com.dhl.assetmanager.dto.request.NewOrderRequest;
import com.dhl.assetmanager.dto.request.OrderUpdateNewAssetRequest;
import com.dhl.assetmanager.dto.request.OrderUpdateRequest;
import com.dhl.assetmanager.dto.response.OrderDto;
import com.dhl.assetmanager.entity.*;
import com.dhl.assetmanager.exception.*;
import com.dhl.assetmanager.mapper.OrderMapper;
import com.dhl.assetmanager.repository.*;
import com.dhl.assetmanager.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.dhl.assetmanager.entity.AssetStatus.IN_USE;
import static com.dhl.assetmanager.entity.Status.HANDED_FOR_COMPLETION;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;
    private final LocationRepository locationRepository;
    private final MpkNumberRepository mpkNumberRepository;
    private final MailService mailService;
    private final OrderMapper orderMapper;
    private final GlobalVariableService globalVariableService;
    private final DateUtil dateUtil;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private static final String DHL_EMPLOYEE = "ROLE_EMPLOYEE_DHL";

    public List<OrderDto> getAllOrders() {
        var allOrders = orderRepository.findByOrderByAddedDateDesc();
        return allOrders.stream()
                .map(orderMapper::orderToOrderDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto addNewOrder(NewOrderRequest orderDto, User requester) {
        checkIfDateIsCorrect(orderDto.getPickupDate());
        User receiver = null;

        if (orderDto.getReceiverEmail() == null) {
            receiver = requester;
        } else if (userRepository.existsByEmail(orderDto.getReceiverEmail())) {
            receiver = userRepository.findByEmail(orderDto.getReceiverEmail());
        } else {
            var role = roleRepository.findByName(DHL_EMPLOYEE).orElseThrow(() -> new RoleNotFoundException(false));
            var user = User.builder()
                    .username(orderDto.getReceiverEmail())
                    .email(orderDto.getReceiverEmail())
                    .password(passwordEncoder.encode(orderDto.getReceiverEmail()))
                    .role(role)
                    .isDeleted(false)
                    .build();
            userRepository.save(user);
            receiver = userRepository.findByEmail(orderDto.getReceiverEmail());
        }
        var mpkNumber = mpkNumberRepository.findById(orderDto.getMpkId())
                .orElseThrow(MpkNumberNotFoundException::new);
        var orderNumber = getOrderNumber();
        var order = orderMapper.orderDtoToOrder(orderDto, receiver, requester, orderNumber, mpkNumber);
        var savedOrder = orderRepository.save(order);
        mailService.sendNewOrder(savedOrder);
        return orderMapper.orderToOrderDto(savedOrder);
}

    public OrderDto getOrderDetails(long orderId, User user) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        if (hasRole(user, "ROLE_EMPLOYEE_DHL")) {
            validateIfUserIsReceiver(user, order);
        }
        return orderMapper.orderToOrderDto(order);
    }

    @Transactional
    public OrderDto updateOrder(long orderId, OrderUpdateRequest orderUpdateRequest, User createdByUser) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        var receiver = userRepository.findById(orderUpdateRequest.getReceiverId())
                .orElseThrow(UserNotFoundException::new);
        var assignedTo = userRepository.findById(orderUpdateRequest.getAssignedTo())
                .orElseThrow(UserNotFoundException::new);
        var assignedBy = userRepository.findById(orderUpdateRequest.getAssignedBy())
                .orElseThrow(UserNotFoundException::new);

        Asset asset = null;
        if (orderUpdateRequest.getSerialNumber() != null && !orderUpdateRequest.getSerialNumber().isEmpty()) {
            asset = assetRepository.findBySerialNumber(orderUpdateRequest.getSerialNumber())
                    .orElseThrow(AssetNotFoundException::new);
            if (currentUserChanged(receiver, asset.getCurrentUser())) {
                addChangesToAsset(createdByUser, receiver, asset);
            }
        }

        updateOrder(orderUpdateRequest.getPickupDate(), orderUpdateRequest.getStatus(), orderUpdateRequest.getRemark(),
                createdByUser, order, receiver, orderUpdateRequest.getRequestedModel(), asset,
                orderUpdateRequest.getDeliveryAddress(), orderUpdateRequest.getDeliveryType(), assignedTo, assignedBy);
        orderRepository.save(order);
        return orderMapper.orderToOrderDto(order);
    }


    @Transactional
    public OrderDto updateOrderWithNewAsset(long orderId, OrderUpdateNewAssetRequest orderUpdateRequest, User createdByUser) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        var receiver = userRepository.findById(orderUpdateRequest.getReceiverId())
                .orElseThrow(UserNotFoundException::new);
        var assignedTo = userRepository.findById(orderUpdateRequest.getAssignedTo())
                .orElseThrow(UserNotFoundException::new);
        var assignedBy = userRepository.findById(orderUpdateRequest.getAssignedBy())
                .orElseThrow(UserNotFoundException::new);

        if (order.getAsset() != null) {
            throw new AssetAlreadyCreatedException();
        }

        if (assetRepository.existsBySerialNumber(orderUpdateRequest.getAsset().getSerialNumber())) {
            throw new SerialNumberAlreadyExistsException();
        }

        var asset = createNewAssetEntity(orderUpdateRequest, receiver, order);

        updateOrder(orderUpdateRequest.getPickupDate(), orderUpdateRequest.getStatus(), orderUpdateRequest.getRemark(),
                createdByUser, order, receiver, orderUpdateRequest.getRequestedModel(), asset,
                orderUpdateRequest.getDeliveryAddress(), orderUpdateRequest.getDeliveryType(), assignedTo, assignedBy);
        var savedOrder = orderRepository.save(order);
        return orderMapper.orderToOrderDto(savedOrder);
    }

    private void updateOrder(LocalDateTime pickupDate, String status, String remark, User createdByUser, Order order,
                             User receiver, Model requestedModel, Asset asset, String deliveryAddress, DeliveryType deliveryType,
                             User assignedTo, User assignedBy) {
        var previousStatus = order.getStatus();
        order.setPickupDate(pickupDate);
        order.setAsset(asset);
        order.setReceiver(receiver);
        order.setStatus(Status.valueOf(status));
        order.setRequestedModel(requestedModel);
        order.setDeliveryAddress(deliveryAddress);
        order.setDeliveryType(deliveryType);
        order.setAssignedTo(assignedTo);
        order.setAssignedBy(assignedBy);
        var changeHistory = order.getChangeHistory();
        changeHistory.add(createChangeHistory(order.getStatus(), remark, createdByUser, order));

        if (statusChanged(previousStatus, order.getStatus())) {
            mailService.sendChangingStatusUpdate(order);
        }
    }

    private Asset createNewAssetEntity(OrderUpdateNewAssetRequest orderUpdateRequest, User receiver, Order order) {
        var asset = orderUpdateRequest.getAsset();
        var location = locationRepository.findById(asset.getLocationId())
                .orElseThrow(LocationNotFoundException::new);

        return Asset.builder()
                .serialNumber(asset.getSerialNumber())
                .model(orderUpdateRequest.getAsset().getModel())
                .type(asset.getType())
                .deprecation(asset.getDeprecation())
                .currentUser(receiver)
                .status(IN_USE)
                .location(location)
                .crestCode(globalVariableService.getCrestCode())
                .name(asset.getName())
                .orders(List.of(order))
                .build();
    }

    private void addChangesToAsset(User createdByUser, User receiver, Asset asset) {
        asset.setCurrentUser(receiver);
        asset.setStatus(IN_USE);
        var currentDateTime = dateUtil.getCurrentDateTime();

        var changeHistoryList = asset.getChangeHistory();
        if (!changeHistoryList.isEmpty()) {
            var newestChangeHistory = changeHistoryList.stream()
                    .max(Comparator.comparing(AssetChangeHistory::getTimestamp))
                    .orElseThrow(RuntimeException::new);
            newestChangeHistory.setDischargeDate(currentDateTime);
        }

        var changeHistory = createAssetChangeHistory(asset, receiver, createdByUser, currentDateTime);
        changeHistoryList.add(changeHistory);
    }

    private boolean currentUserChanged(User receiver, User currentUser) {
        if (currentUser == null) {
            return true;
        }

        return currentUser.getId() != receiver.getId();
    }

    @Transactional
    public void changeStatus(long orderId, Status status, User user) {
        var order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        order.setStatus(status);
        order.getChangeHistory().add(createChangeHistory(status, user, order));
        orderRepository.save(order);
        mailService.sendChangingStatusUpdate(order);
    }

    @Transactional
    public LocalDateTime assignUserToOrder(long orderId, long userId, User user) {
        var order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        var assignedToUser = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (!hasRole(assignedToUser, "ROLE_EMPLOYEE_IT")) {
            throw new UserIsNotEmployeeItException();
        }

        var latestChange = LocalDateTime.now();

        if (order.getAssignedTo() == null) {
            order.setStatus(HANDED_FOR_COMPLETION);
            order.getChangeHistory().add(createChangeHistory(HANDED_FOR_COMPLETION, user, order));
        }
        order.setAssignmentDate(latestChange);
        order.setAssignedTo(assignedToUser);
        order.setAssignedBy(user);
        orderRepository.save(order);
        mailService.sendChangingStatusUpdate(order);
        return latestChange;
    }

    @Transactional
    public void addRemark(long orderId, String remark, User user) {
        var order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if (hasRole(user, "ROLE_EMPLOYEE_DHL")) {
            validateIfUserIsReceiver(user, order);
        }

        order.getChangeHistory().add(createChangeHistory(order.getStatus(), remark, user, order));
        orderRepository.save(order);
    }

    private void checkIfDateIsCorrect(LocalDateTime pickupDate) {
        if (pickupDate.isBefore(LocalDateTime.now().plus(7, ChronoUnit.DAYS))) {
            throw new PickupDateTooEarlyException();
        }
    }

    private boolean statusChanged(Status previousStatus, Status newStatus) {
        return previousStatus != newStatus;
    }

    private ChangeHistory createChangeHistory(Status status, User author, Order order) {
        return createChangeHistory(status, null, author, order);
    }

    private ChangeHistory createChangeHistory(Status status, String remark, User author, Order order) {
        return ChangeHistory.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .remark(remark)
                .author(author)
                .order(order)
                .build();
    }

    private String getOrderNumber() {
        var ordersCreatedToday = orderRepository.findAllOrdersCreatedToday();

        return ordersCreatedToday.stream().findFirst()
                .map(Order::getOrderNumber)
                .map(this::getIncrementedOrderNumber)
                .orElse(dateUtil.getCurrentDate() + "-1");
    }

    private String getIncrementedOrderNumber(String orderNumber) {
        var lastDash = orderNumber.lastIndexOf('-');
        var lastNumberAsString = orderNumber.substring(lastDash + 1);
        var lastNumber = Integer.parseInt(lastNumberAsString);
        return dateUtil.getCurrentDate() + "-" + (lastNumber + 1);
    }

    private boolean hasRole(User user, String role) {
        return user.getRole().getName().equals(role);
    }

    private void validateIfUserIsReceiver(User user, Order order) {
        if (order.getReceiver().getId() != user.getId()) {
            throw new UserNotPermittedException();
        }
    }

    private AssetChangeHistory createAssetChangeHistory(Asset asset, User receiver, User createdByUser, LocalDateTime currentDateTime) {
        return AssetChangeHistory.builder()
                .asset(asset)
                .owner(receiver)
                .author(createdByUser)
                .timestamp(currentDateTime)
                .assignmentDate(currentDateTime)
                .deprecation(asset.getDeprecation())
                .status(IN_USE)
                .build();
    }

}
