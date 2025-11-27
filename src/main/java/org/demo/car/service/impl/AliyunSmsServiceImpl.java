package org.demo.car.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.exception.BusinessException;
import org.demo.car.service.SmsService;
import org.demo.car.util.SendSmsUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 阿里云短信服务实现
 */
@Slf4j
@Service
// @Primary  // 设置为主要实现，替代 MockSmsServiceImpl（开发环境暂时注释，使用Mock服务）
public class AliyunSmsServiceImpl implements SmsService {

    @Value("${aliyun.sms.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.sms.access-key-secret}")
    private String accessKeySecret;

    @Value("${aliyun.sms.sign-name}")
    private String signName;

    @Value("${aliyun.sms.template-code}")
    private String templateCode;

    // 存储验证码，key为手机号，value为验证码
    private final Map<String, String> codeCache = new ConcurrentHashMap<>();
    
    // 验证码有效期（毫秒）
    private static final long CODE_EXPIRE_TIME = 5 * 60 * 1000; // 5分钟
    
    // 存储验证码发送时间
    private final Map<String, Long> codeTimeCache = new ConcurrentHashMap<>();
    
    // 存储上次发送时间，用于防止频繁发送
    private final Map<String, Long> sendTimeCache = new ConcurrentHashMap<>();
    
    // 发送间隔限制（毫秒）
    private static final long SEND_INTERVAL = 60 * 1000; // 1分钟

    @Override
    public void sendCode(String phone, String code) {
        // 验证手机号
        if (phone == null || phone.trim().isEmpty()) {
            throw new BusinessException("手机号不能为空");
        }

        // 检查发送频率
        Long lastSendTime = sendTimeCache.get(phone);
        if (lastSendTime != null && System.currentTimeMillis() - lastSendTime < SEND_INTERVAL) {
            long waitTime = (SEND_INTERVAL - (System.currentTimeMillis() - lastSendTime)) / 1000;
            throw new BusinessException("发送过于频繁，请" + waitTime + "秒后再试");
        }

        try {
            // 调用阿里云短信工具发送
            boolean success = SendSmsUtil.sendSMS(phone, code, accessKeyId, 
                                                   accessKeySecret, signName, templateCode);
            
            if (success) {
                // 存储验证码和时间
                codeCache.put(phone, code);
                codeTimeCache.put(phone, System.currentTimeMillis());
                sendTimeCache.put(phone, System.currentTimeMillis());
                
                log.info("验证码发送成功: phone={}", phone);
            } else {
                throw new BusinessException("短信发送失败，请稍后重试");
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("发送短信验证码异常: phone=" + phone, e);
            throw new BusinessException("短信发送失败，请稍后重试");
        }
    }

    @Override
    public boolean verifyCode(String phone, String code) {
        if (phone == null || code == null) {
            return false;
        }
        
        // 获取缓存的验证码
        String cachedCode = codeCache.get(phone);
        Long sendTime = codeTimeCache.get(phone);
        
        if (cachedCode == null || sendTime == null) {
            log.warn("验证码不存在或已过期: phone={}", phone);
            return false;
        }
        
        // 检查是否过期
        if (System.currentTimeMillis() - sendTime > CODE_EXPIRE_TIME) {
            log.warn("验证码已过期: phone={}", phone);
            codeCache.remove(phone);
            codeTimeCache.remove(phone);
            return false;
        }
        
        // 验证码匹配
        boolean matches = cachedCode.equals(code);
        if (matches) {
            // 验证成功后删除
            codeCache.remove(phone);
            codeTimeCache.remove(phone);
            log.info("验证码验证成功: phone={}", phone);
        } else {
            log.warn("验证码不匹配: phone={}", phone);
        }
        
        return matches;
    }

    @Override
    public String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 生成6位数字
        return String.valueOf(code);
    }
}

