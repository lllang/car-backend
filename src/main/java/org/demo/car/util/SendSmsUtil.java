package org.demo.car.util;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: SendSmsUtil
 * @Description: 阿里云短信工具类
 * @author: TracyYang
 * @date: 2024年7月22日 上午10:15:40
 */
@Slf4j
public class SendSmsUtil {

    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     * @param code 验证码
     * @param accessKeyId 阿里云AccessKeyId
     * @param accessKeySecret 阿里云AccessKeySecret
     * @param signName 短信签名
     * @param templateCode 短信模板编码
     * @return 执行结果
     */
    public static boolean sendSMS(String phone, String code, String accessKeyId, 
                                   String accessKeySecret, String signName, String templateCode) {
        try {
            // 配置阿里云客户端
            Config config = new Config()
                    .setAccessKeyId(accessKeyId)
                    .setAccessKeySecret(accessKeySecret)
                    .setEndpoint("dysmsapi.aliyuncs.com");
            
            Client client = new Client(config);
            
            // 构建短信请求
            SendSmsRequest request = new SendSmsRequest()
                    .setPhoneNumbers(phone)
                    .setSignName(signName)
                    .setTemplateCode(templateCode)
                    .setTemplateParam("{\"code\":\"" + code + "\"}");
            
            // 发送短信
            SendSmsResponse response = client.sendSms(request);
            
            if (response.getBody().getCode() != null && 
                response.getBody().getCode().equals("OK")) {
                log.info("短信发送成功: phone={}, code={}", phone, response.getBody().getCode());
                return true;
            } else {
                log.error("短信发送失败: phone={}, code={}, message={}", 
                         phone, response.getBody().getCode(), response.getBody().getMessage());
                return false;
            }
        } catch (Exception e) {
            log.error("短信发送异常: phone=" + phone, e);
            return false;
        }
    }
}

