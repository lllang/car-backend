package org.demo.car.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.result.PageResult;
import org.demo.car.common.result.Result;
import org.demo.car.dto.response.VehicleResponse;
import org.demo.car.entity.Vehicle;
import org.demo.car.entity.VehicleImage;
import org.demo.car.service.VehicleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * C端车辆控制器
 */
@Slf4j
@RestController
@RequestMapping("/user/vehicle")
@RequiredArgsConstructor
public class UserVehicleController {

    private final VehicleService vehicleService;

    /**
     * 获取精选现车
     */
    @GetMapping("/featured")
    public Result<List<VehicleResponse>> getFeaturedVehicles(@RequestParam(defaultValue = "10") Integer limit) {
        List<Vehicle> vehicles = vehicleService.getFeaturedVehicles(limit);
        List<VehicleResponse> responses = vehicles.stream()
            .map(VehicleResponse::from)
            .collect(Collectors.toList());
        return Result.success(responses);
    }

    /**
     * 车辆列表（分页）
     */
    @GetMapping("/list")
    public Result<PageResult<VehicleResponse>> getVehicleList(
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) String model,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        PageResult<Vehicle> pageResult = vehicleService.getVehiclePage(
            brandId, model, 1, pageNum, pageSize);  // 只查询上架的车辆
        
        List<VehicleResponse> responses = pageResult.getList().stream()
            .map(VehicleResponse::from)
            .collect(Collectors.toList());
        
        PageResult<VehicleResponse> result = new PageResult<>(
            responses, 
            pageResult.getTotal(), 
            pageResult.getPageNum(), 
            pageResult.getPageSize()
        );
        
        return Result.success(result);
    }

    /**
     * 车辆详情
     */
    @GetMapping("/{id}")
    public Result<VehicleResponse> getVehicleDetail(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.getVehicleById(id);
        VehicleResponse response = VehicleResponse.from(vehicle);
        
        // 获取车辆图片
        List<VehicleImage> images = vehicleService.getVehicleImages(id);
        response.setImages(images.stream()
            .map(VehicleImage::getImageUrl)
            .collect(Collectors.toList()));
        
        return Result.success(response);
    }

    /**
     * 车辆图片列表
     */
    @GetMapping("/{id}/images")
    public Result<List<String>> getVehicleImages(@PathVariable Long id) {
        List<VehicleImage> images = vehicleService.getVehicleImages(id);
        List<String> urls = images.stream()
            .map(VehicleImage::getImageUrl)
            .collect(Collectors.toList());
        return Result.success(urls);
    }

    /**
     * 同品牌其他车型
     */
    @GetMapping("/{id}/similar")
    public Result<List<VehicleResponse>> getSimilarVehicles(
            @PathVariable Long id,
            @RequestParam(defaultValue = "5") Integer limit) {
        
        List<Vehicle> vehicles = vehicleService.getSimilarVehicles(id, limit);
        List<VehicleResponse> responses = vehicles.stream()
            .map(VehicleResponse::from)
            .collect(Collectors.toList());
        
        return Result.success(responses);
    }
}

