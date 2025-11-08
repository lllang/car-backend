package org.demo.car.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.result.Result;
import org.demo.car.dto.request.ClaimBenefitRequest;
import org.demo.car.entity.Benefit;
import org.demo.car.entity.UserBenefit;
import org.demo.car.security.SecurityUtils;
import org.demo.car.service.BenefitService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * C端权益控制器
 */
@Slf4j
@RestController
@RequestMapping("/user/benefit")
@RequiredArgsConstructor
public class UserBenefitController {

    private final BenefitService benefitService;

    /**
     * 获取权益列表
     */
    @GetMapping("/list")
    public Result<List<Benefit>> getBenefitList() {
        log.debug("获取权益列表");
        List<Benefit> list = benefitService.getAllActiveBenefits();
        return Result.success(list);
    }

    /**
     * 领取权益
     */
    @PostMapping("/claim")
    public Result<Long> claimBenefit(@Valid @RequestBody ClaimBenefitRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("用户领取权益: userId={}, benefitId={}", userId, request.getBenefitId());
        Long id = benefitService.claimBenefit(userId, request);
        return Result.success(id);
    }

    /**
     * 获取我的权益领取记录
     */
    @GetMapping("/my-list")
    public Result<List<UserBenefit>> getMyBenefits() {
        Long userId = SecurityUtils.getCurrentUserId();
        log.debug("获取我的权益记录: userId={}", userId);
        List<UserBenefit> list = benefitService.getMyBenefits(userId);
        return Result.success(list);
    }

    /**
     * 使用权益
     */
    @PutMapping("/{id}/use")
    public Result<Void> useBenefit(@PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("用户使用权益: userId={}, userBenefitId={}", userId, id);
        benefitService.useBenefit(id, userId);
        return Result.success();
    }
}

