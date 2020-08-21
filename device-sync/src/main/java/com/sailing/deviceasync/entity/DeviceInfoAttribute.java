package com.sailing.deviceasync.entity;

import lombok.Data;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className DeviceInfoAttribute
 * @description TODO
 * @date 2020/3/18 16:27
 **/
@Data
public class DeviceInfoAttribute {

    private String device_id;

    private String pole_code;

    private String secret_attribute;

    private String file_path;

    private String point_name;
}
