package org.demo.car.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.exception.BusinessException;
import org.demo.car.entity.UserFavorite;
import org.demo.car.entity.Vehicle;
import org.demo.car.mapper.UserFavoriteMapper;
import org.demo.car.mapper.VehicleMapper;
import org.demo.car.service.FavoriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 收藏服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final UserFavoriteMapper favoriteMapper;
    private final VehicleMapper vehicleMapper;

    @Override
    @Transactional
    public void addFavorite(Long userId, Long vehicleId) {
        log.info("添加收藏: userId={}, vehicleId={}", userId, vehicleId);
        
        // 验证车辆存在
        Vehicle vehicle = vehicleMapper.findById(vehicleId);
        if (vehicle == null) {
            log.error("车辆不存在: vehicleId={}", vehicleId);
            throw new BusinessException("车辆不存在");
        }
        
        // 检查是否已收藏
        UserFavorite existing = favoriteMapper.findByUserIdAndVehicleId(userId, vehicleId);
        if (existing != null) {
            log.warn("已经收藏过该车辆: userId={}, vehicleId={}", userId, vehicleId);
            throw new BusinessException("已经收藏过该车辆");
        }
        
        UserFavorite favorite = UserFavorite.builder()
                .userId(userId)
                .vehicleId(vehicleId)
                .build();
        
        favoriteMapper.insert(favorite);
        log.info("收藏成功: favoriteId={}", favorite.getId());
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, Long vehicleId) {
        log.info("取消收藏: userId={}, vehicleId={}", userId, vehicleId);
        
        UserFavorite favorite = favoriteMapper.findByUserIdAndVehicleId(userId, vehicleId);
        if (favorite == null) {
            log.warn("未收藏该车辆: userId={}, vehicleId={}", userId, vehicleId);
            throw new BusinessException("未收藏该车辆");
        }
        
        favoriteMapper.deleteByUserIdAndVehicleId(userId, vehicleId);
        log.info("取消收藏成功: userId={}, vehicleId={}", userId, vehicleId);
    }

    @Override
    public List<Vehicle> getMyFavorites(Long userId) {
        log.debug("获取用户收藏列表: userId={}", userId);
        return favoriteMapper.findFavoriteVehiclesByUserId(userId);
    }

    @Override
    public boolean isFavorite(Long userId, Long vehicleId) {
        UserFavorite favorite = favoriteMapper.findByUserIdAndVehicleId(userId, vehicleId);
        return favorite != null;
    }
}

