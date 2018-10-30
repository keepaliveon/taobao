package com.youmeng.taoshelf;

import com.youmeng.taoshelf.quartz.TestJob;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuartzTest {

    @Resource
    private Scheduler scheduler;

    @Test
    public void test1() throws SchedulerException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        String group = "QUARTZ";
        Date date = format.parse("2018-10-23 15:30:00");
        for (int i = 0; i < 2; i++) {
            System.out.println("创建" + i + "个任务");
            JobKey key = JobKey.jobKey("test_" + i, group);
            scheduler.deleteJob(key);
            //创建任务
            JobDetail jobDetail = JobBuilder
                    .newJob(TestJob.class)
                    .withIdentity("test_" + i, group)
                    .build();
            //创建任务触发器
            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("test_" + i, group)
                    .withSchedule(simpleSchedule().repeatForever().withIntervalInSeconds(1))
                    .startAt(new Date())
                    .endAt(date)
                    .build();
            //将触发器与任务绑定到调度器内
            scheduler.scheduleJob(jobDetail, trigger);
        }
    }
}
