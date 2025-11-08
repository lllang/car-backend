package org.demo.car.controller.user;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.result.Result;
import org.demo.car.dto.request.BindPhoneRequest;
import org.demo.car.dto.request.UpdateUserInfoRequest;
import org.demo.car.dto.request.UserLoginRequest;
import org.demo.car.dto.response.UserResponse;
import org.demo.car.entity.User;
import org.demo.car.security.CustomUserDetails;
import org.demo.car.security.SecurityUtils;
import org.demo.car.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * C端用户认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserService userService;

    /**
     * 用户登录（微信授权）
     */
    @PostMapping("/login")
    public Result<UserResponse> login(@RequestBody UserLoginRequest request, HttpSession session) {
        User user = userService.login(request);
        
        // 创建认证对象
        CustomUserDetails userDetails = new CustomUserDetails(user);
        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 存储到Session
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        
        log.info("用户登录成功: userId={}", user.getId());
        return Result.success(UserResponse.from(user));
    }

    /**
     * 绑定手机号
     */
    @PostMapping("/bind-phone")
    public Result<Void> bindPhone(@Valid @RequestBody BindPhoneRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.bindPhone(userId, request);
        return Result.success();
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<UserResponse> getUserInfo() {
        Long userId = SecurityUtils.getCurrentUserId();
        User user = userService.getUserById(userId);
        return Result.success(UserResponse.from(user));
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/info")
    public Result<Void> updateUserInfo(@RequestBody UpdateUserInfoRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.updateUserInfo(userId, request);
        return Result.success();
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public Result<Void> logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();
        return Result.success();
    }
}

