package com.ztk.gulimall.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.ztk.gulimall.user.mapper")
public class GulimallUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallUserServiceApplication.class, args);
    }

}
