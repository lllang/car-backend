package org.demo.car.common.enums;

import lombok.Getter;

/**
 * 角色编码枚举
 */
@Getter
public enum RoleCode {
    SUPER_ADMIN("SUPER_ADMIN", "超级管理员"),
    ADMIN("ADMIN", "普通管理员"),
    SALESMAN("SALESMAN", "业务员");

    private final String code;
    private final String desc;

    RoleCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static RoleCode fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (RoleCode roleCode : values()) {
            if (roleCode.code.equals(code)) {
                return roleCode;
            }
        }
        return null;
    }
}

