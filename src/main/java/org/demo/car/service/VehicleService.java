package org.demo.car.service;

import org.demo.car.common.result.PageResult;
import org.demo.car.entity.Vehicle;
import org.demo.car.entity.VehicleImage;

import java.util.List;

/**
 * 车辆服务接口
 */
public interface VehicleService {
    
    /**
     * 获取精选现车
     */
    List<Vehicle> getFeaturedVehicles(Integer limit);
    
    /**
     * 分页查询车辆
     */
    PageResult<Vehicle> getVehiclePage(Long brandId, String model, Integer status, 
                                       Integer pageNum, Integer pageSize);
    
    /**
     * 获取车辆详情
     */
    Vehicle getVehicleById(Long id);
    
    /**
     * 获取车辆图片
     */
    List<VehicleImage> getVehicleImages(Long vehicleId);
    
    /**
     * 获取相似车型
     */
    List<Vehicle> getSimilarVehicles(Long vehicleId, Integer limit);
    
    /**
     * 创建车辆
     */
    Long createVehicle(Vehicle vehicle);
    
    /**
     * 更新车辆
     */
    void updateVehicle(Vehicle vehicle);
    
    /**
     * 删除车辆
     */
    void deleteVehicle(Long id);
    
    /**
     * 上下架车辆
     */
    void updateVehicleStatus(Long id, Integer status);
    
    /**
     * 设置精选
     */
    void updateVehicleFeatured(Long id, Integer isFeatured);
}

