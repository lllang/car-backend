package org.demo.car.mapper;

import org.apache.ibatis.annotations.*;
import org.demo.car.entity.UserFavorite;
import org.demo.car.entity.Vehicle;

import java.util.List;

/**
 * 用户喜欢Mapper
 */
@Mapper
public interface UserFavoriteMapper {

    @Select("SELECT * FROM user_favorite WHERE id = #{id}")
    UserFavorite findById(Long id);

    @Select("SELECT * FROM user_favorite WHERE user_id = #{userId} AND vehicle_id = #{vehicleId}")
    UserFavorite findByUserIdAndVehicleId(@Param("userId") Long userId, @Param("vehicleId") Long vehicleId);

    @Select("SELECT v.*, b.name as brand_name FROM vehicle v " +
            "INNER JOIN user_favorite uf ON v.id = uf.vehicle_id " +
            "LEFT JOIN brand b ON v.brand_id = b.id " +
            "WHERE uf.user_id = #{userId} " +
            "ORDER BY uf.create_time DESC")
    @Results({
            @Result(property = "brandName", column = "brand_name")
    })
    List<Vehicle> findFavoriteVehiclesByUserId(Long userId);

    @Insert("INSERT INTO user_favorite(user_id, vehicle_id) VALUES(#{userId}, #{vehicleId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserFavorite favorite);

    @Delete("DELETE FROM user_favorite WHERE user_id = #{userId} AND vehicle_id = #{vehicleId}")
    int deleteByUserIdAndVehicleId(@Param("userId") Long userId, @Param("vehicleId") Long vehicleId);

    @Delete("DELETE FROM user_favorite WHERE id = #{id}")
    int deleteById(Long id);
    
    @Select("SELECT vehicle_id FROM user_favorite WHERE user_id = #{userId}")
    List<Long> findVehicleIdsByUserId(Long userId);
}

