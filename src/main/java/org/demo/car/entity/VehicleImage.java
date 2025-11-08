package org.demo.car.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 车辆图片实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleImage {
    private Long id;
    private Long vehicleId;
    private String imageUrl;
    private Integer sortOrder;
    private LocalDateTime createTime;
}

