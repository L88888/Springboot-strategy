package com.sailing.alarmtask.entity;

import java.io.Serializable;

import lombok.Data;
/**
 * 关注车辆信息
 * @author sailing
 *
 */
@Data
public class ZyGzcl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 关注用户机构ID
	 */
	private String org_id;
	/**
	 * 关注用户ID
	 */
	private String user_id;
	/**
	 * 关注车牌
	 */
	private String gzcp;
	/**
	 * 车牌种类
	 */
	private String cpzl;
	/**
	 * 配置ID
	 */
	private String config_id;
}
