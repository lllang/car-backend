package org.demo.car.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.exception.BusinessException;
import org.demo.car.dto.request.BindPhoneRequest;
import org.demo.car.dto.request.UpdateUserInfoRequest;
import org.demo.car.dto.request.UserLoginRequest;
import org.demo.car.entity.User;
import org.demo.car.mapper.UserMapper;
import org.demo.car.service.SmsService;
import org.demo.car.service.UserService;
import org.demo.car.service.WechatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final WechatService wechatService;
    private final SmsService smsService;

    @Override
    @Transactional
    public User login(UserLoginRequest request) {
        String openid;
        
        // 通过code换取openid或直接使用openid
        if (request.getCode() != null && !request.getCode().isEmpty()) {
            openid = wechatService.getOpenidByCode(request.getCode());
        } else if (request.getOpenid() != null && !request.getOpenid().isEmpty()) {
            openid = request.getOpenid();
        } else {
            throw new BusinessException("登录失败：缺少code或openid");
        }
        
        // 查询用户是否存在
        User user = userMapper.findByOpenid(openid);
        
        // 如果不存在则创建
        if (user == null) {
            // 获取微信用户信息
            WechatService.WechatUserInfo wechatUserInfo = wechatService.getUserInfo(openid);
            
            user = User.builder()
                .openid(openid)
                .nickname(wechatUserInfo.getNickname())
                .avatar(wechatUserInfo.getAvatar())
                .gender(0)
                .build();
            
            userMapper.insert(user);
            log.info("创建新用户: {}", openid);
        }
        
        return user;
    }

    @Override
    @Transactional
    public void bindPhone(Long userId, BindPhoneRequest request) {
        // 验证验证码
        if (!smsService.verifyCode(request.getPhone(), request.getCode())) {
            throw new BusinessException("验证码错误或已过期");
        }
        
        // 更新用户手机号
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        user.setPhone(request.getPhone());
        userMapper.update(user);
        
        log.info("用户绑定手机号成功: userId={}, phone={}", userId, request.getPhone());
    }

    @Override
    public User getUserById(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user;
    }

    @Override
    @Transactional
    public void updateUserInfo(Long userId, UpdateUserInfoRequest request) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }
        if (request.getAge() != null) {
            user.setAge(request.getAge());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        
        userMapper.update(user);
        log.info("更新用户信息成功: userId={}", userId);
    }

    @Override
    public User getUserByOpenid(String openid) {
        return userMapper.findByOpenid(openid);
    }
}

