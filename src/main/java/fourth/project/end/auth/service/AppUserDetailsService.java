package fourth.project.end.auth.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fourth.project.end.auth.security.UserPrincipal;
import fourth.project.end.domain.model.AppUser;
import fourth.project.end.domain.repository.AppUserRepository;
import fourth.project.end.domain.repository.AppUserRoleRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final AppUserRoleRepository appUserRoleRepository;

    public AppUserDetailsService(
        AppUserRepository appUserRepository,
        AppUserRoleRepository appUserRoleRepository
    ) {
        this.appUserRepository = appUserRepository;
        this.appUserRoleRepository = appUserRoleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByEmailAndDeletedFalse(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<String> roleCodes = appUserRoleRepository.findAllByUserIdAndDeletedFalse(user.getId()).stream()
            .map(userRole -> userRole.getRole().getCode())
            .toList();

        return new UserPrincipal(user, roleCodes);
    }
}
