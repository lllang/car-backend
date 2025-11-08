package org.demo.car.common.enums;

import lombok.Getter;

/**
 * 权限类型枚举
 */
@Getter
public enum PermissionType {
    MENU("MENU", "菜单"),
    BUTTON("BUTTON", "按钮");

    private final String code;
    private final String desc;

    PermissionType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PermissionType fromCode(String code) {
        if (code == null) {
            return MENU;
        }
        for (PermissionType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return MENU;
    }
}

