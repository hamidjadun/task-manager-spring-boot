package task_manager.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import task_manager.dto.RegisterRequest;
import task_manager.dto.LoginRequest;
import task_manager.dto.LoginResponse;
import task_manager.service.AuthService;
import task_manager.dto.RefreshTokenRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(
            @Valid @RequestBody RefreshTokenRequest request) {

        return authService.refreshAccessToken(request.getRefreshToken());
    }

    @PostMapping("/logout")
    public String logout(@Valid @RequestBody RefreshTokenRequest request) {

        authService.logout(request.getRefreshToken());

        return "Logged out successfully";
    }
}