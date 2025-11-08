package org.demo.car.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security配置
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // 禁用CSRF（API场景）
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // 启用CORS
            .authorizeHttpRequests(auth -> auth
                // 公开接口
                .requestMatchers(
                    "/user/login",
                    "/user/wechat-login",
                    "/admin/login",
                    "/admin/logout",
                    "/common/**",
                    "/files/**",
                    "/health"
                ).permitAll()
                
                // C端接口需要USER角色
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                
                // 管理端接口需要ADMIN角色
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // 其他请求需要认证
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .maximumSessions(1)  // 最多一个会话
                .maxSessionsPreventsLogin(false)  // 新登录会踢掉旧会话
            )
            .logout(logout -> logout
                .logoutUrl("/admin/logout")
                .invalidateHttpSession(true)
                .deleteCookies("CAR_SESSION")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(200);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":200,\"message\":\"登出成功\"}");
                })
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(401);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":401,\"message\":\"未登录或登录已过期\"}");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(403);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":403,\"message\":\"权限不足\"}");
                })
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

