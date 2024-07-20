package com.project.movies.auth.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.movies.auth.entities.User;
import com.project.movies.auth.entities.UserRole;
import com.project.movies.auth.repositories.UserRepository;
import com.project.movies.auth.utils.AuthResponse;
import com.project.movies.auth.utils.LoginRequest;
import com.project.movies.auth.utils.RegisterRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
        private final PasswordEncoder passwordEncoder;
        private final UserRepository userRepository;
        private final JwtService jwtService;
        private final RefreshTokenService refreshTokenService;
        private final AuthenticationManager authenticationManager;

        public User register(RegisterRequest registerRequest) {
                var user = User.builder()
                                .name(registerRequest.getName())
                                .email(registerRequest.getEmail())
                                .username(registerRequest.getUsername())
                                .password(passwordEncoder.encode(registerRequest.getPassword()))
                                .role(UserRole.USER)
                                .build();

                User savedUser = userRepository.save(user);
                return savedUser;
        }

        public AuthResponse login(LoginRequest loginRequest) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                loginRequest.getEmail(),
                                                loginRequest.getPassword()));

                var user = userRepository.findByEmail(loginRequest.getEmail())
                                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
                var accessToken = jwtService.generateToken(user);
                var refreshToken = refreshTokenService.createRefreshToken(loginRequest.getEmail());

                return AuthResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken.getRefreshToken())
                                .build();
        }
}
