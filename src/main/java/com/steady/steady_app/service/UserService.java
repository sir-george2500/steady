package com.steady.steady_app.service;

import com.steady.steady_app.dto.UserRegistrationDto;
import com.steady.steady_app.model.UserModel;
import com.steady.steady_app.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public UserModel registerUser(UserRegistrationDto dto) {
        validateNewUser(dto);

        String verificationToken = generateVerificationToken();
        UserModel user = new UserModel();
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setVerificationToken(verificationToken);
        user.setVerificationTokenExpiry(LocalDateTime.now().plusMinutes(15));
        user.setVerified(false);

        UserModel savedUser = userRepository.save(user);

        try {
            emailService.sendOtpEmail(user.getEmail(), verificationToken, user.getFullName());
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }

        return savedUser;
    }

    private String generateVerificationToken() {
        // Generate an 8-character token using numbers and uppercase letters
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // Removed similar looking characters (0,1,I,O)
        StringBuilder token = new StringBuilder();
        java.security.SecureRandom random = new java.security.SecureRandom();

        for (int i = 0; i < 8; i++) {
            token.append(chars.charAt(random.nextInt(chars.length())));
        }
        return token.toString();
    }

    private void validateNewUser(UserRegistrationDto dto) {
        if (userRepository.existsByEmail(dto.getEmail().toLowerCase())) {
            throw new IllegalArgumentException("Email already registered");
        }
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
    }

    @Transactional
    public boolean verifyToken(String email, String token) {
        UserModel user = userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getVerificationToken().equals(token) &&
                LocalDateTime.now().isBefore(user.getVerificationTokenExpiry())) {
            user.setVerified(true);
            user.setVerificationToken(null);
            user.setVerificationTokenExpiry(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}