package com.sailing.deviceasync.dao;

import java.util.List;
import java.util.Map;

import com.sailing.deviceasync.config.Config;
import com.sailing.deviceasync.entity.DeviceInfo;
import com.sailing.deviceasync.entity.DeviceInfoAttribute;
import com.sailing.jdbc.dao.BaseDao;
import com.sailing.jdbc.setter.BatchUpdateSetter;

/**
 * 用于新视综数据库crud
 * 
 * @author sailing
 *
 */
public class XszDbDao extends BaseDao {
	/**
	 * 根据设备编号判断设备是否已存在
	 * @param device_id 设备编号
	 * @return
	 */
	public boolean exists(String sql, String device_id) {
		List<Map<String, Object>> listMap = this.listMap(sql, device_id);
		return listMap != null && listMap.size() > 0;
	}
	
	/**
	 * 批量插入设备信息
	 * @param deviceInfos
	 */
	public void batchInsert(List<DeviceInfo> deviceInfos) {
		this.batchUpdate(new BatchUpdateSetter(Config.xszDbDaoBatchInsertSql, deviceInfos));
	}

	/**
	 * 批量插入设备属性信息
	 * @param deviceInfoAttributes
	 */
	public void batchInsertDeviceAttribute(List<DeviceInfoAttribute> deviceInfoAttributes) {
		this.batchUpdate(new BatchUpdateSetter(Config.xszDbDaoBIDeviceAttributeSql, deviceInfoAttributes));
	}

	/**
	 * 批量更新设备信息
	 * @param deviceInfos
	 */
	public void batchUpdate(List<DeviceInfo> deviceInfos) {
		this.batchUpdate(new BatchUpdateSetter(Config.xszDbDaoBatchUpdateSql, deviceInfos));
	}

	/**
	 * 批量更新设备属性信息
	 * @param deviceInfoAttributes
	 */
	public void batchUpdateDeviceAttribute(List<DeviceInfoAttribute> deviceInfoAttributes) {
		this.batchUpdate(new BatchUpdateSetter(Config.xszDbDaoBUDeviceAttributeSql, deviceInfoAttributes));
	}
}
