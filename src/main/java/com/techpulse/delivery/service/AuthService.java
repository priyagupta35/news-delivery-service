package com.techpulse.delivery.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techpulse.delivery.dto.AuthRequest;
import com.techpulse.delivery.dto.AuthResponse;
import com.techpulse.delivery.dto.RegisterRequest;
import com.techpulse.delivery.model.User;
import com.techpulse.delivery.repository.UserRepository;
import com.techpulse.delivery.security.JwtUtil;

@Service
public class AuthService {

    private static final Logger logger =
        LogManager.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        logger.info("Registration attempt for email: {}",
            request.getEmail());

        if (userRepository.findByEmail(request.getEmail())
                .isPresent()) {
            logger.warn("Registration failed — email already exists: {}",
                request.getEmail());
            throw new RuntimeException(
                "Email already registered: " + request.getEmail());
        }

        String role = (request.getRole() != null
            && !request.getRole().isBlank())
            ? request.getRole().toUpperCase()
            : "READER";

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(
            passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        userRepository.save(user);
        logger.info("User registered successfully: {} with role: {}",
            request.getEmail(), role);

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token, role, "Registration successful");
    }

    public AuthResponse login(AuthRequest request) {
        logger.info("Login attempt for email: {}", request.getEmail());

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()));
        } catch (AuthenticationException e) {
            // WARN — failed login attempt, security relevant
            logger.warn("Login failed for email: {} — {}",
                request.getEmail(), e.getMessage());
            throw e;
        }

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user);
        logger.info("Login successful for email: {}", request.getEmail());

        return new AuthResponse(token, user.getRole(), "Login successful");
    }
}