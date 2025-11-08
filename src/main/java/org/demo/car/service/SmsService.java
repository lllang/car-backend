package org.demo.car.service;

/**
 * 短信服务接口
 */
public interface SmsService {
    
    /**
     * 发送验证码
     * @param phone 手机号
     * @param code 验证码
     */
    void sendCode(String phone, String code);
    
    /**
     * 验证验证码
     * @param phone 手机号
     * @param code 验证码
     * @return 是否验证成功
     */
    boolean verifyCode(String phone, String code);
    
    /**
     * 生成验证码
     * @return 6位数字验证码
     */
    String generateCode();
}

