package org.demo.car.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户喜欢实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFavorite {
    private Long id;
    private Long userId;
    private Long vehicleId;
    private LocalDateTime createTime;
}

