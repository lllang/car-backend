package org.demo.car.dto.response;

import lombok.Data;
import org.demo.car.entity.Vehicle;

import java.util.List;

/**
 * 车辆响应
 */
@Data
public class VehicleResponse {
    private Long id;
    private Long brandId;
    private String brandName;
    private String model;
    private String guidePrice;
    private String batteryCapacity;
    private String endurance;
    private String fastCharge;
    private String slowCharge;
    private String mainImage;
    private Integer status;
    private Integer isFeatured;
    private List<String> images;
    private Boolean isFavorite;  // 是否已收藏
    
    public static VehicleResponse from(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        VehicleResponse response = new VehicleResponse();
        response.setId(vehicle.getId());
        response.setBrandId(vehicle.getBrandId());
        response.setBrandName(vehicle.getBrandName());
        response.setModel(vehicle.getModel());
        response.setGuidePrice(vehicle.getGuidePrice());
        response.setBatteryCapacity(vehicle.getBatteryCapacity());
        response.setEndurance(vehicle.getEndurance());
        response.setFastCharge(vehicle.getFastCharge());
        response.setSlowCharge(vehicle.getSlowCharge());
        response.setMainImage(vehicle.getMainImage());
        response.setStatus(vehicle.getStatus());
        response.setIsFeatured(vehicle.getIsFeatured());
        return response;
    }
}

