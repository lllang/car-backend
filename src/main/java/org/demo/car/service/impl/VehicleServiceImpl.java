package org.demo.car.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.exception.BusinessException;
import org.demo.car.common.result.PageResult;
import org.demo.car.entity.Vehicle;
import org.demo.car.entity.VehicleImage;
import org.demo.car.mapper.VehicleImageMapper;
import org.demo.car.mapper.VehicleMapper;
import org.demo.car.service.VehicleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 车辆服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleMapper vehicleMapper;
    private final VehicleImageMapper vehicleImageMapper;

    @Override
    public List<Vehicle> getFeaturedVehicles(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        return vehicleMapper.findFeatured(limit);
    }

    @Override
    public PageResult<Vehicle> getVehiclePage(Long brandId, String model, Integer status, 
                                               Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }
        
        int offset = (pageNum - 1) * pageSize;
        List<Vehicle> list = vehicleMapper.findByPage(brandId, model, status, null, offset, pageSize);
        long total = vehicleMapper.countByPage(brandId, model, status, null);
        
        return new PageResult<>(list, total, pageNum, pageSize);
    }

    @Override
    public Vehicle getVehicleById(Long id) {
        Vehicle vehicle = vehicleMapper.findById(id);
        if (vehicle == null) {
            throw new BusinessException("车辆不存在");
        }
        return vehicle;
    }

    @Override
    public List<VehicleImage> getVehicleImages(Long vehicleId) {
        return vehicleImageMapper.findByVehicleId(vehicleId);
    }

    @Override
    public List<Vehicle> getSimilarVehicles(Long vehicleId, Integer limit) {
        Vehicle vehicle = vehicleMapper.findById(vehicleId);
        if (vehicle == null) {
            throw new BusinessException("车辆不存在");
        }
        
        if (limit == null || limit <= 0) {
            limit = 5;
        }
        
        return vehicleMapper.findSimilarByBrand(vehicle.getBrandId(), vehicleId, limit);
    }

    @Override
    @Transactional
    public Long createVehicle(Vehicle vehicle) {
        vehicleMapper.insert(vehicle);
        log.info("创建车辆成功: vehicleId={}", vehicle.getId());
        return vehicle.getId();
    }

    @Override
    @Transactional
    public void updateVehicle(Vehicle vehicle) {
        Vehicle existing = vehicleMapper.findById(vehicle.getId());
        if (existing == null) {
            throw new BusinessException("车辆不存在");
        }
        
        // 只更新非空字段
        if (vehicle.getBrandId() != null) {
            existing.setBrandId(vehicle.getBrandId());
        }
        if (vehicle.getModel() != null) {
            existing.setModel(vehicle.getModel());
        }
        if (vehicle.getGuidePrice() != null) {
            existing.setGuidePrice(vehicle.getGuidePrice());
        }
        if (vehicle.getBatteryCapacity() != null) {
            existing.setBatteryCapacity(vehicle.getBatteryCapacity());
        }
        if (vehicle.getEndurance() != null) {
            existing.setEndurance(vehicle.getEndurance());
        }
        if (vehicle.getFastCharge() != null) {
            existing.setFastCharge(vehicle.getFastCharge());
        }
        if (vehicle.getSlowCharge() != null) {
            existing.setSlowCharge(vehicle.getSlowCharge());
        }
        if (vehicle.getMainImage() != null) {
            existing.setMainImage(vehicle.getMainImage());
        }
        // status 字段通过专门的 updateVehicleStatus 方法更新，这里不处理
        if (vehicle.getIsFeatured() != null) {
            existing.setIsFeatured(vehicle.getIsFeatured());
        }
        if (vehicle.getSortOrder() != null) {
            existing.setSortOrder(vehicle.getSortOrder());
        }
        
        vehicleMapper.update(existing);
        log.info("更新车辆成功: vehicleId={}", vehicle.getId());
    }

    @Override
    @Transactional
    public void deleteVehicle(Long id) {
        Vehicle vehicle = vehicleMapper.findById(id);
        if (vehicle == null) {
            throw new BusinessException("车辆不存在");
        }
        
        // 删除车辆图片
        vehicleImageMapper.deleteByVehicleId(id);
        
        // 删除车辆
        vehicleMapper.deleteById(id);
        log.info("删除车辆成功: vehicleId={}", id);
    }

    @Override
    @Transactional
    public void updateVehicleStatus(Long id, Integer status) {
        Vehicle vehicle = vehicleMapper.findById(id);
        if (vehicle == null) {
            throw new BusinessException("车辆不存在");
        }
        
        vehicleMapper.updateStatus(id, status);
        log.info("更新车辆状态成功: vehicleId={}, status={}", id, status);
    }
    
    @Override
    @Transactional
    public void updateVehicleFeatured(Long id, Integer isFeatured) {
        Vehicle vehicle = vehicleMapper.findById(id);
        if (vehicle == null) {
            throw new BusinessException("车辆不存在");
        }
        
        vehicleMapper.updateFeatured(id, isFeatured);
        log.info("更新车辆精选状态成功: vehicleId={}, isFeatured={}", id, isFeatured);
    }
}

