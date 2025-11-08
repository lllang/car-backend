package org.demo.car.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 角色实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private Long id;
    private String name;
    private String code;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

