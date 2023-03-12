package com.dhl.assetmanager.service;

import com.dhl.assetmanager.dto.request.UserCredentialsRequest;
import com.dhl.assetmanager.dto.request.UserRegistrationRequest;
import com.dhl.assetmanager.dto.response.TokenResponse;
import com.dhl.assetmanager.entity.User;
import com.dhl.assetmanager.exception.RoleNotFoundException;
import com.dhl.assetmanager.exception.UserAlreadyExistsException;
import com.dhl.assetmanager.exception.UserNotFoundException;
import com.dhl.assetmanager.repository.RoleRepository;
import com.dhl.assetmanager.repository.UserRepository;
import com.dhl.assetmanager.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    private static final String DHL_EMPLOYEE = "ROLE_EMPLOYEE_DHL";

    public TokenResponse authenticateUser(UserCredentialsRequest userCredentials) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userCredentials.getUsername(), userCredentials.getPassword()));
                LdapUserDetailsImpl ldapUserDetails = (LdapUserDetailsImpl) authentication.getPrincipal();

        if (userRepository.existsByUsername(ldapUserDetails.getUsername())) {
            return jwtUtil.createToken(userRepository.findByUsernameLdap(ldapUserDetails.getUsername()));
        }
        throw new UserNotFoundException();
    }

    @Transactional
    public void registerUser(UserRegistrationRequest userCredentials) {
        if (userExists(userCredentials)) {
            throw new UserAlreadyExistsException();
        }

        var role = roleRepository.findByName(DHL_EMPLOYEE).orElseThrow(() -> new RoleNotFoundException(false));
        var user = User.builder()
                .username(userCredentials.getUsername())
                .email(userCredentials.getEmail())
                .password(passwordEncoder.encode(userCredentials.getPassword()))
                .role(role)
                .isDeleted(false)
                .build();
        userRepository.save(user);
    }

    private boolean userExists(UserRegistrationRequest userCredentials) {
        return userRepository.existsByUsername(userCredentials.getUsername()) || userRepository.existsByEmail(userCredentials.getEmail());
    }

}
