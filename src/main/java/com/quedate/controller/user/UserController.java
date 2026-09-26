package com.quedate.controller.user;

import com.quedate.dto.user.UserResponseDTO;
import com.quedate.dto.user.UserUpdateDTO;
import com.quedate.security.UserPrincipal;
import com.quedate.service.auth.AuthService;
import com.quedate.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/me")
    public UserResponseDTO getCurrentUser(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return authService.getCurrentUser(principal);
    }

    @PutMapping("/me")
    public UserResponseDTO updateCurrentUser(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UserUpdateDTO request
    ) {
        return userService.update(principal.getUserId(), request);
    }
}