package com.dhl.assetmanager.service;

import com.dhl.assetmanager.dto.response.ReportDto;
import com.dhl.assetmanager.entity.Asset;
import com.dhl.assetmanager.entity.Report;
import com.dhl.assetmanager.entity.User;
import com.dhl.assetmanager.exception.ReportAlreadyGeneratedException;
import com.dhl.assetmanager.exception.ReportNotFoundException;
import com.dhl.assetmanager.mapper.ReportMapper;
import com.dhl.assetmanager.repository.AssetRepository;
import com.dhl.assetmanager.repository.ReportRepository;
import com.dhl.assetmanager.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final AssetRepository assetRepository;
    private final ReportRepository reportRepository;

    private final ReportGenerationService reportGenerationService;
    private final DateUtil dateUtil;

    private final ReportMapper reportMapper;

    public List<ReportDto> getAllReports() {
        var reports = reportRepository.findByOrderByToDateDesc();
        return reports.stream()
                .map(reportMapper::reportToReportDto)
                .collect(Collectors.toList());
    }

    public byte[] getFile(long id) {
        var reportFile = reportRepository.findById(id)
                .orElseThrow(ReportNotFoundException::new);
        return reportFile.getFile();
    }

    @Transactional
    public ReportDto createReport(User user) {
        var latestReportOptional = reportRepository.findTopByOrderByToDateDesc();

        LocalDateTime fromDate = null;
        List<Asset> assets = assetRepository.findAll();
        if (latestReportOptional.isPresent()) {
            var latestReport = latestReportOptional.get();
            checkIfReportWasAlreadyGenerated(latestReport);
            fromDate = getFromDate(latestReport);
        }

        byte[] reportBytes = reportGenerationService.generateReportBytes(assets);

        var report = Report.builder()
                .generatedBy(user)
                .file(reportBytes)
                .timestamp(dateUtil.getCurrentDateTime())
                .fromDate(fromDate)
                .toDate(dateUtil.getCurrentDateTime())
                .build();
        var savedReport = reportRepository.save(report);
        return reportMapper.reportToReportDto(savedReport);
    }

    public LocalDateTime getLastReportDate() {
        return reportRepository.findTopByOrderByToDateDesc()
                .map(Report::getTimestamp)
                .orElse(null);
    }

    private void checkIfReportWasAlreadyGenerated(Report latestReport) {
        var currentDate = dateUtil.getCurrentDate();
        var generatedDate = latestReport.getToDate().toLocalDate();

        if (currentDate.isEqual(generatedDate)) {
            throw new ReportAlreadyGeneratedException();
        }
    }

    private LocalDateTime getFromDate(Report report) {
        var currentDate = dateUtil.getCurrentDateTime();

        if (currentDate.minusDays(7).isBefore(report.getToDate())) {
            return currentDate.minusDays(7);
        }
        return report.getToDate();
    }

}
