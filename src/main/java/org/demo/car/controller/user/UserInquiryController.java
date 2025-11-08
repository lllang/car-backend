package org.demo.car.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.result.Result;
import org.demo.car.dto.request.InquiryRequest;
import org.demo.car.entity.VehicleInquiry;
import org.demo.car.security.SecurityUtils;
import org.demo.car.service.InquiryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * C端询价控制器
 */
@Slf4j
@RestController
@RequestMapping("/user/inquiry")
@RequiredArgsConstructor
public class UserInquiryController {

    private final InquiryService inquiryService;

    /**
     * 提交询价
     */
    @PostMapping
    public Result<Long> submitInquiry(@Valid @RequestBody InquiryRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("用户提交询价: userId={}, vehicleId={}", userId, request.getVehicleId());
        Long id = inquiryService.submitInquiry(userId, request);
        return Result.success(id);
    }

    /**
     * 获取我的询价列表
     */
    @GetMapping("/list")
    public Result<List<VehicleInquiry>> getMyInquiries() {
        Long userId = SecurityUtils.getCurrentUserId();
        log.debug("获取我的询价列表: userId={}", userId);
        List<VehicleInquiry> list = inquiryService.getMyInquiries(userId);
        return Result.success(list);
    }

    /**
     * 获取询价详情
     */
    @GetMapping("/{id}")
    public Result<VehicleInquiry> getInquiryDetail(@PathVariable Long id) {
        log.debug("获取询价详情: inquiryId={}", id);
        VehicleInquiry inquiry = inquiryService.getInquiryById(id);
        return Result.success(inquiry);
    }
}

