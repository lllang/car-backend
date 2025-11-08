package org.demo.car.common.enums;

import lombok.Getter;

/**
 * 估价状态枚举
 */
@Getter
public enum AppraisalStatus {
    PENDING("PENDING", "未跟进"),
    FOLLOWING("FOLLOWING", "已跟进"),
    COMPLETED("COMPLETED", "已完成");

    private final String code;
    private final String desc;

    AppraisalStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static AppraisalStatus fromCode(String code) {
        if (code == null) {
            return PENDING;
        }
        for (AppraisalStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return PENDING;
    }
}

