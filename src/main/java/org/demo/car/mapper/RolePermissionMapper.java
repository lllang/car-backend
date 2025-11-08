package org.demo.car.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 角色权限关联Mapper
 */
@Mapper
public interface RolePermissionMapper {

    @Insert("INSERT INTO role_permission(role_id, permission_id) VALUES(#{roleId}, #{permissionId})")
    int insert(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    @Delete("DELETE FROM role_permission WHERE role_id = #{roleId}")
    int deleteByRoleId(Long roleId);

    @Delete("DELETE FROM role_permission WHERE permission_id = #{permissionId}")
    int deleteByPermissionId(Long permissionId);

    @Select("SELECT permission_id FROM role_permission WHERE role_id = #{roleId}")
    List<Long> findPermissionIdsByRoleId(Long roleId);

    @Insert("<script>" +
            "INSERT INTO role_permission(role_id, permission_id) VALUES " +
            "<foreach collection='permissionIds' item='permissionId' separator=','>" +
            "(#{roleId}, #{permissionId})" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("roleId") Long roleId, @Param("permissionIds") List<Long> permissionIds);
}

