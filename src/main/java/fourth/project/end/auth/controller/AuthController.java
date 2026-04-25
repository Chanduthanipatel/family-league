package fourth.project.end.auth.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fourth.project.end.auth.dto.AuthResponse;
import fourth.project.end.auth.dto.LoginRequest;
import fourth.project.end.auth.dto.MeResponse;
import fourth.project.end.auth.dto.RegisterRequest;
import fourth.project.end.auth.security.UserPrincipal;
import fourth.project.end.auth.service.AuthenticationService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authenticationService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authenticationService.login(request);
    }

    @GetMapping("/me")
    public MeResponse me(@AuthenticationPrincipal UserPrincipal principal) {
        return authenticationService.me(principal);
    }
}
