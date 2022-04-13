package com.qq.blogapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("com.qq.blogapi.dao.mapper")
@SpringBootApplication
public class BlogApiApplication {

    public static void main(String[] args) {

        SpringApplication.run(BlogApiApplication.class, args);
    }

}
