package com.sailing.alarmtask.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 系统配置信息，包括常量和sql配置
 * 
 * @author sailing
 *
 */
@Configuration
public class Config {
	//#批量提交条数
	public static int batchSize = 1000;
	// 查询所有关注车辆信息
	public static String listZyGzclSql;
	// 查询所有关注车辆信息
	public static String listZyGzkkSql;
	// MOBILE_USER_TASK插入语句
	public static String batchUpdateSql;
	// 查询标准位置
	public static String bqjSbwzByBzwzdm;
	//根据重点人员类型和标准位置代码查询关注的卡口
	public static String listZyGzkkByRylbdmAndBzwzdmSql;
	//查询车辆的车主是否是重点人员信息
	public static String jwdxryByClhpAndCllx;
	// 比对类型-卡口
	public static final String type_kk = "kk";
	// 比对类型-卡点
	public static final String type_kd = "kd";
	// 比对类型-关注车辆
	public static final String type_cp = "cp";
	// 是否发送发送到警务终端 0-不发送1-发送
	public static final String defaul_send = "0";
	// 发送方式 1-消息推送,2发生短信
	public static final String defaul_send_type = "1";
	// MobileUserTask.state状态
	public static final String defaul_state = "1";
	
	@Value("${MobileDbDao.listZyGzcl.sql}")
	public void setListZyGzclSql(String listZyGzclSql) {
		Config.listZyGzclSql = listZyGzclSql;
	}
	
	@Value("${MobileDbDao.batchUpdate.sql}")
	public void setBatchUpdateSql(String batchUpdateSql) {
		Config.batchUpdateSql = batchUpdateSql;
	}
	
	@Value("${ClypDbDao.bqjSbwzByBzwzdm.sql}")
	public void setBqjSbwzByBzwzdm(String bqjSbwzByBzwzdm) {
		Config.bqjSbwzByBzwzdm = bqjSbwzByBzwzdm;
	}
	
	@Value("${MobileDbDao.listZyGzkkByRylbdmAndBzwzdm.sql}")
	public void setListZyGzkkByRylbdmAndBzwzdmSql(String listZyGzkkByRylbdmAndBzwzdmSql) {
		Config.listZyGzkkByRylbdmAndBzwzdmSql = listZyGzkkByRylbdmAndBzwzdmSql;
	}

	@Value("${ClypDbDao.jwdxryByClhpAndCllx.sql}")
	public void setJwdxryByClhpAndCllx(String jwdxryByClhpAndCllx) {
		Config.jwdxryByClhpAndCllx = jwdxryByClhpAndCllx;
	}
	
	@Value("${sql.update.batch-size}")
	public void setBatchSize(int batchSize) {
		Config.batchSize = batchSize;
	}
	
	
}
