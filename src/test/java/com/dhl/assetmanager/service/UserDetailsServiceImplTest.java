package com.dhl.assetmanager.service;

import com.dhl.assetmanager.entity.Role;
import com.dhl.assetmanager.entity.User;
import com.dhl.assetmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserDetailsServiceImplTest {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void shouldReturnUserDetailsWhenUserIsFound() {
        String givenUsername = "test";

        when(userRepository.findByUsername("test")).thenReturn(Optional.of(exampleUser()));

        var actualUserDetails = userDetailsService.loadUserByUsername(givenUsername);

        assertThat(actualUserDetails)
                .hasNoNullFieldsOrPropertiesExcept("requestedOrders", "assignedToOrders", "recivedOrders");
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserIsNotFound() {
        String givenUsername = "test";

        when(userRepository.findByUsername("test")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(givenUsername));
    }

    private User exampleUser() {
        // password is encrypted qwerty123
        return new User(99L,
                "test",
                "test@test.test",
                "$2a$10$aYZVX/tu3WrsaI2MnEBAF.woMZY/sIbcyU/2GHDCep1oIZHHUaAZe",
                new Role(99L, "IT_MANAGER"),
                Collections.emptyList(),
                null,
                null,
                null,
                Collections.emptyList(),
                false);
    }

}
