package org.demo.car.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 车辆请求
 */
@Data
public class VehicleRequest {
    @NotNull(message = "品牌ID不能为空")
    private Long brandId;
    
    @NotBlank(message = "车型不能为空")
    private String model;
    
    private String guidePrice;
    private String batteryCapacity;
    private String endurance;
    private String fastCharge;
    private String slowCharge;
    private String mainImage;
    private Integer status;
    private Integer isFeatured;
    private Integer sortOrder;
}

