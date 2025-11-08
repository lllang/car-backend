package org.demo.car.common.enums;

import lombok.Getter;

/**
 * 权益使用状态枚举
 */
@Getter
public enum BenefitStatus {
    UNUSED("UNUSED", "未使用"),
    USED("USED", "已使用");

    private final String code;
    private final String desc;

    BenefitStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static BenefitStatus fromCode(String code) {
        if (code == null) {
            return UNUSED;
        }
        for (BenefitStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return UNUSED;
    }
}

