package com.sailing.alarmtask.entity;

import java.io.Serializable;

import lombok.Data;
/**
 * 标准位置信息
 * @author sailing
 *
 */
@Data
public class ZyBzwz implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 关注用户机构ID
	 */
	private String wzmc;
	/**
	 * 上级区域
	 */
	private String ssqy;
	/**
	 * 标准位置代码
	 */
	private String bzwzdm;
	
}
