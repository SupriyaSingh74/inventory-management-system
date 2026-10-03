package com.inventory.controller;

import com.inventory.dto.auth.AuthResponse;
import com.inventory.dto.auth.LoginRequest;
import com.inventory.dto.auth.RegisterRequest;
import com.inventory.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public AuthResponse register(
            @Valid @RequestBody RegisterRequest request) {

        return authenticationService.register(request);

    }

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request) {

        return authenticationService.login(request);

    }

}