package org.demo.car.mapper;

import org.apache.ibatis.annotations.*;
import org.demo.car.entity.Activity;

import java.util.List;

/**
 * 活动Mapper
 */
@Mapper
public interface ActivityMapper {

    @Select("SELECT * FROM activity WHERE id = #{id}")
    Activity findById(Long id);

    @Select("SELECT * FROM activity WHERE type = #{type} AND status = 1 " +
            "AND (start_time IS NULL OR start_time <= NOW()) " +
            "AND (end_time IS NULL OR end_time > NOW()) " +
            "ORDER BY sort_order DESC, create_time DESC")
    List<Activity> findByType(String type);

    @Insert("INSERT INTO activity(name, type, image, link_url, vehicle_id, content, status, " +
            "start_time, end_time, sort_order) " +
            "VALUES(#{name}, #{type}, #{image}, #{linkUrl}, #{vehicleId}, #{content}, #{status}, " +
            "#{startTime}, #{endTime}, #{sortOrder})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Activity activity);

    @Update("UPDATE activity SET name = #{name}, type = #{type}, image = #{image}, link_url = #{linkUrl}, " +
            "vehicle_id = #{vehicleId}, content = #{content}, status = #{status}, " +
            "start_time = #{startTime}, end_time = #{endTime}, sort_order = #{sortOrder} WHERE id = #{id}")
    int update(Activity activity);

    @Update("UPDATE activity SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Delete("DELETE FROM activity WHERE id = #{id}")
    int deleteById(Long id);

    @Select("<script>" +
            "SELECT * FROM activity " +
            "<where>" +
            "<if test='name != null and name != \"\"'> AND name LIKE CONCAT('%', #{name}, '%') </if>" +
            "<if test='type != null and type != \"\"'> AND type = #{type} </if>" +
            "<if test='status != null'> AND status = #{status} </if>" +
            "</where>" +
            "ORDER BY sort_order DESC, create_time DESC " +
            "LIMIT #{offset}, #{limit}" +
            "</script>")
    List<Activity> findByPage(@Param("name") String name,
                              @Param("type") String type,
                              @Param("status") Integer status,
                              @Param("offset") Integer offset,
                              @Param("limit") Integer limit);

    @Select("<script>" +
            "SELECT COUNT(*) FROM activity " +
            "<where>" +
            "<if test='name != null and name != \"\"'> AND name LIKE CONCAT('%', #{name}, '%') </if>" +
            "<if test='type != null and type != \"\"'> AND type = #{type} </if>" +
            "<if test='status != null'> AND status = #{status} </if>" +
            "</where>" +
            "</script>")
    long countByPage(@Param("name") String name,
                     @Param("type") String type,
                     @Param("status") Integer status);
}

