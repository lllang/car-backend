package org.demo.car.mapper;

import org.apache.ibatis.annotations.*;
import org.demo.car.entity.Brand;

import java.util.List;

/**
 * 品牌Mapper
 */
@Mapper
public interface BrandMapper {

    @Select("SELECT * FROM brand WHERE id = #{id}")
    Brand findById(Long id);

    @Select("SELECT * FROM brand WHERE status = 1 ORDER BY create_time DESC")
    List<Brand> findAllActive();

    @Insert("INSERT INTO brand(name, logo, dealer_name, dealer_address, dealer_contact_name, dealer_contact_phone, status) " +
            "VALUES(#{name}, #{logo}, #{dealerName}, #{dealerAddress}, #{dealerContactName}, #{dealerContactPhone}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Brand brand);

    @Update("UPDATE brand SET name = #{name}, logo = #{logo}, dealer_name = #{dealerName}, " +
            "dealer_address = #{dealerAddress}, dealer_contact_name = #{dealerContactName}, " +
            "dealer_contact_phone = #{dealerContactPhone}, status = #{status} WHERE id = #{id}")
    int update(Brand brand);

    @Update("UPDATE brand SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Delete("DELETE FROM brand WHERE id = #{id}")
    int deleteById(Long id);

    @Select("<script>" +
            "SELECT * FROM brand " +
            "<where>" +
            "<if test='name != null and name != \"\"'> AND name LIKE CONCAT('%', #{name}, '%') </if>" +
            "<if test='status != null'> AND status = #{status} </if>" +
            "</where>" +
            "ORDER BY create_time DESC " +
            "LIMIT #{offset}, #{limit}" +
            "</script>")
    List<Brand> findByPage(@Param("name") String name,
                           @Param("status") Integer status,
                           @Param("offset") Integer offset,
                           @Param("limit") Integer limit);

    @Select("<script>" +
            "SELECT COUNT(*) FROM brand " +
            "<where>" +
            "<if test='name != null and name != \"\"'> AND name LIKE CONCAT('%', #{name}, '%') </if>" +
            "<if test='status != null'> AND status = #{status} </if>" +
            "</where>" +
            "</script>")
    long countByPage(@Param("name") String name, @Param("status") Integer status);
}

