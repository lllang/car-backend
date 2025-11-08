package org.demo.car.mapper;

import org.apache.ibatis.annotations.*;
import org.demo.car.entity.UserBenefit;

import java.util.List;

/**
 * 用户权益领取记录Mapper
 */
@Mapper
public interface UserBenefitMapper {

    @Select("SELECT * FROM user_benefit WHERE id = #{id}")
    UserBenefit findById(Long id);

    @Select("SELECT * FROM user_benefit WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<UserBenefit> findByUserId(Long userId);

    @Insert("INSERT INTO user_benefit(user_id, benefit_id, benefit_name, benefit_image, quantity, " +
            "city, license_plate, phone, verification_code, status) " +
            "VALUES(#{userId}, #{benefitId}, #{benefitName}, #{benefitImage}, #{quantity}, " +
            "#{city}, #{licensePlate}, #{phone}, #{verificationCode}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserBenefit userBenefit);

    @Update("UPDATE user_benefit SET status = #{status}, use_time = #{useTime} WHERE id = #{id}")
    int updateStatus(UserBenefit userBenefit);

    @Delete("DELETE FROM user_benefit WHERE id = #{id}")
    int deleteById(Long id);

    @Select("<script>" +
            "SELECT * FROM user_benefit " +
            "<where>" +
            "<if test='userId != null'> AND user_id = #{userId} </if>" +
            "<if test='benefitId != null'> AND benefit_id = #{benefitId} </if>" +
            "<if test='phone != null and phone != \"\"'> AND phone LIKE CONCAT('%', #{phone}, '%') </if>" +
            "<if test='status != null and status != \"\"'> AND status = #{status} </if>" +
            "</where>" +
            "ORDER BY create_time DESC " +
            "LIMIT #{offset}, #{limit}" +
            "</script>")
    List<UserBenefit> findByPage(@Param("userId") Long userId,
                                 @Param("benefitId") Long benefitId,
                                 @Param("phone") String phone,
                                 @Param("status") String status,
                                 @Param("offset") Integer offset,
                                 @Param("limit") Integer limit);

    @Select("<script>" +
            "SELECT COUNT(*) FROM user_benefit " +
            "<where>" +
            "<if test='userId != null'> AND user_id = #{userId} </if>" +
            "<if test='benefitId != null'> AND benefit_id = #{benefitId} </if>" +
            "<if test='phone != null and phone != \"\"'> AND phone LIKE CONCAT('%', #{phone}, '%') </if>" +
            "<if test='status != null and status != \"\"'> AND status = #{status} </if>" +
            "</where>" +
            "</script>")
    long countByPage(@Param("userId") Long userId,
                     @Param("benefitId") Long benefitId,
                     @Param("phone") String phone,
                     @Param("status") String status);
}

