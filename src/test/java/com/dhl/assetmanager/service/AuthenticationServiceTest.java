package com.dhl.assetmanager.service;

import com.dhl.assetmanager.dto.request.UserCredentialsRequest;
import com.dhl.assetmanager.dto.request.UserRegistrationRequest;
import com.dhl.assetmanager.entity.Role;
import com.dhl.assetmanager.exception.RoleNotFoundException;
import com.dhl.assetmanager.exception.UserAlreadyExistsException;
import com.dhl.assetmanager.repository.RoleRepository;
import com.dhl.assetmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import static com.dhl.assetmanager.TestData.itManager;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void shouldReturnToken() {
        UserCredentialsRequest givenCredentials = new UserCredentialsRequest("test", "qwerty123");

        when(userDetailsService.loadUserByUsername("test"))
                .thenReturn(itManager());

        var actualToken = authenticationService.authenticateUser(givenCredentials);

        assertThat(actualToken.getAccessToken())
                .isNotBlank();
    }

    @Test
    void shouldRegisterUser() {
        UserRegistrationRequest givenCredentials = new UserRegistrationRequest("test", "test", "test");

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(new Role(1L, "test")));

        authenticationService.registerUser(givenCredentials);

        verify(userRepository).save(any());
    }

    @Test
    void shouldThrowUserAlreadyExistsExceptionWhenUserExists() {
        UserRegistrationRequest givenCredentials = new UserRegistrationRequest("test", "test", "test");

        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authenticationService.registerUser(givenCredentials));
    }

    @Test
    void shouldThrowRoleNotFoundExceptionWhenRoleIsNotFound() {
        UserRegistrationRequest givenCredentials = new UserRegistrationRequest("test", "test", "test");

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> authenticationService.registerUser(givenCredentials));
    }

}
