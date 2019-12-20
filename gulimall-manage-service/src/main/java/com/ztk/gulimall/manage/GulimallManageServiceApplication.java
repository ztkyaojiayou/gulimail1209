package com.ztk.gulimall.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.ztk.gulimall.manage.mapper")
public class GulimallManageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallManageServiceApplication.class, args);
    }

}
