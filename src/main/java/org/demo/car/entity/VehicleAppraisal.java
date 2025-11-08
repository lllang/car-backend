package org.demo.car.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 旧车估价实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleAppraisal {
    private Long id;
    private Long userId;
    private String phone;
    private String oldBrand;
    private String intentionBrand;
    private String purchaseYear;
    private String region;
    private String status;
    private Long followerId;
    private String followerName;
    private LocalDateTime followTime;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

