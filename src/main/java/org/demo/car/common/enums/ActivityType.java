package org.demo.car.common.enums;

import lombok.Getter;

/**
 * 活动类型枚举
 */
@Getter
public enum ActivityType {
    LIMITED_TIME("LIMITED_TIME", "限时优惠"),
    EVENT("EVENT", "活动中心");

    private final String code;
    private final String desc;

    ActivityType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ActivityType fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (ActivityType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}

