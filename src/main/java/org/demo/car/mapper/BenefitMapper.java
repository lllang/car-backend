package org.demo.car.mapper;

import org.apache.ibatis.annotations.*;
import org.demo.car.entity.Benefit;

import java.util.List;

/**
 * 权益Mapper
 */
@Mapper
public interface BenefitMapper {

    @Select("SELECT * FROM benefit WHERE id = #{id}")
    Benefit findById(Long id);

    @Select("SELECT * FROM benefit WHERE code = #{code}")
    Benefit findByCode(String code);

    @Select("SELECT * FROM benefit WHERE status = 1 ORDER BY create_time DESC")
    List<Benefit> findAllActive();

    @Insert("INSERT INTO benefit(name, code, type, image, description, stock, status) " +
            "VALUES(#{name}, #{code}, #{type}, #{image}, #{description}, #{stock}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Benefit benefit);

    @Update("UPDATE benefit SET name = #{name}, type = #{type}, image = #{image}, " +
            "description = #{description}, stock = #{stock}, status = #{status} WHERE id = #{id}")
    int update(Benefit benefit);

    @Update("UPDATE benefit SET stock = stock - #{quantity} WHERE id = #{id} AND (stock >= #{quantity} OR stock = -1)")
    int decreaseStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    @Delete("DELETE FROM benefit WHERE id = #{id}")
    int deleteById(Long id);

    @Select("<script>" +
            "SELECT * FROM benefit " +
            "<where>" +
            "<if test='name != null and name != \"\"'> AND name LIKE CONCAT('%', #{name}, '%') </if>" +
            "<if test='type != null and type != \"\"'> AND type = #{type} </if>" +
            "<if test='status != null'> AND status = #{status} </if>" +
            "</where>" +
            "ORDER BY create_time DESC " +
            "LIMIT #{offset}, #{limit}" +
            "</script>")
    List<Benefit> findByPage(@Param("name") String name,
                             @Param("type") String type,
                             @Param("status") Integer status,
                             @Param("offset") Integer offset,
                             @Param("limit") Integer limit);

    @Select("<script>" +
            "SELECT COUNT(*) FROM benefit " +
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

