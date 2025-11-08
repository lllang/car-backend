package org.demo.car.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.result.Result;
import org.demo.car.entity.Activity;
import org.demo.car.service.ActivityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * C端活动控制器
 */
@Slf4j
@RestController
@RequestMapping("/user/activity")
@RequiredArgsConstructor
public class UserActivityController {

    private final ActivityService activityService;

    /**
     * 获取限时优惠列表
     */
    @GetMapping("/limited-offers")
    public Result<List<Activity>> getLimitedOffers() {
        log.debug("获取限时优惠列表");
        List<Activity> list = activityService.getActivitiesByType("LIMITED_OFFER");
        return Result.success(list);
    }

    /**
     * 获取活动中心列表
     */
    @GetMapping("/events")
    public Result<List<Activity>> getEvents() {
        log.debug("获取活动中心列表");
        List<Activity> list = activityService.getActivitiesByType("EVENT");
        return Result.success(list);
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
}

