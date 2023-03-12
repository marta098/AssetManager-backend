package com.dhl.assetmanager.controller;

import com.dhl.assetmanager.dto.response.ReportDto;
import com.dhl.assetmanager.entity.User;
import com.dhl.assetmanager.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PreAuthorize("hasAnyRole('MANAGER_IT', 'EMPLOYEE_IT')")
    @GetMapping
    public ResponseEntity<List<ReportDto>> getAllReports() {
        var reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    @PreAuthorize("hasAnyRole('MANAGER_IT', 'EMPLOYEE_IT')")
    @PostMapping
    public ResponseEntity<ReportDto> createReport(Authentication authentication) {
        var report = reportService.createReport((User) authentication.getPrincipal());
        return ResponseEntity.ok(report);
    }

    @PreAuthorize("hasAnyRole('MANAGER_IT', 'EMPLOYEE_IT')")
    @GetMapping(value = "/{id}", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public byte[] getFile(@PathVariable long id) {
        return reportService.getFile(id);
    }

    @PreAuthorize("hasAnyRole('MANAGER_IT', 'EMPLOYEE_IT')")
    @GetMapping("/last-report-date")
    public ResponseEntity<LocalDateTime> getLastReportDate() {
        return ResponseEntity.ok(reportService.getLastReportDate());
    }

}
