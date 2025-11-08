package org.demo.car.common.enums;

import lombok.Getter;

/**
 * 权益类型枚举
 */
@Getter
public enum BenefitType {
    PARTNER("PARTNER", "合作商"),
    OWN("OWN", "自有"),
    DEALER("DEALER", "经销商");

    private final String code;
    private final String desc;

    BenefitType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static BenefitType fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (BenefitType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}

