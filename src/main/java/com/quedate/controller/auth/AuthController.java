package com.quedate.controller.auth;

import com.quedate.dto.auth.AuthResponseDTO;
import com.quedate.dto.auth.LoginRequestDTO;
import com.quedate.dto.auth.RefreshTokenRequestDTO;
import com.quedate.dto.auth.RegisterRequestDTO;
import com.quedate.service.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDTO register(
            @Valid @RequestBody RegisterRequestDTO request
    ) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(
            @Valid @RequestBody LoginRequestDTO request
    ) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public AuthResponseDTO refresh(
            @Valid @RequestBody RefreshTokenRequestDTO request
    ) {
        return authService.refresh(request);
    }
}