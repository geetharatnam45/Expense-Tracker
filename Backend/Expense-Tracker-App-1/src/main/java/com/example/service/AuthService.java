package com.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dto.AuthResponse;
import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.entity.User;
import com.example.exception.EmailAlreadyExistsException;
import com.example.repo.UserRepository;
import com.example.security.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	 private final UserRepository userRepository;
	    private final PasswordEncoder passwordEncoder;
	    private final AuthenticationManager authenticationManager;
	    private final JwtUtil jwtUtil;

	    public AuthResponse register(RegisterRequest request) {
	        if (userRepository.existsByEmail(request.getEmail())) {
	            throw new EmailAlreadyExistsException("An account with this email already exists");
	        }

	        User user = User.builder()
	                .name(request.getName())
	                .email(request.getEmail())
	                .password(passwordEncoder.encode(request.getPassword()))
	                .build();

	        userRepository.save(user);

	        String token = jwtUtil.generateToken(user.getEmail());
	        return new AuthResponse(token, user.getName(), user.getEmail());
	    }

	    public AuthResponse login(LoginRequest request) {
	        authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

	        User user = userRepository.findByEmail(request.getEmail())
	                .orElseThrow(() -> new IllegalStateException("User vanished after authentication"));

	        String token = jwtUtil.generateToken(user.getEmail());
	        return new AuthResponse(token, user.getName(), user.getEmail());
	    }

}
