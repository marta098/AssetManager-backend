package com.dhl.assetmanager.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dhl.assetmanager.dto.response.TokenResponse;
import com.dhl.assetmanager.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class JwtUtil {

    public static final String USERNAME = "username";
    public static final String ID = "id";
    public static final String ROLE = "role";
    public static final String EMAIL = "email";

    @Value("${authentication.jwt.secret}")
    private String secret;

    public TokenResponse createToken(User user) {
        var calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        var expiresDate = calendar.getTime();

        var jwt = JWT.create()
                .withClaim(ID, user.getId())
                .withClaim(USERNAME, user.getUsername())
                .withClaim(ROLE, user.getRole().getName())
                .withClaim(EMAIL, user.getEmail())
                .withIssuedAt(new Date())
                .withExpiresAt(expiresDate)
                .sign(Algorithm.HMAC512(secret));
        return new TokenResponse(jwt);
    }

    public String getUsername(DecodedJWT decodedJwt) {
        return decodedJwt.getClaim(USERNAME).asString();
    }

    public DecodedJWT getVerifiedToken(String authHeader) {
        var jwtVerifier = JWT.require(Algorithm.HMAC512(secret)).build();
        return jwtVerifier.verify(authHeader.substring(7));
    }
}
