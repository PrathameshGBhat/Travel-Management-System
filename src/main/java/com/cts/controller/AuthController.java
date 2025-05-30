package com.cts.controller;

import com.cts.dto.AuthResponse;
import com.cts.dto.LoginDto;
import com.cts.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

   @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        return ResponseEntity.ok(authResponse);
    }
}
