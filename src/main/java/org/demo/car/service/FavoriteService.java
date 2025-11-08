package org.demo.car.service;

import org.demo.car.entity.Vehicle;

import java.util.List;

/**
 * 收藏服务接口
 */
public interface FavoriteService {
    
    /**
     * 添加收藏
     */
    void addFavorite(Long userId, Long vehicleId);
    
    /**
     * 取消收藏
     */
    void removeFavorite(Long userId, Long vehicleId);
    
    /**
     * 获取我的收藏列表
     */
    List<Vehicle> getMyFavorites(Long userId);
    
    /**
     * 判断是否已收藏
     */
    boolean isFavorite(Long userId, Long vehicleId);
}

