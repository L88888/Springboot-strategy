package com.sailing.dataextraction.controller;

import com.sailing.dataextraction.service.DataFileAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController(value = "/HealthCheck")
@RequestMapping("/sys")
public class HealthCheck {

    @Autowired
    private DataFileAnalysis dataFileAnalysis;

    /**
     * 提供应用服务自身的健康检测.
     * http://127.0.0.1:2020/sys/Serf/Health/Status/Check
     * @return
     */
    @GetMapping(path = "/Serf/Health/Status/Check", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map check() {
        Map<String, String> map = new HashMap<>(2);
        map.put("success", "true");
        map.put("message", "OK");
        return map;
    }

    /**
     * 手动启动任务
     * @return
     */
    @GetMapping(path = "/startJobTask", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map startJobTask(){
        dataFileAnalysis.getFileData();
        Map<String, String> map = new HashMap<>(2);
        map.put("success", "true");
        map.put("message", "OK");
        return map;
    }
}
