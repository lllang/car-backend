package org.demo.car.dto.request;

import lombok.Data;

/**
 * C端用户登录请求
 */
@Data
public class UserLoginRequest {
    private String code;  // 微信授权码
    private String openid;  // 直接使用openid登录
}

