package com.steady.steady_app.service;

import com.steady.steady_app.dto.UserRegistrationDto;
import com.steady.steady_app.model.UserModel;
import com.steady.steady_app.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public UserModel registerUser(UserRegistrationDto dto) {
        validateNewUser(dto);

        UserModel user = new UserModel();
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return userRepository.save(user);
    }

    private void validateNewUser(UserRegistrationDto dto) {
        if (userRepository.existsByEmail(dto.getEmail().toLowerCase())) {
            throw new IllegalArgumentException("Email already registered");
        }
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
    }
}