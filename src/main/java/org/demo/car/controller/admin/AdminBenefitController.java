package org.demo.car.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.result.PageResult;
import org.demo.car.common.result.Result;
import org.demo.car.dto.request.BenefitRequest;
import org.demo.car.entity.Benefit;
import org.demo.car.entity.UserBenefit;
import org.demo.car.service.BenefitService;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端权益控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/benefit")
@RequiredArgsConstructor
public class AdminBenefitController {

    private final BenefitService benefitService;

    /**
     * 分页查询权益列表
     */
    @GetMapping("/list")
    public Result<PageResult<Benefit>> getBenefitPage(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        log.debug("分页查询权益列表: name={}, type={}, status={}", name, type, status);
        PageResult<Benefit> result = benefitService.getBenefitPage(
                name, type, status, pageNum, pageSize);
        return Result.success(result);
    }

    /**
     * 获取权益详情
     */
    @GetMapping("/{id}")
    public Result<Benefit> getBenefitDetail(@PathVariable Long id) {
        log.debug("获取权益详情: benefitId={}", id);
        Benefit benefit = benefitService.getBenefitById(id);
        return Result.success(benefit);
    }

    /**
     * 创建权益
     */
    @PostMapping
    @org.demo.car.security.RequirePermission("benefit:create")
    public Result<Long> createBenefit(@Valid @RequestBody BenefitRequest request) {
        log.info("创建权益: name={}, code={}", request.getName(), request.getCode());
        Long id = benefitService.createBenefit(request);
        return Result.success(id);
    }

    /**
     * 更新权益
     */
    @PutMapping("/{id}")
    @org.demo.car.security.RequirePermission("benefit:update")
    public Result<Void> updateBenefit(@PathVariable Long id, 
                                      @Valid @RequestBody BenefitRequest request) {
        log.info("更新权益: benefitId={}", id);
        benefitService.updateBenefit(id, request);
        return Result.success();
    }

    /**
     * 删除权益
     */
    @DeleteMapping("/{id}")
    @org.demo.car.security.RequirePermission("benefit:delete")
    public Result<Void> deleteBenefit(@PathVariable Long id) {
        log.info("删除权益: benefitId={}", id);
        benefitService.deleteBenefit(id);
        return Result.success();
    }

    /**
     * 分页查询用户权益领取记录
     */
    @GetMapping("/user-benefit/list")
    public Result<PageResult<UserBenefit>> getUserBenefitPage(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long benefitId,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        log.debug("分页查询用户权益领取记录: userId={}, benefitId={}", userId, benefitId);
        PageResult<UserBenefit> result = benefitService.getUserBenefitPage(
                userId, benefitId, phone, status, pageNum, pageSize);
        return Result.success(result);
    }
}

