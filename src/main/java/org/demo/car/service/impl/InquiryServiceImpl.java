package org.demo.car.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.enums.InquiryStatus;
import org.demo.car.common.exception.BusinessException;
import org.demo.car.common.result.PageResult;
import org.demo.car.dto.request.InquiryRequest;
import org.demo.car.entity.Brand;
import org.demo.car.entity.Vehicle;
import org.demo.car.entity.VehicleInquiry;
import org.demo.car.mapper.BrandMapper;
import org.demo.car.mapper.VehicleInquiryMapper;
import org.demo.car.mapper.VehicleMapper;
import org.demo.car.service.InquiryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 询价服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {

    private final VehicleInquiryMapper inquiryMapper;
    private final VehicleMapper vehicleMapper;
    private final BrandMapper brandMapper;

    @Override
    @Transactional
    public Long submitInquiry(Long userId, InquiryRequest request) {
        log.info("用户提交询价: userId={}, vehicleId={}, phone={}, needExchange={}", 
                userId, request.getVehicleId(), request.getPhone(), request.getNeedExchange());
        
        // 验证车辆存在
        Vehicle vehicle = vehicleMapper.findById(request.getVehicleId());
        if (vehicle == null) {
            log.error("车辆不存在: vehicleId={}", request.getVehicleId());
            throw new BusinessException("车辆不存在");
        }
        
        // 获取品牌和经销商信息
        Brand brand = brandMapper.findById(vehicle.getBrandId());
        String dealerName = brand != null ? brand.getDealerName() : null;
        
        VehicleInquiry inquiry = VehicleInquiry.builder()
                .userId(userId)
                .vehicleId(request.getVehicleId())
                .brandId(vehicle.getBrandId())
                .phone(request.getPhone())
                .needExchange(request.getNeedExchange() != null ? request.getNeedExchange() : 0)
                .dealerName(dealerName)
                .status(InquiryStatus.PENDING.getCode())
                .build();
        
        inquiryMapper.insert(inquiry);
        log.info("询价提交成功: inquiryId={}", inquiry.getId());
        return inquiry.getId();
    }

    @Override
    public List<VehicleInquiry> getMyInquiries(Long userId) {
        log.debug("获取用户询价列表: userId={}", userId);
        return inquiryMapper.findByUserId(userId);
    }

    @Override
    public VehicleInquiry getInquiryById(Long id) {
        log.debug("获取询价详情: inquiryId={}", id);
        VehicleInquiry inquiry = inquiryMapper.findById(id);
        if (inquiry == null) {
            log.warn("询价记录不存在: inquiryId={}", id);
            throw new BusinessException("询价记录不存在");
        }
        return inquiry;
    }

    @Override
    public PageResult<VehicleInquiry> getInquiryPage(String phone, Long brandId,
                                                     String status, Integer needExchange,
                                                     Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum <= 0) pageNum = 1;
        if (pageSize == null || pageSize <= 0) pageSize = 10;
        
        log.debug("分页查询询价列表: phone={}, brandId={}, status={}, pageNum={}, pageSize={}", 
                phone, brandId, status, pageNum, pageSize);
        
        int offset = (pageNum - 1) * pageSize;
        List<VehicleInquiry> list = inquiryMapper.findByPage(
                phone, brandId, status, needExchange, offset, pageSize);
        long total = inquiryMapper.countByPage(
                phone, brandId, status, needExchange);
        
        return new PageResult<>(list, total, pageNum, pageSize);
    }

    @Override
    @Transactional
    public void updateInquiryStatus(Long id, String status) {
        log.info("更新询价状态: inquiryId={}, status={}", id, status);
        
        VehicleInquiry inquiry = inquiryMapper.findById(id);
        if (inquiry == null) {
            log.error("询价记录不存在: inquiryId={}", id);
            throw new BusinessException("询价记录不存在");
        }
        
        inquiryMapper.updateStatus(id, status);
        log.info("询价状态更新成功: inquiryId={}", id);
    }
}

