package org.demo.car.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * 提交询价请求
 */
@Data
public class InquiryRequest {
    @NotNull(message = "车辆ID不能为空")
    private Long vehicleId;
    
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    private Integer needExchange;  // 0否 1是
}

