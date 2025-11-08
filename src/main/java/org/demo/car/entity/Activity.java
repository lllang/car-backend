package org.demo.car.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 活动实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Activity {
    private Long id;
    private String name;
    private String type;
    private String image;
    private String linkUrl;
    private Long vehicleId;
    private String content;
    private Integer status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer sortOrder;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

