package com.dhl.assetmanager.util;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dhl.assetmanager.entity.Role;
import com.dhl.assetmanager.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static com.dhl.assetmanager.util.JwtUtil.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void shouldReturnToken() {
        User givenUser = new User(99L,
                "test",
                "test@test.test",
                "test12345",
                new Role(99L, "IT_MANAGER"),
                Collections.emptyList(),
                null,
                null,
                null,
                Collections.emptyList(),
                false);

        var actualTokenResponse = jwtUtil.createToken(givenUser);

        assertThat(actualTokenResponse)
                .isNotNull()
                .hasNoNullFieldsOrProperties();
    }

    @Test
    void shouldThrowJWTVerificationExceptionWhenAuthHeaderIsNotValid() {
        var givenAuthHeader = "Bearer test.test.test";

        assertThrows(JWTVerificationException.class, () -> jwtUtil.getVerifiedToken(givenAuthHeader));
    }

    @Test
    void shouldReturnDecodedToken() {
        User givenUser = new User(99L,
                "test",
                "test@test.test",
                "test12345",
                new Role(99L, "IT_MANAGER"),
                Collections.emptyList(),
                null,
                null,
                null,
                Collections.emptyList(),
                false);

        var accessToken = jwtUtil.createToken(givenUser).getAccessToken();
        var bearerToken = String.format("Bearer %s", accessToken);

        var actualVerifiedToken = jwtUtil.getVerifiedToken(bearerToken);

        assertThat(actualVerifiedToken)
                .isNotNull();

        assertThat(actualVerifiedToken.getClaims())
                .hasFieldOrProperty(USERNAME)
                .hasFieldOrProperty(ID)
                .hasFieldOrProperty(ROLE)
                .hasFieldOrProperty(EMAIL);
    }
}
