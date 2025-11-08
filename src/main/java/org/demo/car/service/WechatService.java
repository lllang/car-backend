package org.demo.car.service;

/**
 * 微信服务接口
 */
public interface WechatService {
    
    /**
     * 通过code换取openid
     * @param code 微信授权码
     * @return openid
     */
    String getOpenidByCode(String code);
    
    /**
     * 获取用户信息
     * @param openid 用户openid
     * @return 用户信息（昵称、头像等）
     */
    WechatUserInfo getUserInfo(String openid);
    
    /**
     * 微信用户信息
     */
    class WechatUserInfo {
        private String openid;
        private String nickname;
        private String avatar;
        
        public WechatUserInfo() {}
        
        public WechatUserInfo(String openid, String nickname, String avatar) {
            this.openid = openid;
            this.nickname = nickname;
            this.avatar = avatar;
        }
        
        public String getOpenid() { return openid; }
        public void setOpenid(String openid) { this.openid = openid; }
        
        public String getNickname() { return nickname; }
        public void setNickname(String nickname) { this.nickname = nickname; }
        
        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }
    }
}

