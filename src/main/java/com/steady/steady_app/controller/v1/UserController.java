package com.steady.steady_app.controller.v1;

import com.steady.steady_app.dto.UserRegistrationDto;
import com.steady.steady_app.model.UserModel;
import com.steady.steady_app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController extends BaseController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/register")
    public ResponseEntity<UserModel> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        UserModel newUser = userService.registerUser(registrationDto);
        return ResponseEntity.ok(newUser);
    }
}