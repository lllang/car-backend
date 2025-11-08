package org.demo.car.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * C端用户实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String openid;
    private String nickname;
    private String avatar;
    private String phone;
    private Integer age;
    private Integer gender;
    private String address;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

