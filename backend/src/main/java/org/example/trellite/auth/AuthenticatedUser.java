package org.example.trellite.auth;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.trellite.user.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.security.Principal;
import java.util.Collection;

/**
 * Helper layer, resolves User and OrgMembers fields.
 * [User.email & User.password, OrgMembers.role] -> AuthenticatedUser -> UserDetailsServiceImpl
 */
@Deprecated
@RequiredArgsConstructor
public class AuthenticatedUser implements UserDetails, Principal {

    @Getter private final User user;
    private final Collection<GrantedAuthority> authorities;


    /**
     * Refers to user's email.
     * @return user's email
     */
    @Override
    public String getName() {
        return user.getEmail();
    }

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Authentification identifier. Refers to user's email.
     * @return user's email
     */
    @Override
    @NonNull
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.getAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
    
}
