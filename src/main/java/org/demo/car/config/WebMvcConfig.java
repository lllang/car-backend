package org.demo.car.config;

import lombok.RequiredArgsConstructor;
import org.demo.car.security.PermissionInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final PermissionInterceptor permissionInterceptor;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置文件访问路径映射
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + uploadPath);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加权限拦截器
        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/",
                        "/health",
                        "/user/login",
                        "/admin/login",
                        "/common/**",
                        "/files/**",
                        "/error"
                );
    }
}

