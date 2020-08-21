package com.sailing.alarmtask.entity;

import lombok.Data;

/**
 * 关注卡口
 * @author sailing
 *
 */
@Data
public class ZyGzkk {
	//用户ID
	private String user_id;
	//y用户组织ID
	private String org_id;
	//用户配置ID
	private String config_id;
	//标准位置代码
	private String bzwzdm;
}
