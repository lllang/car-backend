package org.demo.car.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 品牌实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Brand {
    private Long id;
    private String name;
    private String logo;
    private String dealerName;
    private String dealerAddress;
    private String dealerContactName;
    private String dealerContactPhone;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

