package com.example.swagger2demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author SongQingWei
 * @date 2018年07月13 10:08
 * 启动类
 * EnableSwagger2：表示启动 swagger2
 */
@SpringBootApplication
@EnableSwagger2
public class Swagger2DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(Swagger2DemoApplication.class, args);
    }
}
