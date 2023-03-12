package com.dhl.assetmanager.service;

import com.dhl.assetmanager.dto.response.StockroomDto;
import com.dhl.assetmanager.mapper.StockroomMapper;
import com.dhl.assetmanager.repository.StockroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockroomService {

    private final StockroomRepository stockroomRepository;

    private final StockroomMapper stockroomMapper;

    public List<StockroomDto> getAllStockrooms() {
        var stockrooms = stockroomRepository.findAll();
        return stockroomMapper.stockroomListToStockroomDtoList(stockrooms);
    }

}
