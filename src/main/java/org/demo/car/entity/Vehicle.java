package org.demo.car.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 车辆实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    private Long id;
    private Long brandId;
    private String brandName; // 品牌名称（关联查询结果）
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
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

