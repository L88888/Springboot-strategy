package com.sailing.deviceasync.controller.healthcheck;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Comsys-LIULEI
 * @version V1.0
 * @Title: 给Conusl使用的,提供应用服务自身的健康检测.该类建议保留
 * @Package sailing-server-analysis-platform
 * @Description: HealthCheck
 * Copyright: Copyright (c) 2011
 * Company:上海熙菱信息技术有限公司
 * @date 2018/1/12 15:21:44下午
 */
@RestController(value = "/HealthCheck")
@RequestMapping("/sys/")
public class HealthCheck {

    /**
     * 给Conusl使用的,提供应用服务自身的健康检测.
     * http://127.0.0.1:2020/sys/Serf/Health/Status/Check
     * @return
     */
    @RequestMapping(value = "Serf/Health/Status/Check",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map check() {
        Map map = new HashMap();
        map.put("success", "true");
        map.put("message", "OK");
        return map;
    }

    /**
     * 给Conusl使用的,提供应用服务自身配置数据
     * http://127.0.0.1:1000/sys/configData
     * @return
     */
    @RequestMapping(value = "configData",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map configData() {
        Map map = new HashMap();
        map.put("success", "true");
        map.put("message", "OK");
        return map;
    }
}
