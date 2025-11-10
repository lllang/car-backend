package org.demo.car.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.result.PageResult;
import org.demo.car.common.result.Result;
import org.demo.car.entity.VehicleAppraisal;
import org.demo.car.security.SecurityUtils;
import org.demo.car.service.AppraisalService;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端估价控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/appraisal")
@RequiredArgsConstructor
public class AdminAppraisalController {

    private final AppraisalService appraisalService;

    /**
     * 分页查询估价列表
     */
    @GetMapping("/list")
    public Result<PageResult<VehicleAppraisal>> getAppraisalPage(
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String oldBrand,
            @RequestParam(required = false) String intentionBrand,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long followerId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        log.debug("分页查询估价列表: phone={}, status={}", phone, status);
        PageResult<VehicleAppraisal> result = appraisalService.getAppraisalPage(
                phone, oldBrand, intentionBrand, status, followerId, pageNum, pageSize);
        return Result.success(result);
    }

    /**
     * 获取估价详情
     */
    @GetMapping("/{id}")
    public Result<VehicleAppraisal> getAppraisalDetail(@PathVariable Long id) {
        log.debug("获取估价详情: appraisalId={}", id);
        VehicleAppraisal appraisal = appraisalService.getAppraisalById(id);
        return Result.success(appraisal);
    }

    /**
     * 跟进估价（开始跟进）
     */
    @PutMapping("/{id}/follow")
    public Result<Void> followAppraisal(@PathVariable Long id, 
                                        @RequestParam(required = false) String remark) {
        Long followerId = SecurityUtils.getCurrentUserId();
        String followerName = SecurityUtils.getCurrentUsername();
        
        log.info("跟进估价: appraisalId={}, followerId={}, followerName={}", 
                id, followerId, followerName);
        appraisalService.followAppraisal(id, followerId, followerName, remark);
        return Result.success();
    }

    /**
     * 更新估价状态
     */
    @PutMapping("/{id}/status")
    public Result<Void> updateAppraisalStatus(@PathVariable Long id, 
                                              @RequestParam String status) {
        log.info("更新估价状态: appraisalId={}, status={}", id, status);
        appraisalService.updateAppraisalStatus(id, status);
        return Result.success();
    }
    
    /**
     * 更新备注
     */
    @PutMapping("/{id}/remark")
    public Result<Void> updateRemark(@PathVariable Long id,
                                    @RequestParam(required = false) String remark) {
        log.info("更新估价备注: appraisalId={}, remark={}", id, remark);
        appraisalService.updateRemark(id, remark);
        return Result.success();
    }
}

