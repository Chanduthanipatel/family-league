package fourth.project.end.bootstrap;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import fourth.project.end.auth.config.BootstrapAdminProperties;
import fourth.project.end.domain.enums.UserStatus;
import fourth.project.end.domain.model.AppRole;
import fourth.project.end.domain.model.AppUser;
import fourth.project.end.domain.model.AppUserRole;
import fourth.project.end.domain.repository.AppRoleRepository;
import fourth.project.end.domain.repository.AppUserRepository;
import fourth.project.end.domain.repository.AppUserRoleRepository;

@Component
public class AdminBootstrapRunner implements ApplicationRunner {

    private final BootstrapAdminProperties bootstrapAdminProperties;
    private final AppUserRepository appUserRepository;
    private final AppRoleRepository appRoleRepository;
    private final AppUserRoleRepository appUserRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminBootstrapRunner(
        BootstrapAdminProperties bootstrapAdminProperties,
        AppUserRepository appUserRepository,
        AppRoleRepository appRoleRepository,
        AppUserRoleRepository appUserRoleRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.bootstrapAdminProperties = bootstrapAdminProperties;
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.appUserRoleRepository = appUserRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!StringUtils.hasText(bootstrapAdminProperties.getEmail())
            || !StringUtils.hasText(bootstrapAdminProperties.getPassword())) {
            return;
        }

        AppUser adminUser = appUserRepository.findByEmailAndDeletedFalse(bootstrapAdminProperties.getEmail())
            .orElseGet(this::createAdminUser);

        AppRole adminRole = appRoleRepository.findByCodeAndDeletedFalse("ADMIN")
            .orElseThrow(() -> new IllegalStateException("ADMIN role is not available"));

        boolean alreadyAssigned = appUserRoleRepository.findAllByUserIdAndDeletedFalse(adminUser.getId()).stream()
            .anyMatch(userRole -> adminRole.getId().equals(userRole.getRole().getId()));

        if (!alreadyAssigned) {
            AppUserRole userRole = new AppUserRole();
            userRole.setUser(adminUser);
            userRole.setRole(adminRole);
            appUserRoleRepository.save(userRole);
        }
    }

    private AppUser createAdminUser() {
        AppUser user = new AppUser();
        user.setEmail(bootstrapAdminProperties.getEmail());
        user.setPasswordHash(passwordEncoder.encode(bootstrapAdminProperties.getPassword()));
        user.setDisplayName(bootstrapAdminProperties.getDisplayName());
        user.setStatus(UserStatus.ACTIVE);
        return appUserRepository.save(user);
    }
}
