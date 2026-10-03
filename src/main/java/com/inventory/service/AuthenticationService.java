package com.inventory.service;

import com.inventory.dto.auth.AuthResponse;
import com.inventory.dto.auth.LoginRequest;
import com.inventory.dto.auth.RegisterRequest;

public interface AuthenticationService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

}