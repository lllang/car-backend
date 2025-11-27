package org.demo.car.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.result.Result;
import org.demo.car.dto.response.VehicleResponse;
import org.demo.car.entity.Vehicle;
import org.demo.car.security.SecurityUtils;
import org.demo.car.service.FavoriteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * C端收藏控制器
 */
@Slf4j
@RestController
@RequestMapping("/user/favorite")
@RequiredArgsConstructor
public class UserFavoriteController {

    private final FavoriteService favoriteService;

    /**
     * 添加收藏
     */
    @PostMapping("/{vehicleId}")
    public Result<Void> addFavorite(@PathVariable Long vehicleId) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("添加收藏: userId={}, vehicleId={}", userId, vehicleId);
        favoriteService.addFavorite(userId, vehicleId);
        return Result.success();
    }

    /**
     * 取消收藏
     */
    @DeleteMapping("/{vehicleId}")
    public Result<Void> removeFavorite(@PathVariable Long vehicleId) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("取消收藏: userId={}, vehicleId={}", userId, vehicleId);
        favoriteService.removeFavorite(userId, vehicleId);
        return Result.success();
    }

    /**
     * 获取我的收藏列表
     */
    @GetMapping("/list")
    public Result<List<VehicleResponse>> getMyFavorites() {
        Long userId = SecurityUtils.getCurrentUserId();
        log.debug("获取我的收藏: userId={}", userId);
        List<Vehicle> vehicles = favoriteService.getMyFavorites(userId);
        List<VehicleResponse> responses = vehicles.stream()
                .map(vehicle -> {
                    VehicleResponse response = VehicleResponse.from(vehicle);
                    response.setIsFavorite(true);  // 收藏列表中的车辆都是已收藏的
                    return response;
                })
                .collect(Collectors.toList());
        return Result.success(responses);
    }

    /**
     * 检查是否已收藏
     */
    @GetMapping("/check/{vehicleId}")
    public Result<Boolean> checkFavorite(@PathVariable Long vehicleId) {
        Long userId = SecurityUtils.getCurrentUserId();
        boolean isFavorite = favoriteService.isFavorite(userId, vehicleId);
        return Result.success(isFavorite);
    }
}

