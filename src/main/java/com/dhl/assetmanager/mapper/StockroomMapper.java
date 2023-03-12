package com.dhl.assetmanager.mapper;

import com.dhl.assetmanager.dto.response.StockroomDto;
import com.dhl.assetmanager.entity.Stockroom;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface StockroomMapper {

    List<StockroomDto> stockroomListToStockroomDtoList(List<Stockroom> stockrooms);

}
