package com.inventory.service.impl;

import com.inventory.dto.auth.AuthResponse;
import com.inventory.dto.auth.LoginRequest;
import com.inventory.dto.auth.RegisterRequest;
import com.inventory.entity.User;
import com.inventory.enums.Role;
import com.inventory.repository.UserRepository;
import com.inventory.security.JwtService;
import com.inventory.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl
        implements AuthenticationService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {

            throw new IllegalArgumentException(
                    "Email already registered");

        }

        User user = User.builder()

                .fullName(request.getFullName())

                .email(request.getEmail())

                .password(passwordEncoder.encode(
                        request.getPassword()))

                .role(request.getRole() == null
                        ? Role.STAFF
                        : request.getRole())

                .enabled(true)

                .build();

        userRepository.save(user);

        String token =
                jwtService.generateToken(

                        org.springframework.security.core.userdetails.User

                                .builder()

                                .username(user.getEmail())

                                .password(user.getPassword())

                                .roles(user.getRole().name())

                                .build());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .fullName(user.getFullName())
                .build();

    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid Email or Password"));

        String token = jwtService.generateToken(

                org.springframework.security.core.userdetails.User
                        .builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .fullName(user.getFullName())
                .build();

    }
}