package org.demo.car.dto.request;

import lombok.Data;

/**
 * 更新用户信息请求
 */
@Data
public class UpdateUserInfoRequest {
    private String nickname;
    private String avatar;
    private Integer age;
    private Integer gender;
    private String address;
}

