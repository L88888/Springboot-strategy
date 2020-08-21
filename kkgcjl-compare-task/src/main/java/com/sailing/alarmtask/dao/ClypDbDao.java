package com.sailing.alarmtask.dao;

import java.util.List;

import com.sailing.alarmtask.config.Config;
import com.sailing.alarmtask.entity.ZyBzwz;
import com.sailing.alarmtask.entity.ZyJwdxry;
import com.sailing.jdbc.dao.BaseDao;

/**
 * 用于关注车辆比对数据库crud
 * 
 * @author sailing
 *
 */
public class ClypDbDao extends BaseDao {

	/**
	 * 根据标准位置代码获取位置信息
	 * 
	 * @param bzwzdm
	 *            标准位置代码
	 * @return
	 */
	public ZyBzwz bqjSbwzByBzwzdm(String bzwzdm) {
		return this.query(ZyBzwz.class, Config.bqjSbwzByBzwzdm, bzwzdm);
	}

	/**
	 * 根据车辆号码和车牌类型获取警务对象车主
	 * 
	 * @param clhp
	 *            车辆号码
	 * @param cllx
	 *            车辆类型
	 * @return
	 */
	public List<ZyJwdxry> listByClhpAndCllx(String clhp, String cllx) {
		return this.list(ZyJwdxry.class, Config.jwdxryByClhpAndCllx, clhp, cllx);
	}
	
}
