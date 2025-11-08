package org.demo.car.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 活动请求
 */
@Data
public class ActivityRequest {
    @NotBlank(message = "活动名称不能为空")
    private String name;
    
    @NotBlank(message = "活动类型不能为空")
    private String type;
    
    private String image;
    private String linkUrl;
    private Long vehicleId;
    private String content;
    private Integer status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer sortOrder;
}

