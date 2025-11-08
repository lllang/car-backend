package org.demo.car.security;

import org.demo.car.common.exception.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Security工具类
 */
public class SecurityUtils {

    /**
     * 获取当前登录用户
     */
    public static CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("未登录");
        }
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return (CustomUserDetails) principal;
        }
        
        throw new AuthenticationException("无效的用户信息");
    }

    /**
     * 获取当前用户ID
     */
    public static Long getCurrentUserId() {
        return getCurrentUser().getUserId();
    }

    /**
     * 获取当前用户名
     */
    public static String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }

    /**
     * 判断当前用户是否是管理员
     */
    public static boolean isAdmin() {
        try {
            return getCurrentUser().isAdmin();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断当前用户是否是C端用户
     */
    public static boolean isUser() {
        try {
            return getCurrentUser().isUser();
        } catch (Exception e) {
            return false;
        }
    }
}

