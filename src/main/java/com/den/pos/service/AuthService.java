package com.den.pos.service;

import com.den.pos.web.dto.LoginRequest;
import com.den.pos.web.dto.RefreshRequest;
import com.den.pos.web.dto.RegisterRequest;
import com.den.pos.web.dto.TokenResponse;

public interface AuthService {

    TokenResponse login(LoginRequest request);

    TokenResponse register(RegisterRequest request);

    TokenResponse refresh(RefreshRequest request);

    void logout(String email);
}
