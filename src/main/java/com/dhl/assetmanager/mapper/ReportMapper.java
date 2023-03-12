package com.dhl.assetmanager.mapper;

import com.dhl.assetmanager.dto.response.ReportDto;
import com.dhl.assetmanager.entity.Report;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = UserMapper.class)
public interface ReportMapper {

    ReportDto reportToReportDto(Report report);

}
