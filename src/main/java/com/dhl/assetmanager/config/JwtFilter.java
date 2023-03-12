package com.dhl.assetmanager.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dhl.assetmanager.entity.User;
import com.dhl.assetmanager.exception.UserNotFoundException;
import com.dhl.assetmanager.repository.UserRepository;
import com.dhl.assetmanager.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    @Value("${authentication.jwt.secret}")
    private String secret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || authHeader.isEmpty() || authHeader.equals("Bearer null")) {
            filterChain.doFilter(request, response);
            return;
        }

        DecodedJWT decodedJwt;
        try {
            decodedJwt = jwtUtil.getVerifiedToken(authHeader);
        } catch (JWTVerificationException exception) {
            filterChain.doFilter(request, response);
            return;
        }

        var username = jwtUtil.getUsername(decodedJwt);
        var user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

        var token = getToken(user);
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(token);

        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getToken(User user) {
        return new UsernamePasswordAuthenticationToken(
                user,
                user.getUsername(),
                user.getAuthorities()
        );
    }
}
