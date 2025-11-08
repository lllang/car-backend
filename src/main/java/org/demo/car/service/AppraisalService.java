package org.demo.car.service;

import org.demo.car.common.result.PageResult;
import org.demo.car.dto.request.AppraisalRequest;
import org.demo.car.entity.VehicleAppraisal;

import java.util.List;

/**
 * 估价服务接口
 */
public interface AppraisalService {
    
    /**
     * 提交估价申请
     */
    Long submitAppraisal(Long userId, AppraisalRequest request);
    
    /**
     * 获取我的估价列表
     */
    List<VehicleAppraisal> getMyAppraisals(Long userId);
    
    /**
     * 获取估价详情
     */
    VehicleAppraisal getAppraisalById(Long id);
    
    /**
     * 分页查询估价列表（管理端）
     */
    PageResult<VehicleAppraisal> getAppraisalPage(String phone, String oldBrand, 
                                                   String intentionBrand, String status,
                                                   Long followerId, Integer pageNum, Integer pageSize);
    
    /**
     * 跟进估价
     */
    void followAppraisal(Long id, Long followerId, String followerName, String remark);
    
    /**
     * 更新估价状态
     */
    void updateAppraisalStatus(Long id, String status);
}

