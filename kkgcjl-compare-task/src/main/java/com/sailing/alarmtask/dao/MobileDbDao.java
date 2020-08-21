package com.sailing.alarmtask.dao;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.sailing.alarmtask.config.Config;
import com.sailing.alarmtask.entity.MobileUserTask;
import com.sailing.alarmtask.entity.ZyGzcl;
import com.sailing.alarmtask.entity.ZyGzkk;
import com.sailing.jdbc.dao.BaseDao;
import com.sailing.jdbc.setter.BatchUpdateSetter;

/**
 * 用于关注车辆比对数据库crud
 * 
 * @author sailing
 *
 */
public class MobileDbDao extends BaseDao {
	/**
	 * 获取所有的关注车辆信息缓存起来
	 * <p>
	 * type-->cp=关注车牌 type=布控卡口
	 * 
	 * @return
	 */
	@Cacheable(value = "gcjlCompareCache", key = "'listAllZyGzcl'")
	public List<ZyGzcl> listAllZyGzcl() {
		return this.list(ZyGzcl.class, Config.listZyGzclSql);
	}

	/**
	 * 根据重点人员类型和标准位置代码获取用户配置的关注卡口信息
	 * 
	 * @param rylbdm
	 *            重点人员类型代码
	 * @param bzwzdm
	 *            标准位置代码
	 * @return
	 */
	public List<ZyGzkk> listZyGzkkByRylbdmAndBzwzdm(String rylbdm, String bzwzdm) {
		return this.list(ZyGzkk.class, Config.listZyGzkkByRylbdmAndBzwzdmSql, rylbdm, bzwzdm);
	}

	/**
	 * 批量保持预警信息
	 * 
	 * @param mobileUserTasks
	 *            预警信息
	 */
	public void batchUpdate(List<MobileUserTask> mobileUserTasks) {
		if (mobileUserTasks.size() == 0) {
			return;
		}
		int size = mobileUserTasks.size();
		int batchCount = (size % Config.batchSize == 0 ? size / Config.batchSize : size / Config.batchSize + 1);
		for (int i = 0; i < batchCount; i++) {
			int startIndex = i * Config.batchSize;
			int endIndex = (startIndex + Config.batchSize) > size ? size : (startIndex + Config.batchSize);
			this.batchUpdate(
					new BatchUpdateSetter(Config.batchUpdateSql, mobileUserTasks.subList(startIndex, endIndex)));
		}
	}
}
