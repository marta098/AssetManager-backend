package com.dhl.assetmanager.repository;

import com.dhl.assetmanager.entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends Repository<Order, Long> {

    Optional<Order> findById(long id);

    List<Order> findByOrderByAddedDateDesc();

    Order save(Order order);

    @Query("select o from Order o where o.addedDate >= cast(current_timestamp as date) and o.addedDate < dateadd(DD, 1, cast(current_timestamp as date)) order by o.orderNumber desc")
    List<Order> findAllOrdersCreatedToday();

    @Query("select o from Order o where o.status = 'SENT'")
    List<Order> findAllOrdersWithSentStatus();

    List<Order> findAllByReceiver_IdOrderByAddedDateDesc(Long userId);

    List<Order> findAllByRequester_IdOrderByAddedDateDesc(Long userId);

}