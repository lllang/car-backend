package org.demo.car.security;

import lombok.Getter;
import org.demo.car.entity.Admin;
import org.demo.car.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义用户详情
 */
@Getter
public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final String username;
    private final String password;
    private final String userType; // "USER" 或 "ADMIN"
    private final List<String> permissions;
    private final boolean enabled;

    // C端用户构造
    public CustomUserDetails(User user) {
        this.userId = user.getId();
        this.username = user.getOpenid();
        this.password = "";  // C端用户无密码
        this.userType = "USER";
        this.permissions = List.of();
        this.enabled = true;
    }

    // 管理员构造
    public CustomUserDetails(Admin admin, List<String> permissions) {
        this.userId = admin.getId();
        this.username = admin.getUsername();
        this.password = admin.getPassword();
        this.userType = "ADMIN";
        this.permissions = permissions;
        this.enabled = admin.getStatus() == 1;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toCollection(ArrayList::new));
        if (isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (isUser()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
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
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAdmin() {
        return "ADMIN".equals(userType);
    }

    public boolean isUser() {
        return "USER".equals(userType);
    }
}

