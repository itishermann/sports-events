package com.etna.sportevents.security;

import com.etna.sportevents.models.UserEntity;
import com.etna.sportevents.models.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

public class JwtUserDetails implements UserDetails {
    private final UserEntity user;

    public JwtUserDetails(UserEntity user) {
        this.user = user;
    }

    public Integer getId() {
        return user.getId();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    public UserRole getRole() {
        return user.getRole();
    }

    public UserEntity getUser() {
        return user;
    }

    public boolean isAdmin() {
        return user.getRole() == UserRole.ROLE_ADMIN;
    }

    public boolean isUser() {
        return user.getRole() == UserRole.ROLE_USER || user.getRole() == UserRole.ROLE_MODERATOR || user.getRole() == UserRole.ROLE_ADMIN;
    }

    public boolean isModerator() {
        return user.getRole() == UserRole.ROLE_MODERATOR || user.getRole() == UserRole.ROLE_ADMIN;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        UserRole role = user.getRole();
        authorities.add(new SimpleGrantedAuthority(role.toString()));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
