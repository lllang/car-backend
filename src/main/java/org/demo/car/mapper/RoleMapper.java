package org.demo.car.mapper;

import org.apache.ibatis.annotations.*;
import org.demo.car.entity.Role;

import java.util.List;

/**
 * 角色Mapper
 */
@Mapper
public interface RoleMapper {

    @Select("SELECT * FROM role WHERE id = #{id}")
    Role findById(Long id);

    @Select("SELECT * FROM role WHERE code = #{code}")
    Role findByCode(String code);

    @Select("SELECT * FROM role ORDER BY create_time ASC")
    List<Role> findAll();

    @Insert("INSERT INTO role(name, code, description) VALUES(#{name}, #{code}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Role role);

    @Update("UPDATE role SET name = #{name}, description = #{description} WHERE id = #{id}")
    int update(Role role);

    @Delete("DELETE FROM role WHERE id = #{id}")
    int deleteById(Long id);
}

