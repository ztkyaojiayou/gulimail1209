package com.ztk.gulimall.conf;

import com.ztk.gulimall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//spring整合redis的配置类
@Configuration
public class RedisConfig {
    //读取配置文件中的redis的ip地址（具体的值在manage-service的resources-application.properties中
    // (不能放在service-util模块中，因为这不是一个springboot项目，而只是一个普通的maven项目，项目启动时并不能被读取到。每个应用工程引入service-util后，单独配置自己的redis的配置文件；
    //而Service-util的配置文件并不会起作用）
    @Value("${spring.redis.host:disabled}")
    private String host;
    @Value("${spring.redis.port:0}")
    private int port ;
    @Value("${spring.redis.database:0}")
    private int database;

    @Bean
    public RedisUtil getRedisUtil(){
        if(host.equals("disabled")){
            return null;
        }
        RedisUtil redisUtil=new RedisUtil();
        redisUtil.initPool(host,port,database);
        return redisUtil;
    }
}
