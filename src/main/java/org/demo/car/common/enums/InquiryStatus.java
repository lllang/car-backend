package org.demo.car.common.enums;

import lombok.Getter;

/**
 * 询价状态枚举
 */
@Getter
public enum InquiryStatus {
    PENDING("PENDING", "待处理"),
    CONTACTED("CONTACTED", "已联系");

    private final String code;
    private final String desc;

    InquiryStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static InquiryStatus fromCode(String code) {
        if (code == null) {
            return PENDING;
        }
        for (InquiryStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return PENDING;
    }
}

