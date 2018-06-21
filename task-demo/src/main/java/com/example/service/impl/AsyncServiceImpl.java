package com.example.service.impl;

import com.example.service.IAsyncService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步任务
 * @author SongQingWei
 * @date 2018年05月15 15:38
 */
@Service(value = "iAsyncService")
public class AsyncServiceImpl implements IAsyncService {

    @Async
    @Override
    public void hello() {
        System.out.println("处理数据中...");
    }
}
