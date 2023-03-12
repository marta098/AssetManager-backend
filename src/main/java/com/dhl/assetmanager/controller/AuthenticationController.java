package com.dhl.assetmanager.controller;

import com.dhl.assetmanager.dto.request.UserCredentialsRequest;
import com.dhl.assetmanager.dto.request.UserRegistrationRequest;
import com.dhl.assetmanager.dto.response.TokenResponse;
import com.dhl.assetmanager.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
class AuthenticationController {

    private final AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Validated UserCredentialsRequest userCredentials) {
        var tokenResponse = authenticationService.authenticateUser(userCredentials);
        return ResponseEntity.ok(tokenResponse);
    }


    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody @Validated UserRegistrationRequest userCredentials) {
        authenticationService.registerUser(userCredentials);
        return login(new UserCredentialsRequest(userCredentials.getUsername(), userCredentials.getPassword()));
    }

}
