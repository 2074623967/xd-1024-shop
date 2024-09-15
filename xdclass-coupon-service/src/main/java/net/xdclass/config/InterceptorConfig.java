package net.xdclass.config;

import net.xdclass.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 *
 * @author tangcj
 * @date 2024/09/15 10:38
 **/
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/api/coupon_record/*/**")
                .addPathPatterns("/api/coupon/*/**")
                //不拦截的路径
                .excludePathPatterns("/api/coupon/*/page_coupon");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
