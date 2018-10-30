package com.youmeng.taoshelf.quartz;

import com.youmeng.taoshelf.entity.Card;
import com.youmeng.taoshelf.service.CardService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestJob extends QuartzJobBean {

    @Resource
    private CardService cardService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext){

        SimpleDateFormat format = new SimpleDateFormat();
        System.out.println("开始" + Thread.currentThread().getName() + "-" + format.format(new Date()));
        try {
            List<Card> allCard = cardService.getAllCard();
            for (Card card : allCard) {
                System.out.println(card);
            }
            throw new JobExecutionException();
        } catch (JobExecutionException e) {
            System.out.println("任务异常中止");
        }

        try {
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("结束与" + Thread.currentThread().getName() + "-" + format.format(new Date()));
    }
}
