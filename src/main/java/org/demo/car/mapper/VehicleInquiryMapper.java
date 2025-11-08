package org.demo.car.mapper;

import org.apache.ibatis.annotations.*;
import org.demo.car.entity.VehicleInquiry;

import java.util.List;

/**
 * 新车询价Mapper
 */
@Mapper
public interface VehicleInquiryMapper {

    @Select("SELECT * FROM vehicle_inquiry WHERE id = #{id}")
    VehicleInquiry findById(Long id);

    @Select("SELECT * FROM vehicle_inquiry WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<VehicleInquiry> findByUserId(Long userId);

    @Insert("INSERT INTO vehicle_inquiry(user_id, vehicle_id, brand_id, phone, need_exchange, dealer_name, status) " +
            "VALUES(#{userId}, #{vehicleId}, #{brandId}, #{phone}, #{needExchange}, #{dealerName}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(VehicleInquiry inquiry);

    @Update("UPDATE vehicle_inquiry SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Delete("DELETE FROM vehicle_inquiry WHERE id = #{id}")
    int deleteById(Long id);

    @Select("<script>" +
            "SELECT * FROM vehicle_inquiry " +
            "<where>" +
            "<if test='phone != null and phone != \"\"'> AND phone LIKE CONCAT('%', #{phone}, '%') </if>" +
            "<if test='brandId != null'> AND brand_id = #{brandId} </if>" +
            "<if test='status != null and status != \"\"'> AND status = #{status} </if>" +
            "<if test='needExchange != null'> AND need_exchange = #{needExchange} </if>" +
            "</where>" +
            "ORDER BY create_time DESC " +
            "LIMIT #{offset}, #{limit}" +
            "</script>")
    List<VehicleInquiry> findByPage(@Param("phone") String phone,
                                    @Param("brandId") Long brandId,
                                    @Param("status") String status,
                                    @Param("needExchange") Integer needExchange,
                                    @Param("offset") Integer offset,
                                    @Param("limit") Integer limit);

    @Select("<script>" +
            "SELECT COUNT(*) FROM vehicle_inquiry " +
            "<where>" +
            "<if test='phone != null and phone != \"\"'> AND phone LIKE CONCAT('%', #{phone}, '%') </if>" +
            "<if test='brandId != null'> AND brand_id = #{brandId} </if>" +
            "<if test='status != null and status != \"\"'> AND status = #{status} </if>" +
            "<if test='needExchange != null'> AND need_exchange = #{needExchange} </if>" +
            "</where>" +
            "</script>")
    long countByPage(@Param("phone") String phone,
                     @Param("brandId") Long brandId,
                     @Param("status") String status,
                     @Param("needExchange") Integer needExchange);
}

