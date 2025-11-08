package org.demo.car.mapper;

import org.apache.ibatis.annotations.*;
import org.demo.car.entity.User;

import java.util.List;

/**
 * 用户Mapper
 */
@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);

    @Select("SELECT * FROM user WHERE openid = #{openid}")
    User findByOpenid(String openid);

    @Select("SELECT * FROM user WHERE phone = #{phone}")
    User findByPhone(String phone);

    @Insert("INSERT INTO user(openid, nickname, avatar, phone, age, gender, address) " +
            "VALUES(#{openid}, #{nickname}, #{avatar}, #{phone}, #{age}, #{gender}, #{address})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("UPDATE user SET nickname = #{nickname}, avatar = #{avatar}, phone = #{phone}, " +
            "age = #{age}, gender = #{gender}, address = #{address} WHERE id = #{id}")
    int update(User user);

    @Delete("DELETE FROM user WHERE id = #{id}")
    int deleteById(Long id);

    @Select("<script>" +
            "SELECT * FROM user " +
            "<where>" +
            "<if test='phone != null and phone != \"\"'> AND phone LIKE CONCAT('%', #{phone}, '%') </if>" +
            "<if test='nickname != null and nickname != \"\"'> AND nickname LIKE CONCAT('%', #{nickname}, '%') </if>" +
            "</where>" +
            "ORDER BY create_time DESC " +
            "LIMIT #{offset}, #{limit}" +
            "</script>")
    List<User> findByPage(@Param("phone") String phone, 
                          @Param("nickname") String nickname,
                          @Param("offset") Integer offset, 
                          @Param("limit") Integer limit);

    @Select("<script>" +
            "SELECT COUNT(*) FROM user " +
            "<where>" +
            "<if test='phone != null and phone != \"\"'> AND phone LIKE CONCAT('%', #{phone}, '%') </if>" +
            "<if test='nickname != null and nickname != \"\"'> AND nickname LIKE CONCAT('%', #{nickname}, '%') </if>" +
            "</where>" +
            "</script>")
    long countByPage(@Param("phone") String phone, @Param("nickname") String nickname);
}

