package org.demo.car.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户权益领取记录实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBenefit {
    private Long id;
    private Long userId;
    private Long benefitId;
    private String benefitName;
    private String benefitImage;
    private Integer quantity;
    private String city;
    private String licensePlate;
    private String phone;
    private String verificationCode;
    private String status;
    private LocalDateTime useTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

