package org.demo.car.controller.admin;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.exception.BusinessException;
import org.demo.car.common.result.Result;
import org.demo.car.dto.request.LoginRequest;
import org.demo.car.entity.Admin;
import org.demo.car.entity.Permission;
import org.demo.car.mapper.AdminMapper;
import org.demo.car.mapper.PermissionMapper;
import org.demo.car.security.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理员认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminMapper adminMapper;
    private final PermissionMapper permissionMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        // 查询管理员
        Admin admin = adminMapper.findByUsername(request.getUsername());
        if (admin == null) {
            throw new BusinessException("用户名或密码错误");
        }
        
        // 验证密码（明文比对）
        if (!request.getPassword().equals(admin.getPassword())) {
            log.warn("密码验证失败: username={}", request.getUsername());
            throw new BusinessException("用户名或密码错误");
        }
        
        // 检查状态
        if (admin.getStatus() != 1) {
            throw new BusinessException("账号已被禁用");
        }
        
        // 获取权限
        List<Permission> permissions = permissionMapper.findByRoleId(admin.getRoleId());
        List<String> permissionCodes = permissions.stream()
            .map(Permission::getCode)
            .collect(Collectors.toList());
        
        // 创建认证对象
        CustomUserDetails userDetails = new CustomUserDetails(admin, permissionCodes);
        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 存储到Session
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        
        // 返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("id", admin.getId());
        data.put("username", admin.getUsername());
        data.put("realName", admin.getRealName());
        data.put("roleId", admin.getRoleId());
        data.put("permissions", permissionCodes);
        
        log.info("管理员登录成功: username={}", admin.getUsername());
        return Result.success(data);
    }

    /**
     * 获取当前管理员信息
     */
    @GetMapping("/info")
    public Result<Map<String, Object>> getInfo() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
            .getContext().getAuthentication().getPrincipal();
        
        Admin admin = adminMapper.findById(userDetails.getUserId());
        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("id", admin.getId());
        data.put("username", admin.getUsername());
        data.put("realName", admin.getRealName());
        data.put("roleId", admin.getRoleId());
        data.put("permissions", userDetails.getPermissions());
        
        return Result.success(data);
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

