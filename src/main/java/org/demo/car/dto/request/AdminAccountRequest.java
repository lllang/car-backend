package org.demo.car.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 管理员账号请求DTO
 */
@Data
public class AdminAccountRequest {
    
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    private String password;
    
    @NotBlank(message = "真实姓名不能为空")
    private String realName;
    
    private String phone;
    
    @NotNull(message = "角色不能为空")
    private Long roleId;
    
    private Integer status;
}

