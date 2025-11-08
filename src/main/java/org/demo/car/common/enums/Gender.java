package org.demo.car.common.enums;

import lombok.Getter;

/**
 * 性别枚举
 */
@Getter
public enum Gender {
    UNKNOWN(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女");

    private final Integer code;
    private final String desc;

    Gender(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static Gender fromCode(Integer code) {
        if (code == null) {
            return UNKNOWN;
        }
        for (Gender gender : values()) {
            if (gender.code.equals(code)) {
                return gender;
            }
        }
        return UNKNOWN;
    }
}

