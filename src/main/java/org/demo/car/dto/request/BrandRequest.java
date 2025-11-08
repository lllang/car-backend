package org.demo.car.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 品牌请求
 */
@Data
public class BrandRequest {
    @NotBlank(message = "品牌名称不能为空")
    private String name;
    
    private String logo;
    private String dealerName;
    private String dealerAddress;
    private String dealerContactName;
    private String dealerContactPhone;
    private Integer status;
}

