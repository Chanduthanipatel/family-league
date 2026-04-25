package fourth.project.end.auth.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import fourth.project.end.domain.enums.UserStatus;
import fourth.project.end.domain.model.AppUser;

public class UserPrincipal implements UserDetails {

    private final Long userId;
    private final String username;
    private final String password;
    private final String displayName;
    private final UserStatus status;
    private final List<SimpleGrantedAuthority> authorities;

    public UserPrincipal(AppUser user, List<String> roleCodes) {
        this.userId = user.getId();
        this.username = user.getEmail();
        this.password = user.getPasswordHash();
        this.displayName = user.getDisplayName();
        this.status = user.getStatus();
        this.authorities = roleCodes.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .toList();
    }

    public Long getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getRoleCodes() {
        return authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .map(value -> value.replaceFirst("^ROLE_", ""))
            .toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != UserStatus.LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == UserStatus.ACTIVE;
    }
}
