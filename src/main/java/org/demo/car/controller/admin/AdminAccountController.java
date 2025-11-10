package org.demo.car.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.result.PageResult;
import org.demo.car.common.result.Result;
import org.demo.car.dto.request.AdminAccountRequest;
import org.demo.car.entity.Admin;
import org.demo.car.mapper.AdminMapper;
import org.demo.car.common.exception.BusinessException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 管理端账号管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/account")
@RequiredArgsConstructor
public class AdminAccountController {

    private final AdminMapper adminMapper;

    /**
     * 分页查询管理员账号列表
     */
    @GetMapping("/list")
    @org.demo.car.security.RequirePermission("permission:account:list")
    public Result<PageResult<Admin>> getAccountPage(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long roleId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        
        log.debug("分页查询管理员列表: keyword={}, roleId={}, status={}", keyword, roleId, status);
        
        if (page == null || page <= 0) page = 1;
        if (pageSize == null || pageSize <= 0) pageSize = 10;
        
        int offset = (page - 1) * pageSize;
        var list = adminMapper.findByPage(keyword, keyword, roleId, status, offset, pageSize);
        var total = adminMapper.countByPage(keyword, keyword, roleId, status);
        
        // 移除密码字段
        list.forEach(admin -> admin.setPassword(null));
        
        PageResult<Admin> result = new PageResult<>(list, total, page, pageSize);
        return Result.success(result);
    }

    /**
     * 获取管理员详情
     */
    @GetMapping("/{id}")
    @org.demo.car.security.RequirePermission("permission:account:list")
    public Result<Admin> getAccountDetail(@PathVariable Long id) {
        log.debug("获取管理员详情: adminId={}", id);
        Admin admin = adminMapper.findById(id);
        if (admin != null) {
            admin.setPassword(null);
        }
        return Result.success(admin);
    }

    /**
     * 创建管理员账号
     */
    @PostMapping
    @org.demo.car.security.RequirePermission("permission:account:create")
    public Result<Void> createAccount(@Valid @RequestBody AdminAccountRequest request) {
        log.info("创建管理员账号: username={}", request.getUsername());
        
        // 检查用户名是否已存在
        Admin existing = adminMapper.findByUsername(request.getUsername());
        if (existing != null) {
            throw new BusinessException("用户名已存在");
        }
        
        // 创建管理员
        Admin admin = new Admin();
        admin.setUsername(request.getUsername());
        admin.setPassword(request.getPassword()); // 暂时使用明文密码
        admin.setRealName(request.getRealName());
        admin.setPhone(request.getPhone());
        admin.setRoleId(request.getRoleId());
        admin.setStatus(request.getStatus() != null ? request.getStatus() : 1);
        admin.setCreateTime(LocalDateTime.now());
        admin.setUpdateTime(LocalDateTime.now());
        
        adminMapper.insert(admin);
        log.info("管理员账号创建成功: adminId={}", admin.getId());
        return Result.success();
    }

    /**
     * 更新管理员账号
     */
    @PutMapping("/{id}")
    @org.demo.car.security.RequirePermission("permission:account:update")
    public Result<Void> updateAccount(@PathVariable Long id, 
                                      @Valid @RequestBody AdminAccountRequest request) {
        log.info("更新管理员账号: adminId={}", id);
        
        Admin admin = adminMapper.findById(id);
        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }
        
        // 如果修改了用户名，检查是否已存在
        if (!admin.getUsername().equals(request.getUsername())) {
            Admin existing = adminMapper.findByUsername(request.getUsername());
            if (existing != null) {
                throw new BusinessException("用户名已存在");
            }
            admin.setUsername(request.getUsername());
        }
        
        // 更新字段
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            admin.setPassword(request.getPassword());
        }
        admin.setRealName(request.getRealName());
        admin.setPhone(request.getPhone());
        admin.setRoleId(request.getRoleId());
        if (request.getStatus() != null) {
            admin.setStatus(request.getStatus());
        }
        admin.setUpdateTime(LocalDateTime.now());
        
        adminMapper.update(admin);
        log.info("管理员账号更新成功: adminId={}", id);
        return Result.success();
    }

    /**
     * 删除管理员账号
     */
    @DeleteMapping("/{id}")
    @org.demo.car.security.RequirePermission("permission:account:delete")
    public Result<Void> deleteAccount(@PathVariable Long id) {
        log.info("删除管理员账号: adminId={}", id);
        
        // 不允许删除ID为1的超级管理员
        if (id == 1) {
            throw new BusinessException("不能删除超级管理员");
        }
        
        adminMapper.deleteById(id);
        log.info("管理员账号删除成功: adminId={}", id);
        return Result.success();
    }

    /**
     * 更新管理员状态
     */
    @PutMapping("/{id}/status")
    @org.demo.car.security.RequirePermission("permission:account:update")
    public Result<Void> updateAccountStatus(@PathVariable Long id, 
                                            @RequestParam Integer status) {
        log.info("更新管理员状态: adminId={}, status={}", id, status);
        
        // 不允许禁用ID为1的超级管理员
        if (id == 1 && status == 0) {
            throw new BusinessException("不能禁用超级管理员");
        }
        
        adminMapper.updateStatus(id, status);
        log.info("管理员状态更新成功: adminId={}", id);
        return Result.success();
    }

    /**
     * 重置管理员密码
     */
    @PutMapping("/{id}/reset-password")
    @org.demo.car.security.RequirePermission("permission:account:update")
    public Result<Void> resetPassword(@PathVariable Long id) {
        log.info("重置管理员密码: adminId={}", id);
        
        Admin admin = adminMapper.findById(id);
        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }
        
        // 重置为默认密码 123456
        admin.setPassword("123456");
        admin.setUpdateTime(LocalDateTime.now());
        adminMapper.update(admin);
        
        log.info("管理员密码重置成功: adminId={}", id);
        return Result.success();
    }
}

