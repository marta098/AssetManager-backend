package com.dhl.assetmanager.service;

import com.dhl.assetmanager.dto.response.LocationDto;
import com.dhl.assetmanager.mapper.LocationMapper;
import com.dhl.assetmanager.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    private final LocationMapper locationMapper;

    public List<LocationDto> getAllLocations() {
        var locations = locationRepository.findAll();
        return locationMapper.locationListToLocationDtoList(locations);
    }

}
