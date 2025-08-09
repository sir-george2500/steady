package com.steady.steady_app.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRegistrationDto {
    @NotBlank(message = "Username is required")
    @Pattern(
            regexp = "^[a-zA-Z0-9._-]{4,50}$",
            message = "Username must be 4-50 characters long and can only contain letters, numbers, dots, underscores, and hyphens"
    )
    private String username;

    @NotBlank(message = "Full name is required")
    @Pattern(
            regexp = "^[a-zA-Z\\s]{2,100}$",
            message = "Full name must contain only letters and spaces, and be 2-100 characters long"
    )
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=\\S+$).{8,}$",
            message = "Password must be at least 8 characters long and contain no spaces"
    )
    private String password;
}
