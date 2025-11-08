package org.demo.car.dto.request;

import lombok.Data;

/**
 * 分页请求基类
 */
@Data
public class PageRequest {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    
    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }
}

