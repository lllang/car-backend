package org.demo.car.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 权益请求
 */
@Data
public class BenefitRequest {
    @NotBlank(message = "权益名称不能为空")
    private String name;
    
    @NotBlank(message = "权益编码不能为空")
    private String code;
    
    @NotBlank(message = "权益类型不能为空")
    private String type;
    
    private String image;
    private String description;
    private Integer stock;
    private Integer status;
}

