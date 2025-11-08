package org.demo.car.mapper;

import org.apache.ibatis.annotations.*;
import org.demo.car.entity.Permission;

import java.util.List;

/**
 * 权限Mapper
 */
@Mapper
public interface PermissionMapper {

    @Select("SELECT * FROM permission WHERE id = #{id}")
    Permission findById(Long id);

    @Select("SELECT * FROM permission ORDER BY parent_id, id ASC")
    List<Permission> findAll();

    @Select("SELECT p.* FROM permission p " +
            "INNER JOIN role_permission rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id = #{roleId} " +
            "ORDER BY p.parent_id, p.id ASC")
    List<Permission> findByRoleId(Long roleId);

    @Insert("INSERT INTO permission(name, code, type, parent_id, path) " +
            "VALUES(#{name}, #{code}, #{type}, #{parentId}, #{path})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Permission permission);

    @Update("UPDATE permission SET name = #{name}, type = #{type}, parent_id = #{parentId}, path = #{path} " +
            "WHERE id = #{id}")
    int update(Permission permission);

    @Delete("DELETE FROM permission WHERE id = #{id}")
    int deleteById(Long id);
}

