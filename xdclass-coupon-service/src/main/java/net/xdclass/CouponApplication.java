package net.xdclass;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 优惠劵服务启动类
 *
 * @author tangcj
 * @date 2024/09/07 19:07
 **/
@SpringBootApplication
@MapperScan("net.xdclass.mapper")
public class CouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(CouponApplication.class, args);
    }
}
