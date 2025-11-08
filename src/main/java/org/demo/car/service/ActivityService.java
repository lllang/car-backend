package org.demo.car.service;

import org.demo.car.common.result.PageResult;
import org.demo.car.dto.request.ActivityRequest;
import org.demo.car.entity.Activity;

import java.util.List;

/**
 * 活动服务接口
 */
public interface ActivityService {
    
    /**
     * 根据类型获取活动列表
     */
    List<Activity> getActivitiesByType(String type);
    
    /**
     * 获取活动详情
     */
    Activity getActivityById(Long id);
    
    /**
     * 分页查询活动列表（管理端）
     */
    PageResult<Activity> getActivityPage(String name, String type, Integer status,
                                         Integer pageNum, Integer pageSize);
    
    /**
     * 创建活动
     */
    Long createActivity(ActivityRequest request);
    
    /**
     * 更新活动
     */
    void updateActivity(Long id, ActivityRequest request);
    
    /**
     * 删除活动
     */
    void deleteActivity(Long id);
    
    /**
     * 上下架活动
     */
    void updateActivityStatus(Long id, Integer status);
}

