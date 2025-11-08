package org.demo.car.dto.response;

import lombok.Data;
import org.demo.car.entity.User;

/**
 * 用户响应
 */
@Data
public class UserResponse {
    private Long id;
    private String openid;
    private String nickname;
    private String avatar;
    private String phone;
    private Integer age;
    private Integer gender;
    private String address;
    
    public static UserResponse from(User user) {
        if (user == null) {
            return null;
        }
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setOpenid(user.getOpenid());
        response.setNickname(user.getNickname());
        response.setAvatar(user.getAvatar());
        response.setPhone(user.getPhone());
        response.setAge(user.getAge());
        response.setGender(user.getGender());
        response.setAddress(user.getAddress());
        return response;
    }
}

