package org.demo.car.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 提交估价请求
 */
@Data
public class AppraisalRequest {
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    @NotBlank(message = "旧车品牌不能为空")
    private String oldBrand;
    
    @NotBlank(message = "意向品牌不能为空")
    private String intentionBrand;
    
    @NotBlank(message = "购入年份不能为空")
    private String purchaseYear;
    
    @NotBlank(message = "所在地区不能为空")
    private String region;
}

