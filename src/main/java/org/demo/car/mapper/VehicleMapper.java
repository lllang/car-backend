package org.demo.car.mapper;

import org.apache.ibatis.annotations.*;
import org.demo.car.entity.Vehicle;

import java.util.List;

/**
 * 车辆Mapper
 */
@Mapper
public interface VehicleMapper {

    @Select("SELECT * FROM vehicle WHERE id = #{id}")
    Vehicle findById(Long id);

    @Select("SELECT * FROM vehicle WHERE status = 1 AND is_featured = 1 " +
            "ORDER BY sort_order DESC, create_time DESC LIMIT #{limit}")
    List<Vehicle> findFeatured(Integer limit);

    @Select("SELECT * FROM vehicle WHERE brand_id = #{brandId} AND status = 1 AND id != #{excludeId} " +
            "ORDER BY sort_order DESC LIMIT #{limit}")
    List<Vehicle> findSimilarByBrand(@Param("brandId") Long brandId, 
                                     @Param("excludeId") Long excludeId,
                                     @Param("limit") Integer limit);

    @Insert("INSERT INTO vehicle(brand_id, model, guide_price, battery_capacity, endurance, " +
            "fast_charge, slow_charge, main_image, status, is_featured, sort_order) " +
            "VALUES(#{brandId}, #{model}, #{guidePrice}, #{batteryCapacity}, #{endurance}, " +
            "#{fastCharge}, #{slowCharge}, #{mainImage}, #{status}, #{isFeatured}, #{sortOrder})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Vehicle vehicle);

    @Update("UPDATE vehicle SET brand_id = #{brandId}, model = #{model}, guide_price = #{guidePrice}, " +
            "battery_capacity = #{batteryCapacity}, endurance = #{endurance}, fast_charge = #{fastCharge}, " +
            "slow_charge = #{slowCharge}, main_image = #{mainImage}, status = #{status}, " +
            "is_featured = #{isFeatured}, sort_order = #{sortOrder} WHERE id = #{id}")
    int update(Vehicle vehicle);

    @Update("UPDATE vehicle SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    @Update("UPDATE vehicle SET is_featured = #{isFeatured} WHERE id = #{id}")
    int updateFeatured(@Param("id") Long id, @Param("isFeatured") Integer isFeatured);

    @Delete("DELETE FROM vehicle WHERE id = #{id}")
    int deleteById(Long id);

    @Select("<script>" +
            "SELECT * FROM vehicle " +
            "<where>" +
            "<if test='brandId != null'> AND brand_id = #{brandId} </if>" +
            "<if test='model != null and model != \"\"'> AND model LIKE CONCAT('%', #{model}, '%') </if>" +
            "<if test='status != null'> AND status = #{status} </if>" +
            "<if test='isFeatured != null'> AND is_featured = #{isFeatured} </if>" +
            "</where>" +
            "ORDER BY sort_order DESC, create_time DESC " +
            "LIMIT #{offset}, #{limit}" +
            "</script>")
    List<Vehicle> findByPage(@Param("brandId") Long brandId,
                             @Param("model") String model,
                             @Param("status") Integer status,
                             @Param("isFeatured") Integer isFeatured,
                             @Param("offset") Integer offset,
                             @Param("limit") Integer limit);

    @Select("<script>" +
            "SELECT COUNT(*) FROM vehicle " +
            "<where>" +
            "<if test='brandId != null'> AND brand_id = #{brandId} </if>" +
            "<if test='model != null and model != \"\"'> AND model LIKE CONCAT('%', #{model}, '%') </if>" +
            "<if test='status != null'> AND status = #{status} </if>" +
            "<if test='isFeatured != null'> AND is_featured = #{isFeatured} </if>" +
            "</where>" +
            "</script>")
    long countByPage(@Param("brandId") Long brandId,
                     @Param("model") String model,
                     @Param("status") Integer status,
                     @Param("isFeatured") Integer isFeatured);
}

