package com.cts.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cts.dto.LoginDto;
import com.cts.security.JwtTokenProvider;
import com.cts.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginDto loginDto) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword());

//        org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);


        return jwtTokenProvider.generateToken(authentication);
    }
}
