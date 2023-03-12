package com.dhl.assetmanager.controller;

import com.dhl.assetmanager.dto.request.CrestCodeRequest;
import com.dhl.assetmanager.dto.request.DeprecationMonthsRequest;
import com.dhl.assetmanager.service.GlobalVariableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/variables")
@RequiredArgsConstructor
public class GlobalVariableController {

    private final GlobalVariableService globalVariableService;

    @PreAuthorize("hasAnyRole('MANAGER_IT', 'EMPLOYEE_IT')")
    @GetMapping("/deprecation-months")
    public ResponseEntity<Integer> getDeprecationMonths() {
        var deprecationMonths = globalVariableService.getDeprecationMonths();
        return ResponseEntity.ok(deprecationMonths);
    }

    @PreAuthorize("hasAnyRole('MANAGER_IT', 'EMPLOYEE_IT')")
    @PutMapping("/deprecation-months")
    public ResponseEntity<Void> setDeprecationMonths(@RequestBody @Validated DeprecationMonthsRequest deprecationMonthsRequest) {
        globalVariableService.setDeprecationMonths(deprecationMonthsRequest.getDeprecationMonths());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('MANAGER_IT', 'EMPLOYEE_IT', 'MANAGER_DHL')")
    @GetMapping("/crest-code")
    public ResponseEntity<String> getCrestCode() {
        var crestCode = globalVariableService.getCrestCode();
        return ResponseEntity.ok(crestCode);
    }

    @PreAuthorize("hasRole('MANAGER_IT')")
    @PutMapping("/crest-code")
    public ResponseEntity<Void> setCrestCode(@RequestBody @Validated CrestCodeRequest crestCodeRequest) {
        globalVariableService.setCrestCode(crestCodeRequest.getCrestCode());
        return ResponseEntity.noContent().build();
    }

}
