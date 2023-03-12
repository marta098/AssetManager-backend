package com.dhl.assetmanager.mapper;

import com.dhl.assetmanager.dto.response.MpkNumberDto;
import com.dhl.assetmanager.entity.MpkNumber;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface MpkNumberMapper {

    MpkNumberDto mpkNumberToMpkNumberDto(MpkNumber mpkNumber);

}
