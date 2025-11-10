package org.demo.car.service;

import org.demo.car.common.result.PageResult;
import org.demo.car.dto.request.InquiryRequest;
import org.demo.car.entity.VehicleInquiry;

import java.util.List;

/**
 * 询价服务接口
 */
public interface InquiryService {
    
    /**
     * 提交询价
     */
    Long submitInquiry(Long userId, InquiryRequest request);
    
    /**
     * 获取我的询价列表
     */
    List<VehicleInquiry> getMyInquiries(Long userId);
    
    /**
     * 获取询价详情
     */
    VehicleInquiry getInquiryById(Long id);
    
    /**
     * 分页查询询价列表（管理端）
     */
    PageResult<VehicleInquiry> getInquiryPage(String phone, Long brandId, 
                                              String status, Integer needExchange,
                                              Integer pageNum, Integer pageSize);
    
    /**
     * 更新询价状态
     */
    void updateInquiryStatus(Long id, String status);
    
    /**
     * 更新询价信息（状态、备注、处理人）
     */
    void updateInquiry(Long id, String status, String remark, Long handlerId, String handlerName);
}

