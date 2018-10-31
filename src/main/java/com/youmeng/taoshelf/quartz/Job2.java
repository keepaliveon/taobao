package com.youmeng.taoshelf.quartz;

import com.youmeng.taoshelf.entity.Good;
import com.youmeng.taoshelf.entity.Result;
import com.youmeng.taoshelf.entity.Task;
import com.youmeng.taoshelf.entity.User;
import com.youmeng.taoshelf.service.GoodService;
import com.youmeng.taoshelf.service.LogService;
import com.youmeng.taoshelf.service.TaskService;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.*;

public class Job2 extends QuartzJobBean {

    @Resource
    private TaskService taskService;

    @Resource
    private GoodService goodService;

    @Resource
    private LogService logService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private User user;

    private String taskId;

    private Task task;

    private long totalNum;

    private List<Good> goodList1 = new ArrayList<>();

    private List<Good> goodList2 = new ArrayList<>();

    private Map<Long, String> originalStatus1 = new HashMap<>();

    private Map<Long, String> originalStatus2 = new HashMap<>();

    private int busyCount1;

    private int busyCount2;

    private boolean end;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        //从context中获取task_id、user
        taskId = jobDataMap.getString("task_id");
        redisTemplate.opsForValue().set(taskId, "0");
        task = taskService.getTaskById(taskId);
        user = task.getUser();
        initTask();
    }

    //初始化任务
    private void initTask() {
        end = false;
        logService.log(user, task.getDescription() + "任务进度", "任务开始");
        readTotalNum(task.getType());
        task.setNum(totalNum);
        task.setStatus("正在读取商品列表");
        taskService.saveTask(task);
        task = taskService.getTaskById(taskId);
        String s1 = "1-" + totalNum / 400;
        String s2 = totalNum / 400 + 1 + "-" + (totalNum / 200 + 1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    readGoodList(goodList2, s2, task.getType(), 2);
                    logService.log(user, task.getDescription() + "任务进度", "子任务2开始执行");
                    while (!end) {
                        executeGoodList(goodList2, 2);
                    }
                    recoveryGoodList(goodList2, 2);
                    logService.log(user, task.getDescription() + "任务进度", "子任务2完成");
                } catch (Exception e) {
                    logService.log(user, task.getDescription() + "任务进度", "子任务2异常中止");
                }
            }
        }).start();
        try {
            readGoodList(goodList1, s1, task.getType(), 1);
            logService.log(user, task.getDescription() + "任务进度", "子任务1开始执行");
            while (!end) {
                executeGoodList(goodList1, 1);
            }
            recoveryGoodList(goodList1, 1);
            logService.log(user, task.getDescription() + "任务进度", "子任务1完成");
        } catch (Exception e) {
            e.printStackTrace();
            logService.log(user, task.getDescription() + "任务进度", "子任务1异常中止");
        }
        task.setStatus("任务结束(成功处理" + redisTemplate.opsForValue().get(taskId) + "次)");
        taskService.saveTask(task);
        redisTemplate.delete(taskId);
    }

    //读总数(单线程)
    private void readTotalNum(String type) {
        switch (type) {
            case "库存商品上下架": {
                Result<Good> result = goodService.getGoodsInstock(user, null, 5L, 1L, 1);
                totalNum = result.getTotal();
                break;
            }
            case "在售商品上下架": {
                Result<Good> result = goodService.getGoodsOnsale(user, null, 5L, 1L, 1);
                totalNum = result.getTotal();
                break;
            }
        }
    }

    //读列表(双线程)
    private void readGoodList(List<Good> goodList, String page, String type, int flag) {
        String[] split = page.split("-");
        long start = Long.parseLong(split[0]);
        long end = Long.parseLong(split[1]);
        switch (type) {
            case "库存商品上下架": {
                for (long i = start; i <= end; i++) {
                    Result<Good> result = goodService.getGoodsInstock(user, null, 200L, i, flag);
                    goodList.addAll(result.getItems());
                }
                break;
            }
            case "在售商品上下架": {
                for (long i = start; i <= end; i++) {
                    Result<Good> result = goodService.getGoodsOnsale(user, null, 200L, i, flag);
                    goodList.addAll(result.getItems());
                }
                break;
            }
        }
        if (flag == 1) {
            for (Good good : goodList) {
                originalStatus1.put(good.getNumIid(), good.getApproveStatus());
            }
        } else if (flag == 2) {
            for (Good good : goodList) {
                originalStatus2.put(good.getNumIid(), good.getApproveStatus());
            }
        }
        logService.log(user, task.getDescription() + "任务进度", "子任务" + flag + "分配" + goodList.size() + "件");
        task.setStatus("正在执行任务");
        taskService.saveTask(task);
        task = taskService.getTaskById(taskId);
    }

    //处理商品列表
    private void executeGoodList(List<Good> goodList, int flag) {
        for (Good good : goodList) {
            task = taskService.findTaskById(taskId);
            if (task.getEndTime().before(new Date())) {
                end = true;
                break;
            }
            if (flag == 1) {
                rest(busyCount1);
            } else {
                rest(busyCount2);
            }
            if (executeGood(good, flag)) {
                if (flag == 1) {
                    rest(busyCount1);
                } else {
                    rest(busyCount2);
                }
                if (executeGood(good, flag)) {
                    redisTemplate.opsForValue().increment(taskId, 1);
                    System.out.println(user.getNick() + ":" + flag + " " + "[SUCCESS]" + good.getTitle());
                }
            } else {
                if (flag == 1) {
                    goodList1.add(good);
                } else {
                    rest(busyCount2);
                }
            }
        }
    }

    //处理一件商品
    private boolean executeGood(Good good, int flag) {
        if (good.getApproveStatus().equals("onsale")) {
            if (goodService.doGoodDelisting(user, good, flag)) {
                if (flag == 1) {
                    busyCount1 = 0;
                    return true;
                } else {
                    busyCount2 = 0;
                    return true;
                }
            } else {
                if (flag == 1) {
                    busyCount1++;
                    return false;
                } else {
                    busyCount2++;
                    return false;
                }
            }
        } else if (good.getApproveStatus().equals("instock")) {
            if (goodService.doGoodListing(user, good, flag)) {
                if (flag == 1) {
                    busyCount1 = 0;
                    return true;
                } else {
                    busyCount2 = 0;
                    return true;
                }
            } else {
                if (flag == 1) {
                    busyCount1++;
                    return false;
                } else {
                    busyCount2++;
                    return false;
                }
            }
        }
        return false;
    }

    //恢复商品列表
    private void recoveryGoodList(List<Good> goodList, int flag) {
        logService.log(user, task.getDescription() + "任务进度", "开始恢复子任务" + flag);
        Map<Long, String> originalStatus;
        if (flag == 1) {
            originalStatus = this.originalStatus1;
        } else {
            originalStatus = this.originalStatus2;
        }
        for (Good good : goodList) {
            String origin = originalStatus.get(good.getNumIid());
            if (!origin.equals(good.getApproveStatus())) {
                int count = 0;
                while (count < 2 && !origin.equals(good.getApproveStatus())) {
                    if (good.getApproveStatus().equals("onsale")) {
                        if (goodService.doGoodDelisting(user, good, flag)) {
                            if (flag == 1) {
                                busyCount1 = 0;
                            } else {
                                busyCount2 = 0;
                            }
                        } else {
                            if (flag == 1) {
                                busyCount1++;
                            } else {
                                busyCount2++;
                            }
                        }
                    } else if (good.getApproveStatus().equals("instock")) {
                        if (goodService.doGoodListing(user, good, flag)) {
                            if (flag == 1) {
                                busyCount1 = 0;
                            } else {
                                busyCount2 = 0;
                            }
                        } else {
                            if (flag == 1) {
                                busyCount1++;
                            } else {
                                busyCount2++;
                            }
                        }
                    }
                    if (flag == 1) {
                        rest(busyCount1);
                    } else {
                        rest(busyCount2);
                    }
                    count++;
                }
                if (origin.equals(good.getApproveStatus())) {
                    logService.log(user, "恢复商品原状态成功", good.getTitle());
                } else {
                    logService.log(user, "恢复商品原状态失败", good.getTitle());
                }
            }
        }
    }

    //休息
    private void rest(int count) {
        long sleepTime = 100L;
        if (count == 0) {
        } else if (count == 1) {
            sleepTime = 1200L;
        } else if (count == 2) {
            sleepTime = 1600L;
        } else if (count == 3) {
            sleepTime = 2100L;
        } else if (count == 4) {
            sleepTime = 3000L;
        } else {
            sleepTime = 3500L;
        }
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
