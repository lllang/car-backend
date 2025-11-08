package org.demo.car.security;

import java.lang.annotation.*;

/**
 * 权限控制注解
 * 
 * 用法示例：
 * @RequirePermission("brand:create")
 * public Result<Long> createBrand(@RequestBody BrandRequest request) { ... }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {
    
    /**
     * 权限编码，如：brand:create, brand:update
     */
    String value();
    
    /**
     * 权限描述
     */
    String desc() default "";
}

