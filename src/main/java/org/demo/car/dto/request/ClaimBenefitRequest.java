package org.demo.car.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * 领取权益请求
 */
@Data
public class ClaimBenefitRequest {
    @NotNull(message = "权益ID不能为空")
    private Long benefitId;
    
    @NotBlank(message = "行驶城市不能为空")
    private String city;
    
    @NotBlank(message = "车牌号不能为空")
    private String licensePlate;
    
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    @NotBlank(message = "验证码不能为空")
    private String verificationCode;
}

