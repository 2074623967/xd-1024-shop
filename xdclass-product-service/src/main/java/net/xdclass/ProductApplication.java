package net.xdclass;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 优惠劵服务启动类
 *
 * @author tangcj
 * @date 2024/09/07 19:07
 **/
@EnableTransactionManagement
@SpringBootApplication
@MapperScan("net.xdclass.mapper")
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}
