package org.demo.car.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.result.Result;
import org.demo.car.dto.request.AppraisalRequest;
import org.demo.car.entity.VehicleAppraisal;
import org.demo.car.security.SecurityUtils;
import org.demo.car.service.AppraisalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * C端估价控制器
 */
@Slf4j
@RestController
@RequestMapping("/user/appraisal")
@RequiredArgsConstructor
public class UserAppraisalController {

    private final AppraisalService appraisalService;

    /**
     * 提交估价申请
     */
    @PostMapping
    public Result<Long> submitAppraisal(@Valid @RequestBody AppraisalRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("用户提交估价: userId={}", userId);
        Long id = appraisalService.submitAppraisal(userId, request);
        return Result.success(id);
    }

    /**
     * 获取我的估价列表
     */
    @GetMapping("/list")
    public Result<List<VehicleAppraisal>> getMyAppraisals() {
        Long userId = SecurityUtils.getCurrentUserId();
        log.debug("获取我的估价列表: userId={}", userId);
        List<VehicleAppraisal> list = appraisalService.getMyAppraisals(userId);
        return Result.success(list);
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
}

