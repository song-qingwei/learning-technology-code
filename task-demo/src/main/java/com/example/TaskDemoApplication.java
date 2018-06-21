package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 * @author SongQingWei
 * @date 2018年05月07 16:35
 * EnableAsync 开启异步调用
 * EnableScheduling 开启定时任务
 */
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class TaskDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskDemoApplication.class, args);
    }
}
