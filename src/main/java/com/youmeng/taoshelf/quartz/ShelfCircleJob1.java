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
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.*;

public class ShelfCircleJob1 extends QuartzJobBean {

    @Resource
    private TaskService taskService;

    @Resource
    private GoodService goodService;

    @Resource
    private LogService logService;

    private User user;

    private Task task;

    private List<Good> goodList = new ArrayList<>();

    private Map<Long, String> originalStatus = new HashMap<>();

    private int total;

    private int busyCount;

    //任务结束标志
    private boolean end = false;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        try {
            JobDetail jobDetail = jobExecutionContext.getJobDetail();
            JobDataMap jobDataMap = jobDetail.getJobDataMap();
            String task_id = jobDataMap.getString("task_id");
            task = taskService.getTaskById(task_id);
            user = task.getUser();
            task.setStatus("正在读取商品列表");
            taskService.saveTask(task);
            queryGoodList(task);
//            task.setNum(goodList.size());
            logService.log(user, "任务记录", "开始执行上下架操作");
            for (int i = 1; ; i++) {
                task.setStatus("正在执行第" + i + "轮");
                taskService.saveTask(task);
                executeTask(task_id);
                //判断任务是否到期
                task = taskService.getTaskById(task_id);
                if (end) {
                    //结束任务时执行恢复操作
                    logService.log(user, "任务记录", "任务结束时间到");
                    recoveryTask();
                    break;
                }
            }
            task.setStatus("执行完毕" + total + "次");
            taskService.saveTask(task);
        } catch (Exception e) {
            e.printStackTrace();
            task.setEndTime(new Date());
            logService.log(user, "任务记录", "任务发生异常，提前结束，正在检查商品并恢复初始状态");
            recoveryTask();
        }
    }

    //获取商品列表
    private void queryGoodList(Task task) {
        logService.log(user, "任务记录", "开始执行读取商品列表操作");
        String type = task.getType();
        switch (type) {
            case "库存商品上下架": {
                Result<Good> result = goodService.getGoodsInstock(user, null, 200L, null, 1);
                goodList.addAll(result.getItems());
                long num = result.getTotal() / 200 + 2;
                for (long i = 2L; i < num; i++) {
                    Result<Good> newResult = goodService.getGoodsInstock(user, null, 200L, i, 1);
                    goodList.addAll(newResult.getItems());
                }
                break;
            }
            case "在售商品上下架": {
                Result<Good> result = goodService.getGoodsOnsale(user, null, 200L, null, 1);
                goodList.addAll(result.getItems());
                long num = result.getTotal() / 200 + 2;
                for (long i = 2L; i < num; i++) {
                    Result<Good> newResult = goodService.getGoodsOnsale(user, null, 200L, i, 1);
                    goodList.addAll(newResult.getItems());
                }
                break;
            }
        }
        //记录初始状态
        for (Good good : goodList) {
            originalStatus.put(good.getNumIid(), good.getApproveStatus());
        }
    }

    //进行一轮上下架
    private void executeTask(String task_id) {
        for (Good good : goodList) {
            //判断任务是否到期
            task = taskService.findTaskById(task_id);
            if (task.getEndTime().before(new Date())) {
                end = true;
                break;
            }
            executeGood(good);
            rest(busyCount);
            executeGood(good);
            rest(busyCount);
            total++;
        }
    }

    //上架或下架一件
    private void executeGood(Good good) {
        if (good.getApproveStatus().equals("onsale")) {
            if (goodService.doGoodDelisting(user, good, 1)) {
                busyCount = 0;
            } else {
                busyCount++;
            }
        } else if (good.getApproveStatus().equals("instock")) {
            if (goodService.doGoodListing(user, good, 1)) {
                busyCount = 0;
            } else {
                busyCount++;
            }
        }
    }

    //恢复商品原状态
    private void recoveryTask() {
        task.setStatus("正在恢复商品原状态");
        taskService.saveTask(task);
        logService.log(user, "任务记录", "开始执行恢复商品原状态操作");
        for (Good good : goodList) {
            Long numIid = good.getNumIid();
            String s = originalStatus.get(numIid);
            //如果和初始状态不一致
            if (!s.equals(good.getApproveStatus())) {
                //计数
                int count = 0;
                //原始状态
                String origin = good.getApproveStatus();
                while ((count < 3) && origin.equals(good.getApproveStatus())) {
                    if (good.getApproveStatus().equals("onsale")) {
                        if (goodService.doGoodDelisting(user, good, 1)) {
                            busyCount = 0;
                        } else {
                            busyCount++;
                        }
                    } else if (good.getApproveStatus().equals("instock")) {
                        if (goodService.doGoodListing(user, good, 1)) {
                            busyCount = 0;
                        } else {
                            busyCount++;
                        }
                    }
                    rest(busyCount);
                    count++;
                }
                if (origin.equals(good.getApproveStatus())) {
                    logService.log(user, "恢复商品原状态失败", good.getTitle());
                } else {
                    logService.log(user, "恢复商品原状态成功", good.getTitle());
                }
            }
        }
    }

    //休息
    private void rest(int count) {
        long sleepTime = 30L;
        if (count == 0) {
        } else if (count == 1) {
            sleepTime = 500L;
        } else if (count == 2) {
            sleepTime = 1000L;
        } else if (count == 3) {
            sleepTime = 2000L;
        } else if (count == 4) {
            sleepTime = 3000L;
        } else {
            sleepTime = 5000L;
        }
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
