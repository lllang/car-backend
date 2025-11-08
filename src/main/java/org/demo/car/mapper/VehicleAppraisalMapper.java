package org.demo.car.mapper;

import org.apache.ibatis.annotations.*;
import org.demo.car.entity.VehicleAppraisal;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 旧车估价Mapper
 */
@Mapper
public interface VehicleAppraisalMapper {

    @Select("SELECT * FROM vehicle_appraisal WHERE id = #{id}")
    VehicleAppraisal findById(Long id);

    @Select("SELECT * FROM vehicle_appraisal WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<VehicleAppraisal> findByUserId(Long userId);

    @Insert("INSERT INTO vehicle_appraisal(user_id, phone, old_brand, intention_brand, purchase_year, region, status) " +
            "VALUES(#{userId}, #{phone}, #{oldBrand}, #{intentionBrand}, #{purchaseYear}, #{region}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(VehicleAppraisal appraisal);

    @Update("UPDATE vehicle_appraisal SET status = #{status}, follower_id = #{followerId}, " +
            "follower_name = #{followerName}, follow_time = #{followTime}, remark = #{remark} WHERE id = #{id}")
    int updateFollow(VehicleAppraisal appraisal);

    @Delete("DELETE FROM vehicle_appraisal WHERE id = #{id}")
    int deleteById(Long id);

    @Select("<script>" +
            "SELECT * FROM vehicle_appraisal " +
            "<where>" +
            "<if test='phone != null and phone != \"\"'> AND phone LIKE CONCAT('%', #{phone}, '%') </if>" +
            "<if test='oldBrand != null and oldBrand != \"\"'> AND old_brand LIKE CONCAT('%', #{oldBrand}, '%') </if>" +
            "<if test='intentionBrand != null and intentionBrand != \"\"'> AND intention_brand LIKE CONCAT('%', #{intentionBrand}, '%') </if>" +
            "<if test='status != null and status != \"\"'> AND status = #{status} </if>" +
            "<if test='followerId != null'> AND follower_id = #{followerId} </if>" +
            "</where>" +
            "ORDER BY create_time DESC " +
            "LIMIT #{offset}, #{limit}" +
            "</script>")
    List<VehicleAppraisal> findByPage(@Param("phone") String phone,
                                      @Param("oldBrand") String oldBrand,
                                      @Param("intentionBrand") String intentionBrand,
                                      @Param("status") String status,
                                      @Param("followerId") Long followerId,
                                      @Param("offset") Integer offset,
                                      @Param("limit") Integer limit);

    @Select("<script>" +
            "SELECT COUNT(*) FROM vehicle_appraisal " +
            "<where>" +
            "<if test='phone != null and phone != \"\"'> AND phone LIKE CONCAT('%', #{phone}, '%') </if>" +
            "<if test='oldBrand != null and oldBrand != \"\"'> AND old_brand LIKE CONCAT('%', #{oldBrand}, '%') </if>" +
            "<if test='intentionBrand != null and intentionBrand != \"\"'> AND intention_brand LIKE CONCAT('%', #{intentionBrand}, '%') </if>" +
            "<if test='status != null and status != \"\"'> AND status = #{status} </if>" +
            "<if test='followerId != null'> AND follower_id = #{followerId} </if>" +
            "</where>" +
            "</script>")
    long countByPage(@Param("phone") String phone,
                     @Param("oldBrand") String oldBrand,
                     @Param("intentionBrand") String intentionBrand,
                     @Param("status") String status,
                     @Param("followerId") Long followerId);
}

