package org.demo.car.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.exception.BusinessException;
import org.demo.car.service.WechatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 微信服务实现
 */
@Slf4j
@Service
public class WechatServiceImpl implements WechatService {

    @Value("${wechat.appid}")
    private String appId;

    @Value("${wechat.secret}")
    private String appSecret;

    @Override
    public String getOpenidByCode(String code) {
        // TODO: 实现微信code换取openid的逻辑
        // 调用微信API: https://api.weixin.qq.com/sns/oauth2/access_token
        
        log.info("通过code获取openid: {}", code);
        
        // Mock实现，返回一个测试openid
        if (code != null && !code.isEmpty()) {
            return "mock_openid_" + code;
        }
        
        throw new BusinessException("获取openid失败");
    }

    @Override
    public WechatUserInfo getUserInfo(String openid) {
        // TODO: 实现获取微信用户信息的逻辑
        // 调用微信API: https://api.weixin.qq.com/sns/userinfo
        
        log.info("获取微信用户信息: {}", openid);
        
        // Mock实现，返回测试数据
        return new WechatUserInfo(
            openid,
            "微信用户_" + openid.substring(0, 8),
            "https://example.com/avatar.png"
        );
    }
}

