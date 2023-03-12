package com.dhl.assetmanager.controller;

import com.dhl.assetmanager.entity.User;
import com.dhl.assetmanager.repository.UserRepository;
import com.dhl.assetmanager.service.MailService;
import com.dhl.assetmanager.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.dhl.assetmanager.TestData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class BaseTestController {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    protected UserDetailsService userDetailsService;
    @MockBean
    protected UserRepository userRepository;
    @MockBean
    protected MailService mailService;

    @BeforeEach
    void prepareMocks() {
        when(userDetailsService.loadUserByUsername("ItManager")).thenReturn(itManager());
        when(userRepository.findByUsername("ItManager")).thenReturn(Optional.of(itManager()));

        when(userDetailsService.loadUserByUsername("DhlManager")).thenReturn(dhlManager());
        when(userRepository.findByUsername("DhlManager")).thenReturn(Optional.of(dhlManager()));

        when(userDetailsService.loadUserByUsername("ItEmployee")).thenReturn(itEmployee());
        when(userRepository.findByUsername("ItEmployee")).thenReturn(Optional.of(itEmployee()));

        when(userDetailsService.loadUserByUsername("DhlEmployee")).thenReturn(dhlEmployee(3));
        when(userRepository.findByUsername("DhlEmployee")).thenReturn(Optional.of(dhlEmployee(3)));

        doNothing().when(mailService).sendChangingStatusUpdate(any());
        doNothing().when(mailService).sendNewOrder(any());
    }

    String getAccessToken(User user) {
        return "Bearer " + jwtUtil.createToken(user).getAccessToken();
    }

}
