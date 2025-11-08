package org.demo.car.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.enums.BenefitStatus;
import org.demo.car.common.exception.BusinessException;
import org.demo.car.common.result.PageResult;
import org.demo.car.dto.request.BenefitRequest;
import org.demo.car.dto.request.ClaimBenefitRequest;
import org.demo.car.entity.Benefit;
import org.demo.car.entity.UserBenefit;
import org.demo.car.mapper.BenefitMapper;
import org.demo.car.mapper.UserBenefitMapper;
import org.demo.car.service.BenefitService;
import org.demo.car.service.SmsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 权益服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BenefitServiceImpl implements BenefitService {

    private final BenefitMapper benefitMapper;
    private final UserBenefitMapper userBenefitMapper;
    private final SmsService smsService;

    @Override
    public List<Benefit> getAllActiveBenefits() {
        log.debug("获取所有可用权益");
        return benefitMapper.findAllActive();
    }

    @Override
    public Benefit getBenefitById(Long id) {
        log.debug("获取权益详情: benefitId={}", id);
        Benefit benefit = benefitMapper.findById(id);
        if (benefit == null) {
            log.warn("权益不存在: benefitId={}", id);
            throw new BusinessException("权益不存在");
        }
        return benefit;
    }

    @Override
    @Transactional
    public Long claimBenefit(Long userId, ClaimBenefitRequest request) {
        log.info("用户领取权益: userId={}, benefitId={}, phone={}", 
                userId, request.getBenefitId(), request.getPhone());
        
        // 验证权益存在且可用
        Benefit benefit = benefitMapper.findById(request.getBenefitId());
        if (benefit == null) {
            log.error("权益不存在: benefitId={}", request.getBenefitId());
            throw new BusinessException("权益不存在");
        }
        if (benefit.getStatus() != 1) {
            log.error("权益已下架: benefitId={}", request.getBenefitId());
            throw new BusinessException("该权益已下架");
        }
        
        // 验证库存
        if (benefit.getStock() != -1 && benefit.getStock() <= 0) {
            log.error("权益库存不足: benefitId={}, stock={}", request.getBenefitId(), benefit.getStock());
            throw new BusinessException("权益库存不足");
        }
        
        // 验证验证码
        if (!smsService.verifyCode(request.getPhone(), request.getVerificationCode())) {
            log.error("验证码错误: phone={}", request.getPhone());
            throw new BusinessException("验证码错误或已过期");
        }
        
        // 创建领取记录
        UserBenefit userBenefit = UserBenefit.builder()
                .userId(userId)
                .benefitId(request.getBenefitId())
                .benefitName(benefit.getName())
                .benefitImage(benefit.getImage())
                .quantity(1)
                .city(request.getCity())
                .licensePlate(request.getLicensePlate())
                .phone(request.getPhone())
                .verificationCode(request.getVerificationCode())
                .status(BenefitStatus.UNUSED.getCode())
                .build();
        
        userBenefitMapper.insert(userBenefit);
        
        // 扣减库存（如果不是无限库存）
        if (benefit.getStock() != -1) {
            int affected = benefitMapper.decreaseStock(benefit.getId(), 1);
            if (affected == 0) {
                log.error("扣减库存失败，可能库存不足: benefitId={}", benefit.getId());
                throw new BusinessException("权益库存不足");
            }
        }
        
        log.info("权益领取成功: userBenefitId={}", userBenefit.getId());
        return userBenefit.getId();
    }

    @Override
    public List<UserBenefit> getMyBenefits(Long userId) {
        log.debug("获取用户权益领取记录: userId={}", userId);
        return userBenefitMapper.findByUserId(userId);
    }

    @Override
    @Transactional
    public void useBenefit(Long id, Long userId) {
        log.info("使用权益: userBenefitId={}, userId={}", id, userId);
        
        UserBenefit userBenefit = userBenefitMapper.findById(id);
        if (userBenefit == null) {
            log.error("权益领取记录不存在: userBenefitId={}", id);
            throw new BusinessException("权益领取记录不存在");
        }
        
        // 验证是否是本人的权益
        if (!userBenefit.getUserId().equals(userId)) {
            log.error("无权操作他人的权益: userBenefitId={}, userId={}, ownerId={}", 
                    id, userId, userBenefit.getUserId());
            throw new BusinessException("无权操作");
        }
        
        // 验证状态
        if (BenefitStatus.USED.getCode().equals(userBenefit.getStatus())) {
            log.error("权益已使用: userBenefitId={}", id);
            throw new BusinessException("该权益已使用");
        }
        
        userBenefit.setStatus(BenefitStatus.USED.getCode());
        userBenefit.setUseTime(LocalDateTime.now());
        userBenefitMapper.updateStatus(userBenefit);
        
        log.info("权益使用成功: userBenefitId={}", id);
    }

    @Override
    public PageResult<Benefit> getBenefitPage(String name, String type, Integer status,
                                              Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum <= 0) pageNum = 1;
        if (pageSize == null || pageSize <= 0) pageSize = 10;
        
        log.debug("分页查询权益列表: name={}, type={}, status={}, pageNum={}, pageSize={}", 
                name, type, status, pageNum, pageSize);
        
        int offset = (pageNum - 1) * pageSize;
        List<Benefit> list = benefitMapper.findByPage(name, type, status, offset, pageSize);
        long total = benefitMapper.countByPage(name, type, status);
        
        return new PageResult<>(list, total, pageNum, pageSize);
    }

    @Override
    @Transactional
    public Long createBenefit(BenefitRequest request) {
        log.info("创建权益: name={}, code={}, type={}", 
                request.getName(), request.getCode(), request.getType());
        
        // 验证编码唯一性
        Benefit existing = benefitMapper.findByCode(request.getCode());
        if (existing != null) {
            log.error("权益编码已存在: code={}", request.getCode());
            throw new BusinessException("权益编码已存在");
        }
        
        Benefit benefit = Benefit.builder()
                .name(request.getName())
                .code(request.getCode())
                .type(request.getType())
                .image(request.getImage())
                .description(request.getDescription())
                .stock(request.getStock() != null ? request.getStock() : 0)
                .status(request.getStatus() != null ? request.getStatus() : 1)
                .build();
        
        benefitMapper.insert(benefit);
        log.info("权益创建成功: benefitId={}", benefit.getId());
        return benefit.getId();
    }

    @Override
    @Transactional
    public void updateBenefit(Long id, BenefitRequest request) {
        log.info("更新权益: benefitId={}", id);
        
        Benefit benefit = benefitMapper.findById(id);
        if (benefit == null) {
            log.error("权益不存在: benefitId={}", id);
            throw new BusinessException("权益不存在");
        }
        
        // 只更新非空字段
        if (request.getName() != null) {
            benefit.setName(request.getName());
        }
        if (request.getCode() != null) {
            benefit.setCode(request.getCode());
        }
        if (request.getType() != null) {
            benefit.setType(request.getType());
        }
        if (request.getImage() != null) {
            benefit.setImage(request.getImage());
        }
        if (request.getDescription() != null) {
            benefit.setDescription(request.getDescription());
        }
        if (request.getStock() != null) {
            benefit.setStock(request.getStock());
        }
        // status 字段通过专门的 updateBenefitStatus 方法更新，这里不处理
        
        benefitMapper.update(benefit);
        log.info("权益更新成功: benefitId={}", id);
    }

    @Override
    @Transactional
    public void deleteBenefit(Long id) {
        log.info("删除权益: benefitId={}", id);
        
        Benefit benefit = benefitMapper.findById(id);
        if (benefit == null) {
            log.error("权益不存在: benefitId={}", id);
            throw new BusinessException("权益不存在");
        }
        
        benefitMapper.deleteById(id);
        log.info("权益删除成功: benefitId={}", id);
    }

    @Override
    public PageResult<UserBenefit> getUserBenefitPage(Long userId, Long benefitId, String phone,
                                                      String status, Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum <= 0) pageNum = 1;
        if (pageSize == null || pageSize <= 0) pageSize = 10;
        
        log.debug("分页查询用户权益领取记录: userId={}, benefitId={}, pageNum={}, pageSize={}", 
                userId, benefitId, pageNum, pageSize);
        
        int offset = (pageNum - 1) * pageSize;
        List<UserBenefit> list = userBenefitMapper.findByPage(
                userId, benefitId, phone, status, offset, pageSize);
        long total = userBenefitMapper.countByPage(
                userId, benefitId, phone, status);
        
        return new PageResult<>(list, total, pageNum, pageSize);
    }
}

