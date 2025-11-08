package org.demo.car.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.exception.AuthorizationException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 权限拦截器
 */
@Slf4j
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
            throws Exception {
        
        // 不是方法处理器，直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequirePermission annotation = handlerMethod.getMethodAnnotation(RequirePermission.class);
        
        // 没有权限注解，直接放行
        if (annotation == null) {
            return true;
        }
        
        String requiredPermission = annotation.value();
        log.debug("检查权限: {}", requiredPermission);
        
        try {
            // 获取当前登录用户
            CustomUserDetails userDetails = SecurityUtils.getCurrentUser();
            
            // 超级管理员拥有所有权限
            if (userDetails.getPermissions().contains("*")) {
                log.debug("超级管理员，拥有所有权限");
                return true;
            }
            
            // 检查是否拥有所需权限
            if (!userDetails.getPermissions().contains(requiredPermission)) {
                log.warn("权限不足: userId={}, username={}, requiredPermission={}", 
                        userDetails.getUserId(), userDetails.getUsername(), requiredPermission);
                throw new AuthorizationException("权限不足，无法访问该功能");
            }
            
            log.debug("权限验证通过: userId={}, permission={}", 
                    userDetails.getUserId(), requiredPermission);
            return true;
            
        } catch (AuthorizationException e) {
            throw e;
        } catch (Exception e) {
            log.error("权限验证失败", e);
            throw new AuthorizationException("权限验证失败");
        }
    }
}

