package com.ztk.gulimall.user;

import com.ztk.gulimall.user.mapper.UserMapper;
//import org.mybatis.spring.annotation.MapperScan;//不用通用mapper时用的扫描器
import tk.mybatis.spring.annotation.MapperScan;//通用mapper的扫描器，用于代替mybatis的扫描器
//import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan(basePackages = "com.ztk.gulimall.user.mapper")
public class GulimallUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallUserApplication.class, args);
    }

}
