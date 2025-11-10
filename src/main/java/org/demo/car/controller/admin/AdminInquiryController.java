package org.demo.car.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.result.PageResult;
import org.demo.car.common.result.Result;
import org.demo.car.entity.VehicleInquiry;
import org.demo.car.security.SecurityUtils;
import org.demo.car.service.InquiryService;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端询价控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/inquiry")
@RequiredArgsConstructor
public class AdminInquiryController {

    private final InquiryService inquiryService;

    /**
     * 分页查询询价列表
     */
    @GetMapping("/list")
    public Result<PageResult<VehicleInquiry>> getInquiryPage(
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer needExchange,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        log.debug("分页查询询价列表: phone={}, brandId={}, status={}", phone, brandId, status);
        PageResult<VehicleInquiry> result = inquiryService.getInquiryPage(
                phone, brandId, status, needExchange, pageNum, pageSize);
        return Result.success(result);
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

    /**
     * 更新询价状态
     */
    @PutMapping("/{id}/status")
    public Result<Void> updateInquiryStatus(@PathVariable Long id, 
                                            @RequestParam String status) {
        log.info("更新询价状态: inquiryId={}, status={}", id, status);
        inquiryService.updateInquiryStatus(id, status);
        return Result.success();
    }
    
    /**
     * 更新询价信息（状态、备注）
     */
    @PutMapping("/{id}")
    public Result<Void> updateInquiry(@PathVariable Long id,
                                     @RequestParam String status,
                                     @RequestParam(required = false) String remark) {
        Long handlerId = SecurityUtils.getCurrentUserId();
        String handlerName = SecurityUtils.getCurrentUsername();
        
        log.info("更新询价: inquiryId={}, status={}, remark={}, handlerId={}", 
                id, status, remark, handlerId);
        inquiryService.updateInquiry(id, status, remark, handlerId, handlerName);
        return Result.success();
    }
}

