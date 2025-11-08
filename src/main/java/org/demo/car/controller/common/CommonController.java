package org.demo.car.controller.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.result.Result;
import org.demo.car.service.FileStorageService;
import org.demo.car.service.SmsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 公共接口控制器
 */
@Slf4j
@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {

    private final FileStorageService fileStorageService;
    private final SmsService smsService;

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file,
                                               @RequestParam(defaultValue = "images/") String path) {
        String fileUrl = fileStorageService.upload(file, path);
        String accessUrl = fileStorageService.getAccessUrl(fileUrl);
        
        Map<String, String> data = new HashMap<>();
        data.put("fileUrl", fileUrl);
        data.put("accessUrl", accessUrl);
        
        log.info("文件上传成功: {}", fileUrl);
        return Result.success(data);
    }

    /**
     * 发送短信验证码
     */
    @PostMapping("/sms/send")
    public Result<Void> sendSms(@RequestParam String phone) {
        String code = smsService.generateCode();
        smsService.sendCode(phone, code);
        log.info("发送验证码成功: phone={}", phone);
        return Result.success();
    }

    /**
     * 验证短信验证码
     */
    @PostMapping("/sms/verify")
    public Result<Boolean> verifySms(@RequestParam String phone, @RequestParam String code) {
        boolean verified = smsService.verifyCode(phone, code);
        return Result.success(verified);
    }

    /**
     * 获取公共配置
     */
    @GetMapping("/config")
    public Result<Map<String, Object>> getConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("privacyPolicyUrl", "https://example.com/privacy-policy.html");
        config.put("userAgreementUrl", "https://example.com/user-agreement.html");
        return Result.success(config);
    }
}

