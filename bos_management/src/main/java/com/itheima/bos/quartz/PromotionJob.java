package com.itheima.bos.quartz;

import com.itheima.bos.service.base.IPromotionService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class PromotionJob implements Job {

    @Autowired
    private IPromotionService promotionService;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("活动定时过期功能已执行");
        promotionService.updateStatus(new Date());
    }
}
