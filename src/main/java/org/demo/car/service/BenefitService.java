package org.demo.car.service;

import org.demo.car.common.result.PageResult;
import org.demo.car.dto.request.BenefitRequest;
import org.demo.car.dto.request.ClaimBenefitRequest;
import org.demo.car.entity.Benefit;
import org.demo.car.entity.UserBenefit;

import java.util.List;

/**
 * 权益服务接口
 */
public interface BenefitService {
    
    /**
     * 获取所有可用权益
     */
    List<Benefit> getAllActiveBenefits();
    
    /**
     * 获取权益详情
     */
    Benefit getBenefitById(Long id);
    
    /**
     * 领取权益
     */
    Long claimBenefit(Long userId, ClaimBenefitRequest request);
    
    /**
     * 获取我的权益领取记录
     */
    List<UserBenefit> getMyBenefits(Long userId);
    
    /**
     * 使用权益
     */
    void useBenefit(Long id, Long userId);
    
    /**
     * 分页查询权益列表（管理端）
     */
    PageResult<Benefit> getBenefitPage(String name, String type, Integer status,
                                       Integer pageNum, Integer pageSize);
    
    /**
     * 创建权益
     */
    Long createBenefit(BenefitRequest request);
    
    /**
     * 更新权益
     */
    void updateBenefit(Long id, BenefitRequest request);
    
    /**
     * 删除权益
     */
    void deleteBenefit(Long id);
    
    /**
     * 分页查询用户权益领取记录（管理端）
     */
    PageResult<UserBenefit> getUserBenefitPage(Long userId, Long benefitId, String phone,
                                               String status, Integer pageNum, Integer pageSize);
}

