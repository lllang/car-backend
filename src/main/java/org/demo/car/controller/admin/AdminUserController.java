package org.demo.car.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.result.PageResult;
import org.demo.car.common.result.Result;
import org.demo.car.entity.User;
import org.demo.car.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserMapper userMapper;

    /**
     * 分页查询用户列表
     */
    @GetMapping("/list")
    public Result<PageResult<User>> getUserPage(
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String nickname,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        log.debug("分页查询用户列表: phone={}, nickname={}", phone, nickname);
        
        if (pageNum == null || pageNum <= 0) pageNum = 1;
        if (pageSize == null || pageSize <= 0) pageSize = 10;
        
        int offset = (pageNum - 1) * pageSize;
        var list = userMapper.findByPage(phone, nickname, offset, pageSize);
        var total = userMapper.countByPage(phone, nickname);
        
        PageResult<User> result = new PageResult<>(list, total, pageNum, pageSize);
        return Result.success(result);
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/{id}")
    public Result<User> getUserDetail(@PathVariable Long id) {
        log.debug("获取用户详情: userId={}", id);
        User user = userMapper.findById(id);
        return Result.success(user);
    }
}

