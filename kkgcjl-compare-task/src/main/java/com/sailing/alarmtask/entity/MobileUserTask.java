package com.sailing.alarmtask.entity;

import java.util.Date;

import com.sailing.alarmtask.config.Config;

import lombok.Data;

/**
 * 比对命中后的预警数据
 * 
 * @author sailing
 *
 */
@Data
public class MobileUserTask {
	// 主键ID
	private String id;
	// 用户ID
	private String user_id;
	// 消息内容
	private String xxnr;
	// 是否发送发送到警务终端 0-不发送1-发送 -根据聂云龙业务指导此处固定值
	private String send = Config.defaul_send;
	// 发送方式 1-消息推送,2发生短信 -根据聂云龙业务指导此处固定值
	private String send_type = Config.defaul_send_type;

	// 机构外键
	private String org_id;
	// 插入时间
	private Date crsj = new Date();
	// 发送时间
	private Date fssj;
	// 消息类型(kk:卡口,kd:卡点,cp:车牌)
	private String type_;
	// 消息状态
	private String state = Config.defaul_state;
	// 命中号牌号码
	private String hithphm;
	// 命中号牌种类
	private String hithpzl;
	// 命中标准位置代码
	private String hitbzwzdm;
	// 命中设备编号
	private String hitsbbh;
	// 命中设备名称
	private String hitsbmc;
	// 命中关注人员姓名
	private String hitgzryxm;
	// 命中关注人员身份证号码
	private String hitgzrysfzhm;
	// 命中关注人员类型
	private String hitgzrylx;
	// 命中记录编号
	private String hitjlbh;
	// 经过时间
	private Date jgsj;
	// 命中时间
	private Date mzsj = new Date();
	// 配置ID
	private String configid;
}
