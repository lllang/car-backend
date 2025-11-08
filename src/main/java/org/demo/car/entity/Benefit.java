package org.demo.car.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 权益实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Benefit {
    private Long id;
    private String name;
    private String code;
    private String type;
    private String image;
    private String description;
    private Integer stock;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

