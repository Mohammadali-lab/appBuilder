package ir.samin.appbuilder.security;

import ir.samin.appbuilder.entity.BaseEntity;
import ir.samin.appbuilder.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final BaseEntity user;

    public CustomUserDetails(BaseEntity user) {
        this.user = user;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        if (user instanceof User) {
            return ((User) user).getUsername();
        }
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        if (user instanceof User) {
            return ((User) user).isEnabled();
        }
        return false;
    }
}
