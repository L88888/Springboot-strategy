package com.sailing.dataextraction.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className JobManager
 * @description 任务管理
 * @date 2020/8/20 9:46
 **/

@Slf4j
@Service
public class JobManager {

    @Autowired
    private JobTask jobTask;

    @PostConstruct
    public void jobManager(){
        log.info("启动所有数据抽取任务");
        jobTask.populationJob();
        jobTask.trainJob();
        jobTask.flightJob();
        jobTask.busJob();
        jobTask.hotelJob();
        jobTask.internetJob();
    }
}
