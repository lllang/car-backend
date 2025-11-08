package org.demo.car.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.demo.car.service.SmsService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mock短信服务实现（用于开发测试）
 */
@Slf4j
@Service
public class MockSmsServiceImpl implements SmsService {

    // 存储验证码，key为手机号，value为验证码
    private final Map<String, String> codeCache = new ConcurrentHashMap<>();
    
    // 验证码有效期（毫秒）
    private static final long CODE_EXPIRE_TIME = 5 * 60 * 1000; // 5分钟
    
    // 存储验证码发送时间
    private final Map<String, Long> codeTimeCache = new ConcurrentHashMap<>();

    @Override
    public void sendCode(String phone, String code) {
        // Mock实现，仅打印日志
        log.info("========================================");
        log.info("【短信验证码】发送到手机号: {}", phone);
        log.info("【验证码】: {}", code);
        log.info("========================================");
        
        // 存储验证码和时间
        codeCache.put(phone, code);
        codeTimeCache.put(phone, System.currentTimeMillis());
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
            log.warn("验证码不存在或已过期: {}", phone);
            return false;
        }
        
        // 检查是否过期
        if (System.currentTimeMillis() - sendTime > CODE_EXPIRE_TIME) {
            log.warn("验证码已过期: {}", phone);
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
            log.info("验证码验证成功: {}", phone);
        } else {
            log.warn("验证码不匹配: {}", phone);
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

