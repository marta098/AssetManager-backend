package com.dhl.assetmanager.controller;

import com.dhl.assetmanager.dto.response.MpkNumberDto;
import com.dhl.assetmanager.service.MpkNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mpks")
@RequiredArgsConstructor
public class MpkNumberController {

    private final MpkNumberService mpkNumberService;

    @PreAuthorize("hasRole('MANAGER_DHL')")
    @GetMapping
    public ResponseEntity<List<MpkNumberDto>> getAllMpkNumbers() {
        var mpkNumbers = mpkNumberService.getAllMpkNumbers();
        return ResponseEntity.ok(mpkNumbers);
    }

}
