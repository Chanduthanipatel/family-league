package fourth.project.end.auth.service;

import java.time.Instant;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;

import fourth.project.end.auth.dto.AuthResponse;
import fourth.project.end.auth.dto.LoginRequest;
import fourth.project.end.auth.dto.MeResponse;
import fourth.project.end.auth.dto.RegisterRequest;
import fourth.project.end.auth.security.UserPrincipal;
import fourth.project.end.common.exception.ConflictException;
import fourth.project.end.domain.enums.UserStatus;
import fourth.project.end.domain.model.AppRole;
import fourth.project.end.domain.model.AppUser;
import fourth.project.end.domain.model.AppUserRole;
import fourth.project.end.domain.repository.AppRoleRepository;
import fourth.project.end.domain.repository.AppUserRepository;
import fourth.project.end.domain.repository.AppUserRoleRepository;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AppUserRepository appUserRepository;
    private final AppRoleRepository appRoleRepository;
    private final AppUserRoleRepository appUserRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            AppUserRepository appUserRepository,
            AppRoleRepository appRoleRepository,
            AppUserRoleRepository appUserRoleRepository,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.appUserRoleRepository = appUserRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        try {
            UserPrincipal principal = (UserPrincipal) authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())).getPrincipal();

            AppUser user = appUserRepository.findById(principal.getUserId())
                    .orElseThrow(() -> new BadCredentialsException("User not found"));
            user.setLastLoginAt(Instant.now());

            return new AuthResponse(
                    jwtService.generateToken(principal),
                    "Bearer",
                    jwtService.getExpirationSeconds(),
                    principal.getUserId(),
                    principal.getUsername(),
                    principal.getDisplayName(),
                    principal.getRoleCodes());
        } catch (DisabledException ex) {
            throw new BadCredentialsException("User account is inactive", ex);
        }
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (appUserRepository.existsByEmailAndDeletedFalse(request.email())) {
            throw new ConflictException("Email already registered: " + request.email());
        }

        AppUser user = new AppUser();
        user.setEmail(request.email().toLowerCase().trim());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setDisplayName(request.displayName().trim());
        user.setStatus(UserStatus.ACTIVE);
        user = appUserRepository.save(user);

        AppRole userRole = appRoleRepository.findByCodeAndDeletedFalse("USER")
                .orElseThrow(() -> new IllegalStateException("USER role not found"));

        AppUserRole appUserRole = new AppUserRole();
        appUserRole.setUser(user);
        appUserRole.setRole(userRole);
        appUserRoleRepository.save(appUserRole);

        UserPrincipal principal = new UserPrincipal(user, java.util.List.of("USER"));
        return new AuthResponse(
                jwtService.generateToken(principal),
                "Bearer",
                jwtService.getExpirationSeconds(),
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                java.util.List.of("USER"));
    }

    public MeResponse me(UserPrincipal principal) {
        return new MeResponse(
                principal.getUserId(),
                principal.getUsername(),
                principal.getDisplayName(),
                principal.getRoleCodes());
    }
}
