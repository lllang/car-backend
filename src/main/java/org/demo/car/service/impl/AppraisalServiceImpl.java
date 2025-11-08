package org.demo.car.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.enums.AppraisalStatus;
import org.demo.car.common.exception.BusinessException;
import org.demo.car.common.result.PageResult;
import org.demo.car.dto.request.AppraisalRequest;
import org.demo.car.entity.VehicleAppraisal;
import org.demo.car.mapper.VehicleAppraisalMapper;
import org.demo.car.service.AppraisalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 估价服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppraisalServiceImpl implements AppraisalService {

    private final VehicleAppraisalMapper appraisalMapper;

    @Override
    @Transactional
    public Long submitAppraisal(Long userId, AppraisalRequest request) {
        log.info("用户提交估价申请: userId={}, phone={}, oldBrand={}, intentionBrand={}", 
                userId, request.getPhone(), request.getOldBrand(), request.getIntentionBrand());
        
        VehicleAppraisal appraisal = VehicleAppraisal.builder()
                .userId(userId)
                .phone(request.getPhone())
                .oldBrand(request.getOldBrand())
                .intentionBrand(request.getIntentionBrand())
                .purchaseYear(request.getPurchaseYear())
                .region(request.getRegion())
                .status(AppraisalStatus.PENDING.getCode())
                .build();
        
        appraisalMapper.insert(appraisal);
        log.info("估价申请提交成功: appraisalId={}", appraisal.getId());
        return appraisal.getId();
    }

    @Override
    public List<VehicleAppraisal> getMyAppraisals(Long userId) {
        log.debug("获取用户估价列表: userId={}", userId);
        return appraisalMapper.findByUserId(userId);
    }

    @Override
    public VehicleAppraisal getAppraisalById(Long id) {
        log.debug("获取估价详情: appraisalId={}", id);
        VehicleAppraisal appraisal = appraisalMapper.findById(id);
        if (appraisal == null) {
            log.warn("估价记录不存在: appraisalId={}", id);
            throw new BusinessException("估价记录不存在");
        }
        return appraisal;
    }

    @Override
    public PageResult<VehicleAppraisal> getAppraisalPage(String phone, String oldBrand,
                                                         String intentionBrand, String status,
                                                         Long followerId, Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum <= 0) pageNum = 1;
        if (pageSize == null || pageSize <= 0) pageSize = 10;
        
        log.debug("分页查询估价列表: phone={}, status={}, pageNum={}, pageSize={}", 
                phone, status, pageNum, pageSize);
        
        int offset = (pageNum - 1) * pageSize;
        List<VehicleAppraisal> list = appraisalMapper.findByPage(
                phone, oldBrand, intentionBrand, status, followerId, offset, pageSize);
        long total = appraisalMapper.countByPage(
                phone, oldBrand, intentionBrand, status, followerId);
        
        return new PageResult<>(list, total, pageNum, pageSize);
    }

    @Override
    @Transactional
    public void followAppraisal(Long id, Long followerId, String followerName, String remark) {
        log.info("跟进估价: appraisalId={}, followerId={}, followerName={}", 
                id, followerId, followerName);
        
        VehicleAppraisal appraisal = appraisalMapper.findById(id);
        if (appraisal == null) {
            log.error("估价记录不存在: appraisalId={}", id);
            throw new BusinessException("估价记录不存在");
        }
        
        appraisal.setStatus(AppraisalStatus.FOLLOWING.getCode());
        appraisal.setFollowerId(followerId);
        appraisal.setFollowerName(followerName);
        appraisal.setFollowTime(LocalDateTime.now());
        appraisal.setRemark(remark);
        
        appraisalMapper.updateFollow(appraisal);
        log.info("估价跟进成功: appraisalId={}", id);
    }

    @Override
    @Transactional
    public void updateAppraisalStatus(Long id, String status) {
        log.info("更新估价状态: appraisalId={}, status={}", id, status);
        
        VehicleAppraisal appraisal = appraisalMapper.findById(id);
        if (appraisal == null) {
            log.error("估价记录不存在: appraisalId={}", id);
            throw new BusinessException("估价记录不存在");
        }
        
        appraisal.setStatus(status);
        appraisalMapper.updateFollow(appraisal);
        log.info("估价状态更新成功: appraisalId={}", id);
    }
}

