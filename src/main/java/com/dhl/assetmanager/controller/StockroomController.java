package com.dhl.assetmanager.controller;

import com.dhl.assetmanager.dto.response.StockroomDto;
import com.dhl.assetmanager.service.StockroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stockrooms")
@RequiredArgsConstructor
public class StockroomController {

    private final StockroomService stockroomService;

    @PreAuthorize("hasAnyRole('MANAGER_IT', 'EMPLOYEE_IT')")
    @GetMapping
    public ResponseEntity<List<StockroomDto>> getAllStockrooms() {
        var stockrooms = stockroomService.getAllStockrooms();
        return ResponseEntity.ok(stockrooms);
    }

}
