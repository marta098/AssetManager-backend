package com.dhl.assetmanager.repository;

import com.dhl.assetmanager.entity.Report;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends Repository<Report, Long> {

    List<Report> findByOrderByToDateDesc();

    Optional<Report> findById(long id);

    Optional<Report> findTopByOrderByToDateDesc();

    Report save(Report report);

}