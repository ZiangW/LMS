package com.example.lms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

@SpringBootTest
class LmsApplicationTests {

    @Autowired
    DataSource dataSource;

    @Test
    void contextLoads() throws Exception {
        System.out.println("数据库的连接为：" + dataSource.getConnection());
    }

}
