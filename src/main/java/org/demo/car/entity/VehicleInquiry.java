package org.demo.car.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 新车询价实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleInquiry {
    private Long id;
    private Long userId;
    private Long vehicleId;
    private Long brandId;
    private String phone;
    private Integer needExchange;
    private String dealerName;
    private String status;
    private String remark;
    private Long handlerId;
    private String handlerName;
    private LocalDateTime handleTime;
    // 车辆信息冗余字段
    private String brandName;
    private String vehicleModel;
    private String vehiclePrice;
    private String vehicleImage;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

