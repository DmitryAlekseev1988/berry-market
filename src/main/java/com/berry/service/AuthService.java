package com.berry.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.berry.dto.AuthResponse;
import com.berry.dto.LoginRequest;
import com.berry.dto.RegisterRequest;
import com.berry.entity.User;
import com.berry.repository.UserRepository;
import com.berry.security.JwtUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new com.berry.exception.BadRequestException(
                    "Пользователь с email " + request.getEmail() + " уже существует");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setType(request.getUserType());

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getId());

        return new AuthResponse(token, user.getId(), user.getName(), user.getType());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new com.berry.exception.ResourceNotFoundException("Пользователь не найден"));

        String token = jwtUtil.generateToken(user.getId());

        return new AuthResponse(token, user.getId(), user.getName(), user.getType());
    }
}
