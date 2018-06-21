package com.example.service.impl;

import com.example.service.IScheduleService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author SongQingWei
 * @date 2018年05月15 16:38
 */
@Service(value = "iScheduleService")
public class ScheduleServiceImpl implements IScheduleService {

    /**
     * 没10秒执行一次
     * 【0 0/5 14,18 * * ?】每天14点整和18点整，每隔5分钟执行一次
     * 【0 15 10 ? * 1-6】每个月的周一至周六10点15分执行一次
     * 【0 0 2 ? * * 6L】每月这、ui后一周的周六凌晨2点执行一次
     * 【0 0 2-4 ? * 2#1】每月第一周的周二凌晨2点到4点，每个整点执行一次
     */
    @Scheduled(cron = "0/10 * * * * *")
    @Override
    public void hello() {
        System.out.println("world...");
    }
}
