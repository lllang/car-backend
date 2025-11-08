package org.demo.car.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.exception.BusinessException;
import org.demo.car.common.result.PageResult;
import org.demo.car.dto.request.ActivityRequest;
import org.demo.car.entity.Activity;
import org.demo.car.mapper.ActivityMapper;
import org.demo.car.service.ActivityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 活动服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityMapper activityMapper;

    @Override
    public List<Activity> getActivitiesByType(String type) {
        log.debug("获取活动列表: type={}", type);
        return activityMapper.findByType(type);
    }

    @Override
    public Activity getActivityById(Long id) {
        log.debug("获取活动详情: activityId={}", id);
        Activity activity = activityMapper.findById(id);
        if (activity == null) {
            log.warn("活动不存在: activityId={}", id);
            throw new BusinessException("活动不存在");
        }
        return activity;
    }

    @Override
    public PageResult<Activity> getActivityPage(String name, String type, Integer status,
                                                Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum <= 0) pageNum = 1;
        if (pageSize == null || pageSize <= 0) pageSize = 10;
        
        log.debug("分页查询活动列表: name={}, type={}, status={}, pageNum={}, pageSize={}", 
                name, type, status, pageNum, pageSize);
        
        int offset = (pageNum - 1) * pageSize;
        List<Activity> list = activityMapper.findByPage(name, type, status, offset, pageSize);
        long total = activityMapper.countByPage(name, type, status);
        
        return new PageResult<>(list, total, pageNum, pageSize);
    }

    @Override
    @Transactional
    public Long createActivity(ActivityRequest request) {
        log.info("创建活动: name={}, type={}", request.getName(), request.getType());
        
        Activity activity = Activity.builder()
                .name(request.getName())
                .type(request.getType())
                .image(request.getImage())
                .linkUrl(request.getLinkUrl())
                .vehicleId(request.getVehicleId())
                .content(request.getContent())
                .status(request.getStatus() != null ? request.getStatus() : 1)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                .build();
        
        activityMapper.insert(activity);
        log.info("活动创建成功: activityId={}", activity.getId());
        return activity.getId();
    }

    @Override
    @Transactional
    public void updateActivity(Long id, ActivityRequest request) {
        log.info("更新活动: activityId={}", id);
        
        Activity activity = activityMapper.findById(id);
        if (activity == null) {
            log.error("活动不存在: activityId={}", id);
            throw new BusinessException("活动不存在");
        }
        
        // 只更新非空字段
        if (request.getName() != null) {
            activity.setName(request.getName());
        }
        if (request.getType() != null) {
            activity.setType(request.getType());
        }
        if (request.getImage() != null) {
            activity.setImage(request.getImage());
        }
        if (request.getLinkUrl() != null) {
            activity.setLinkUrl(request.getLinkUrl());
        }
        if (request.getVehicleId() != null) {
            activity.setVehicleId(request.getVehicleId());
        }
        if (request.getContent() != null) {
            activity.setContent(request.getContent());
        }
        // status 字段通过专门的 updateActivityStatus 方法更新，这里不处理
        if (request.getStartTime() != null) {
            activity.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            activity.setEndTime(request.getEndTime());
        }
        if (request.getSortOrder() != null) {
            activity.setSortOrder(request.getSortOrder());
        }
        
        activityMapper.update(activity);
        log.info("活动更新成功: activityId={}", id);
    }

    @Override
    @Transactional
    public void deleteActivity(Long id) {
        log.info("删除活动: activityId={}", id);
        
        Activity activity = activityMapper.findById(id);
        if (activity == null) {
            log.error("活动不存在: activityId={}", id);
            throw new BusinessException("活动不存在");
        }
        
        activityMapper.deleteById(id);
        log.info("活动删除成功: activityId={}", id);
    }

    @Override
    @Transactional
    public void updateActivityStatus(Long id, Integer status) {
        log.info("更新活动状态: activityId={}, status={}", id, status);
        
        Activity activity = activityMapper.findById(id);
        if (activity == null) {
            log.error("活动不存在: activityId={}", id);
            throw new BusinessException("活动不存在");
        }
        
        activityMapper.updateStatus(id, status);
        log.info("活动状态更新成功: activityId={}", id);
    }
}

