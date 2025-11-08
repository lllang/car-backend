package org.demo.car.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 权限实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private Long id;
    private String name;
    private String code;
    private String type;
    private Long parentId;
    private String path;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

