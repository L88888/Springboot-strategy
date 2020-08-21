package com.sailing.alarmtask.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
/**
 * 从kafka消费出来的卡口过车记录信息
 * @author sailing
 *
 */
@Data
public class ZyKkgcjl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 经过时间
	 */
	private Date jgsj;
	/**
	 * 行政区划
	 */
	private String xzqh;
	/**
	 * 记录编号
	 */
	private String jlbh;
	/**
	 * 标准位置代码
	 */
	private String bzwzdm;
	/**
	 * 号牌号码
	 */
	private String hphm;
	/**
	 * 号牌种类
	 */
	private String hpzl;
	
}
