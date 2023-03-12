package com.dhl.assetmanager.controller;

import com.dhl.assetmanager.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statuses")
@RequiredArgsConstructor
public class StatusController {

    @GetMapping
    public ResponseEntity<Status[]> getAllStatuses() {
        return ResponseEntity.ok(Status.values());
    }

}
