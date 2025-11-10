package org.demo.car.mapper;

import org.apache.ibatis.annotations.*;
import org.demo.car.entity.Admin;

import java.util.List;

/**
 * 管理员Mapper
 */
@Mapper
public interface AdminMapper {

    @Select("SELECT * FROM admin WHERE id = #{id}")
    Admin findById(Long id);

    @Select("SELECT * FROM admin WHERE username = #{username}")
    Admin findByUsername(String username);

    @Insert("INSERT INTO admin(username, password, real_name, phone, role_id, status) " +
            "VALUES(#{username}, #{password}, #{realName}, #{phone}, #{roleId}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Admin admin);

    @Update("UPDATE admin SET username = #{username}, password = #{password}, real_name = #{realName}, phone = #{phone}, " +
            "role_id = #{roleId}, status = #{status}, update_time = NOW() WHERE id = #{id}")
    int update(Admin admin);
    
    @Update("UPDATE admin SET status = #{status}, update_time = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Update("UPDATE admin SET password = #{password} WHERE id = #{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    @Delete("DELETE FROM admin WHERE id = #{id}")
    int deleteById(Long id);

    @Select("<script>" +
            "SELECT * FROM admin " +
            "<where>" +
            "<if test='username != null and username != \"\"'> AND username LIKE CONCAT('%', #{username}, '%') </if>" +
            "<if test='realName != null and realName != \"\"'> AND real_name LIKE CONCAT('%', #{realName}, '%') </if>" +
            "<if test='roleId != null'> AND role_id = #{roleId} </if>" +
            "<if test='status != null'> AND status = #{status} </if>" +
            "</where>" +
            "ORDER BY create_time DESC " +
            "LIMIT #{offset}, #{limit}" +
            "</script>")
    List<Admin> findByPage(@Param("username") String username,
                           @Param("realName") String realName,
                           @Param("roleId") Long roleId,
                           @Param("status") Integer status,
                           @Param("offset") Integer offset,
                           @Param("limit") Integer limit);

    @Select("<script>" +
            "SELECT COUNT(*) FROM admin " +
            "<where>" +
            "<if test='username != null and username != \"\"'> AND username LIKE CONCAT('%', #{username}, '%') </if>" +
            "<if test='realName != null and realName != \"\"'> AND real_name LIKE CONCAT('%', #{realName}, '%') </if>" +
            "<if test='roleId != null'> AND role_id = #{roleId} </if>" +
            "<if test='status != null'> AND status = #{status} </if>" +
            "</where>" +
            "</script>")
    long countByPage(@Param("username") String username,
                     @Param("realName") String realName,
                     @Param("roleId") Long roleId,
                     @Param("status") Integer status);
}

