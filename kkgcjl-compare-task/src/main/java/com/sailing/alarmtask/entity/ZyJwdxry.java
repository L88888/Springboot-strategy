package com.sailing.alarmtask.entity;

import lombok.Data;

/**
 * 警务对象人员信息
 * 
 * @author sailing
 *
 */
@Data
public class ZyJwdxry {
	// 姓名
	private String xm;
	// 身份证号码
	private String gmsfhm;
	// 人员类别代码
	private String rylbdm;
	// 人员类别名称
	private String rylb;
	// 类别代码
	private String lbdm;
	// 车辆号牌
	private String clhp;
	// 车辆类型
	private String cllx;
}
