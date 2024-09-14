package net.xdclass.config;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置器
 *
 * @author tangcj
 * @date 2024/09/14 16:28
 **/
@Configuration
@Slf4j
public class InterceptorConfig implements WebMvcConfigurer {

    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                //拦截的路径
                .addPathPatterns("/api/user/*/**", "/api/address/*/**")
                //排查不拦截的路径
                .excludePathPatterns("/api/user/*/send_code", "/api/user/*/captcha",
                        "/api/user/*/register", "/api/user/*/login", "/api/user/*/upload");
    }
}
