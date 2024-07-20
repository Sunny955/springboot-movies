package com.project.movies.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.movies.auth.entities.RefreshToken;
import com.project.movies.auth.entities.User;
import com.project.movies.auth.services.AuthService;
import com.project.movies.auth.services.JwtService;
import com.project.movies.auth.services.RefreshTokenService;
import com.project.movies.auth.utils.AuthResponse;
import com.project.movies.auth.utils.LoginRequest;
import com.project.movies.auth.utils.RefreshTokenRequest;
import com.project.movies.auth.utils.RegisterRequest;
import com.project.movies.dto.UserDto;
import com.project.movies.mappers.UserMappers;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final UserMappers userMappers;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtService jwtService,
            UserMappers userMappers) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        this.userMappers = userMappers;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody RegisterRequest registerRequest) {
        UserDto response = userMappers.repoToDto(authService.register(registerRequest));
        response.setMessage("Please login to get authorized!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {

        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = refreshToken.getUser();

        String accessToken = jwtService.generateToken(user);

        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build());
    }
}
