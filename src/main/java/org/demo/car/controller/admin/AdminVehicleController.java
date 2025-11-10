package org.demo.car.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.result.PageResult;
import org.demo.car.common.result.Result;
import org.demo.car.dto.request.VehicleRequest;
import org.demo.car.entity.Vehicle;
import org.demo.car.service.VehicleService;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端车辆控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/vehicle")
@RequiredArgsConstructor
public class AdminVehicleController {

    private final VehicleService vehicleService;

    /**
     * 分页查询车辆列表
     */
    @GetMapping("/list")
    public Result<PageResult<Vehicle>> getVehiclePage(
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        log.debug("分页查询车辆列表: brandId={}, model={}, status={}", brandId, model, status);
        PageResult<Vehicle> result = vehicleService.getVehiclePage(
                brandId, model, status, pageNum, pageSize);
        return Result.success(result);
    }

    /**
     * 获取车辆详情
     */
    @GetMapping("/{id}")
    public Result<Vehicle> getVehicleDetail(@PathVariable Long id) {
        log.debug("获取车辆详情: vehicleId={}", id);
        Vehicle vehicle = vehicleService.getVehicleById(id);
        return Result.success(vehicle);
    }

    /**
     * 创建车辆
     */
    @PostMapping
    @org.demo.car.security.RequirePermission("vehicle:create")
    public Result<Long> createVehicle(@Valid @RequestBody VehicleRequest request) {
        log.info("创建车辆: brandId={}, model={}", request.getBrandId(), request.getModel());
        
        Vehicle vehicle = Vehicle.builder()
                .brandId(request.getBrandId())
                .model(request.getModel())
                .guidePrice(request.getGuidePrice())
                .batteryCapacity(request.getBatteryCapacity())
                .endurance(request.getEndurance())
                .fastCharge(request.getFastCharge())
                .slowCharge(request.getSlowCharge())
                .mainImage(request.getMainImage())
                .status(request.getStatus() != null ? request.getStatus() : 1)
                .isFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : 0)
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                .build();
        
        Long id = vehicleService.createVehicle(vehicle);
        return Result.success(id);
    }

    /**
     * 更新车辆
     */
    @PutMapping("/{id}")
    @org.demo.car.security.RequirePermission("vehicle:update")
    public Result<Void> updateVehicle(@PathVariable Long id, 
                                      @Valid @RequestBody VehicleRequest request) {
        log.info("更新车辆: vehicleId={}", id);
        
        Vehicle vehicle = Vehicle.builder()
                .id(id)
                .brandId(request.getBrandId())
                .model(request.getModel())
                .guidePrice(request.getGuidePrice())
                .batteryCapacity(request.getBatteryCapacity())
                .endurance(request.getEndurance())
                .fastCharge(request.getFastCharge())
                .slowCharge(request.getSlowCharge())
                .mainImage(request.getMainImage())
                .status(request.getStatus())
                .isFeatured(request.getIsFeatured())
                .sortOrder(request.getSortOrder())
                .build();
        
        vehicleService.updateVehicle(vehicle);
        return Result.success();
    }

    /**
     * 删除车辆
     */
    @DeleteMapping("/{id}")
    @org.demo.car.security.RequirePermission("vehicle:delete")
    public Result<Void> deleteVehicle(@PathVariable Long id) {
        log.info("删除车辆: vehicleId={}", id);
        vehicleService.deleteVehicle(id);
        return Result.success();
    }

    /**
     * 上下架车辆
     */
    @PutMapping("/{id}/status")
    public Result<Void> updateVehicleStatus(@PathVariable Long id, 
                                            @RequestParam Integer status) {
        log.info("更新车辆状态: vehicleId={}, status={}", id, status);
        vehicleService.updateVehicleStatus(id, status);
        return Result.success();
    }
    
    /**
     * 设置精选
     */
    @PutMapping("/{id}/featured")
    @org.demo.car.security.RequirePermission("vehicle:update")
    public Result<Void> updateVehicleFeatured(@PathVariable Long id,
                                              @RequestBody java.util.Map<String, Object> payload) {
        Boolean featured = (Boolean) payload.get("featured");
        Integer isFeatured = (featured != null && featured) ? 1 : 0;
        log.info("更新车辆精选状态: vehicleId={}, isFeatured={}", id, isFeatured);
        vehicleService.updateVehicleFeatured(id, isFeatured);
        return Result.success();
    }
    
    /**
     * 获取车辆图片列表
     */
    @GetMapping("/{id}/images")
    @org.demo.car.security.RequirePermission("vehicle:list")
    public Result<java.util.List<org.demo.car.entity.VehicleImage>> getVehicleImages(@PathVariable Long id) {
        log.info("获取车辆图片: vehicleId={}", id);
        java.util.List<org.demo.car.entity.VehicleImage> images = vehicleService.getVehicleImages(id);
        return Result.success(images);
    }
    
    /**
     * 保存车辆图片
     */
    @PostMapping("/{id}/images")
    @org.demo.car.security.RequirePermission("vehicle:update")
    public Result<Void> saveVehicleImages(@PathVariable Long id,
                                          @RequestBody java.util.List<String> imageUrls) {
        log.info("保存车辆图片: vehicleId={}, imageCount={}", id, imageUrls.size());
        vehicleService.saveVehicleImages(id, imageUrls);
        return Result.success();
    }
    
    /**
     * 删除车辆图片
     */
    @DeleteMapping("/{vehicleId}/images/{imageId}")
    @org.demo.car.security.RequirePermission("vehicle:update")
    public Result<Void> deleteVehicleImage(@PathVariable Long vehicleId,
                                           @PathVariable Long imageId) {
        log.info("删除车辆图片: vehicleId={}, imageId={}", vehicleId, imageId);
        vehicleService.deleteVehicleImage(imageId);
        return Result.success();
    }
}

