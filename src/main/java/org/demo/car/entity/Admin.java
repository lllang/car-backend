package org.demo.car.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 管理员实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private Long roleId;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

