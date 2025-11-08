package org.demo.car.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.result.PageResult;
import org.demo.car.common.result.Result;
import org.demo.car.dto.request.ActivityRequest;
import org.demo.car.entity.Activity;
import org.demo.car.service.ActivityService;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端活动控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/activity")
@RequiredArgsConstructor
public class AdminActivityController {

    private final ActivityService activityService;

    /**
     * 分页查询活动列表
     */
    @GetMapping("/list")
    public Result<PageResult<Activity>> getActivityPage(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        log.debug("分页查询活动列表: name={}, type={}, status={}", name, type, status);
        PageResult<Activity> result = activityService.getActivityPage(
                name, type, status, pageNum, pageSize);
        return Result.success(result);
    }

    /**
     * 获取活动详情
     */
    @GetMapping("/{id}")
    public Result<Activity> getActivityDetail(@PathVariable Long id) {
        log.debug("获取活动详情: activityId={}", id);
        Activity activity = activityService.getActivityById(id);
        return Result.success(activity);
    }

    /**
     * 创建活动
     */
    @PostMapping
    @org.demo.car.security.RequirePermission("activity:create")
    public Result<Long> createActivity(@Valid @RequestBody ActivityRequest request) {
        log.info("创建活动: name={}, type={}", request.getName(), request.getType());
        Long id = activityService.createActivity(request);
        return Result.success(id);
    }

    /**
     * 更新活动
     */
    @PutMapping("/{id}")
    @org.demo.car.security.RequirePermission("activity:update")
    public Result<Void> updateActivity(@PathVariable Long id, 
                                       @Valid @RequestBody ActivityRequest request) {
        log.info("更新活动: activityId={}", id);
        activityService.updateActivity(id, request);
        return Result.success();
    }

    /**
     * 删除活动
     */
    @DeleteMapping("/{id}")
    @org.demo.car.security.RequirePermission("activity:delete")
    public Result<Void> deleteActivity(@PathVariable Long id) {
        log.info("删除活动: activityId={}", id);
        activityService.deleteActivity(id);
        return Result.success();
    }

    /**
     * 上下架活动
     */
    @PutMapping("/{id}/status")
    public Result<Void> updateActivityStatus(@PathVariable Long id, 
                                             @RequestParam Integer status) {
        log.info("更新活动状态: activityId={}, status={}", id, status);
        activityService.updateActivityStatus(id, status);
        return Result.success();
    }
}

