package com.dhl.assetmanager.mapper;

import com.dhl.assetmanager.dto.response.LocationDto;
import com.dhl.assetmanager.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface LocationMapper {

    List<LocationDto> locationListToLocationDtoList(List<Location> locations);

}
