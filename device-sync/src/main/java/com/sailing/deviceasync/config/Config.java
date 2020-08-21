package com.sailing.deviceasync.config;

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
	/**
	 * 分页查询数据，每页数据大小
	 */
	public static int pageSize = 2000;
	/**
	 * 是否在启动程序时更新设备基础信息
	 */
	public static boolean update_device_base_info_on_start = false;
	/**
	 * 是否全部更新设备信息
	 */
	public static boolean updateOrInsterDeviceInfoAll = false;
	/**
	 * 查询设备记录是否存在SQL-新视综数据库
	 */
	public static String xszDbDaoDeviceInfoRecordSql;
	/**
	 * 查询设备属性记录是否存在SQL-新视综数据库
	 */
	public static String xszDbDaoDeviceAttributeRecordSql;
	/**
	 * 获取所有设备信息-一机一档数据库
	 */
	public static String yjydDbDaoFindAllSql;
	/**
	 * 根据时间段获取所有设备信息-一机一档数据库
	 */
	public static String yjydDbDaoFindAllByUpdateDateSql;
	/**
	 * 批量插入sql-新视综数据库
	 */
	public static String xszDbDaoBatchInsertSql;
	/**
	 * 批量更新sql-新视综数据库
	 */
	public static String xszDbDaoBatchUpdateSql;

	/**
	 * 查询全部设备属性信息SQL-一机一档数据库
	 */
	public static String yjydDbFindAllDeviceAttributeSql;

	/**
	 * 根据时间段查询全部设备属性信息SQL-一机一档数据库
	 */
	public static String yjydDbFindAllDeviceAttributeDateSql;

	/**
	 * 批量插入设备属性SQL-新视综数据库
	 */
	public static String xszDbDaoBIDeviceAttributeSql;

	/**
	 * 批量更新设备属性SQL-新视综数据库
	 */
	public static String xszDbDaoBUDeviceAttributeSql;

	@Value("${XszDbDao.deviceInfoRecord.sql}")
	public void setXszDbDaoDeviceInfoRecordSql(String xszDbDaoDeviceInfoRecordSql){
		Config.xszDbDaoDeviceInfoRecordSql = xszDbDaoDeviceInfoRecordSql;
	}

	@Value("${XszDbDao.deviceAttributeRecord.sql}")
	public void setXszDbDaoDeviceAttributeRecordSql(String xszDbDaoDeviceAttributeRecordSql){
		Config.xszDbDaoDeviceAttributeRecordSql = xszDbDaoDeviceAttributeRecordSql;
	}

	@Value("${YjydDbDao.findAll.sql}")
	public void setYjydDbDaoFindAllSql(String yjydDbDaoFindAllSql) {
		Config.yjydDbDaoFindAllSql = yjydDbDaoFindAllSql;
	}

	@Value("${YjydDbDao.findAllByUpdateDate.sql}")
	public void setYjydDbDaoFindAllByUpdateDateSql(String yjydDbDaoFindAllByUpdateDateSql) {
		Config.yjydDbDaoFindAllByUpdateDateSql = yjydDbDaoFindAllByUpdateDateSql;
	}

	@Value("${XszDbDao.batchInsert.sql}")
	public void setXszDbDaoBatchInsertSql(String xszDbDaoBatchInsertSql) {
		Config.xszDbDaoBatchInsertSql = xszDbDaoBatchInsertSql;
	}

	@Value("${XszDbDao.batchUpdate.sql}")
	public void setXszDbDaoBatchUpdateSql(String xszDbDaoBatchUpdateSql) {
		Config.xszDbDaoBatchUpdateSql = xszDbDaoBatchUpdateSql;
	}

	@Value("${YjydDbDao.findAllDeviceAttribute.sql}")
	public void setYjydDbFindAllDeviceAttributeSql(String yjydDbFindAllDeviceAttributeSql){
		Config.yjydDbFindAllDeviceAttributeSql = yjydDbFindAllDeviceAttributeSql;
	}

	@Value("${YjydDbDao.findAllDeviceAttributeDate.sql}")
	public void setYjydDbFindAllDeviceAttributeDateSql(String yjydDbFindAllDeviceAttributeDateSql){
		Config.yjydDbFindAllDeviceAttributeDateSql = yjydDbFindAllDeviceAttributeDateSql;
	}

	@Value("${XszDbDao.BIDeviceAttribute.sql}")
	public void setXszDbDaoBIDeviceAttributeSql(String xszDbDaoBIDeviceAttributeSql){
		Config.xszDbDaoBIDeviceAttributeSql = xszDbDaoBIDeviceAttributeSql;
	}

	@Value("${XszDbDao.BUDeviceAttribute.sql}")
	public void setXszDbDaoBUDeviceAttributeSql(String xszDbDaoBUDeviceAttributeSql){
		Config.xszDbDaoBUDeviceAttributeSql = xszDbDaoBUDeviceAttributeSql;
	}

	@Value("${Config.pageSize:2000}")
	public void setPageSize(int pageSize) {
		Config.pageSize = pageSize;
	}
	
	@Value("${Config.update_device_base_info_on_start:false}")
	public void setUpdate_device_base_info_on_start(boolean update_device_base_info_on_start) {
		Config.update_device_base_info_on_start = update_device_base_info_on_start;
	}

	@Value("${Config.updateOrInsterDeviceInfoAll}")
	public void setUpdateOrInsterDeviceInfoAll(boolean updateOrInsterDeviceInfoAll){
		Config.updateOrInsterDeviceInfoAll = updateOrInsterDeviceInfoAll;
	}
	
	
}
