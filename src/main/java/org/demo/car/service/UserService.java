package org.demo.car.service;

import org.demo.car.dto.request.BindPhoneRequest;
import org.demo.car.dto.request.UpdateUserInfoRequest;
import org.demo.car.dto.request.UserLoginRequest;
import org.demo.car.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户登录
     */
    User login(UserLoginRequest request);
    
    /**
     * 绑定手机号
     */
    void bindPhone(Long userId, BindPhoneRequest request);
    
    /**
     * 获取用户信息
     */
    User getUserById(Long userId);
    
    /**
     * 更新用户信息
     */
    void updateUserInfo(Long userId, UpdateUserInfoRequest request);
    
    /**
     * 通过openid获取用户
     */
    User getUserByOpenid(String openid);
}

