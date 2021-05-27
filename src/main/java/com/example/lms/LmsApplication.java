package com.example.lms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 王子昂
 * @date 5/21/21
 * @decription 启动类
 */
@SpringBootApplication
@EnableCaching
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
@tk.mybatis.spring.annotation.MapperScan("com.example.lms.dao")
public class LmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LmsApplication.class, args);
    }

}
