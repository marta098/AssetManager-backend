package com.dhl.assetmanager.controller;

import com.dhl.assetmanager.dto.response.LocationDto;
import com.dhl.assetmanager.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PreAuthorize("hasAnyRole('MANAGER_IT', 'EMPLOYEE_IT')")
    @GetMapping
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        var locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

}
