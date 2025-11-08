package org.demo.car.mapper;

import org.apache.ibatis.annotations.*;
import org.demo.car.entity.VehicleImage;

import java.util.List;

/**
 * 车辆图片Mapper
 */
@Mapper
public interface VehicleImageMapper {

    @Select("SELECT * FROM vehicle_image WHERE id = #{id}")
    VehicleImage findById(Long id);

    @Select("SELECT * FROM vehicle_image WHERE vehicle_id = #{vehicleId} ORDER BY sort_order ASC")
    List<VehicleImage> findByVehicleId(Long vehicleId);

    @Insert("INSERT INTO vehicle_image(vehicle_id, image_url, sort_order) " +
            "VALUES(#{vehicleId}, #{imageUrl}, #{sortOrder})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(VehicleImage vehicleImage);

    @Update("UPDATE vehicle_image SET sort_order = #{sortOrder} WHERE id = #{id}")
    int updateSortOrder(@Param("id") Long id, @Param("sortOrder") Integer sortOrder);

    @Delete("DELETE FROM vehicle_image WHERE id = #{id}")
    int deleteById(Long id);

    @Delete("DELETE FROM vehicle_image WHERE vehicle_id = #{vehicleId}")
    int deleteByVehicleId(Long vehicleId);
}

