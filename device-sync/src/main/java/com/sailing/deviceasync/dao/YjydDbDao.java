package com.sailing.deviceasync.dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.sailing.deviceasync.GPS.GPS;
import com.sailing.deviceasync.GPS.GPSConverterUtils;
import com.sailing.deviceasync.config.Config;
import com.sailing.deviceasync.entity.DeviceInfo;
import com.sailing.deviceasync.entity.DeviceInfoAttribute;
import com.sailing.jdbc.dao.BaseDao;
import com.sailing.jdbc.utils.BeanMapper;
import com.sailing.jdbc.utils.SqlUtils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 用于一机一档数据库crud
 * 
 * @author sailing
 *
 */
@Slf4j
public class YjydDbDao extends BaseDao {

	/**
	 * 获取所有设备总数
	 * 
	 * @return
	 */
	public long count(String sql) {
		String countSql = new SqlUtils().getCountSql(sql);
		Map<String, Object> countMap = this.queryForMap(countSql);
		return ((BigDecimal) countMap.get("count")).longValue();
	}

	/**
	 * 获取时间段内的数据总数
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public long count(Date startTime, Date endTime) {
		String countSql = new SqlUtils().getCountSql(Config.yjydDbDaoFindAllByUpdateDateSql);
		Map<String, Object> countMap = this.queryForMap(countSql, startTime, endTime);
		return ((BigDecimal) countMap.get("count")).longValue();
	}

	/**
	 * 分页获取设备数据
	 * 
	 * @param page
	 *            当前页码
	 * @param size
	 *            每页大小
	 * @return
	 */
	public List<DeviceInfo> findAll(int page, int size) {
		String pageSql = new SqlUtils().getPageSql(Config.yjydDbDaoFindAllSql, page, size);
		List<Map<String, Object>> rs = this.listMap(pageSql);
		return rs.parallelStream().map(e -> parseDeviceInfo(e)).filter(e -> e.getId() != null).collect(Collectors.toList());
	}

	/**
	 * 分页获取设备属性数据
	 * @param page 当前页码
	 * @param size 每页大小
	 * @return
	 */
	public List<DeviceInfoAttribute> findAllDeviceAttribute(int page, int size) {
		String pageSql = new SqlUtils().getPageSql(Config.yjydDbFindAllDeviceAttributeSql, page, size);
		List<Map<String, Object>> rs = this.listMap(pageSql);
		return rs.parallelStream().map(e -> parseDeviceInfoAttribute(e)).filter(e -> e.getDevice_id() != null).collect(Collectors.toList());
	}
	
	/**
	 * 分页获取时间段内的设备数据
	 * 
	 * @param page
	 *            当前页码
	 * @param size
	 *            每页大小
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public List<DeviceInfo> findAllByUpdateDate(int page, int size, Date startTime, Date endTime) {
		String pageSql = new SqlUtils().getPageSql(Config.yjydDbDaoFindAllByUpdateDateSql, page, size);
		List<Map<String, Object>> rs = this.listMap(pageSql, startTime, endTime);
		return rs.parallelStream().map(e -> parseDeviceInfo(e)).filter(e -> e.getId() != null).collect(Collectors.toList());
	}

	/**
	 * 分页获取时间段内的设备属性数据
	 * @param page 当前页码
	 * @param size 每页大小
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public List<DeviceInfoAttribute> findAllByUpdateDeviceAttributeDate(int page, int size, Date startTime, Date endTime) {
		String pageSql = new SqlUtils().getPageSql(Config.yjydDbFindAllDeviceAttributeDateSql, page, size);
		List<Map<String, Object>> rs = this.listMap(pageSql, startTime, endTime);
		return rs.parallelStream().map(e -> parseDeviceInfoAttribute(e)).filter(e -> e.getDevice_id() != null).collect(Collectors.toList());
	}

	/**
	 * 数据转换，避免保存异常
	 * 
	 * @param data
	 * @return
	 */
	private DeviceInfo parseDeviceInfo(Map<String, Object> data) {
		// 经纬度异常 只获取数字部分的值
		String longitude = Objects.toString(data.get("longitude"), "0").replaceAll("[^\\d+\\.\\d+]", "");
		String latitude = Objects.toString(data.get("latitude"), "0").replaceAll("[^\\d+\\.\\d+]", "");
		// 录像保存时间异常 只获取数字部分的值
		String save_days = Objects.toString(data.get("save_days"), "0").replaceAll("[^\\d+\\.\\d+]", "");
		
		try {
			//解析安装时间
			if (data.get("install_time") != null) {
				// 时间转换
				String install_time = Objects.toString(data.get("install_time"));
				data.put("install_time", new SimpleDateFormat("yyyy-MM-dd").parseObject(install_time));
			}
		} catch (ParseException e) {
			log.error("设备安装时间转换出错, id={}，install_time={}", data.get("id"), data.get("install_time"));
			data.put("install_time", null);// 设置为空避免批量插入时报错
		}
		
		//解析IPv4---新视综库ipv4最大15，一机一档最长：52.30.34.228/229
		if(data.get("ipv4") != null) {
			String ipv4 = data.get("ipv4").toString().split("/")[0];
			data.put("ipv4", ipv4);
		}
		double lat = 0;
		double lon = 0;
		if (StringUtils.isNotEmpty(latitude)) {
			lat = Double.parseDouble(latitude);
		}
		if (StringUtils.isNotEmpty(longitude)) {
			lon = Double.parseDouble(longitude);
		}
		// 经纬度转换火星转84
		GPS gpsInfo = GPSConverterUtils.gps84_To_Gcj02(lat,lon);
		
		data.put("longitude", gpsInfo.getLon());
		data.put("latitude", gpsInfo.getLat());
		data.put("save_days", save_days);
		try {
			return BeanMapper.map(data, DeviceInfo.class);
		} catch (Exception e) {
			log.error("设备信息转换出错，本条信息id={}将不会被同步，请管理员注意！！！！", data.get("id"));
			log.error("设备信息转换出错，请管理员注意！！！！", e);
			return new DeviceInfo();
		}
	}

	/**
	 * 设备信息属性
	 * @param data
	 * @return
	 */
	private DeviceInfoAttribute parseDeviceInfoAttribute(Map<String, Object> data) {
		try {
			return BeanMapper.map(data, DeviceInfoAttribute.class);
		}catch (Exception e){
			log.error("设备信息属性转换出错，本条信息device_id={}不会同步===========", JSONObject.toJSONString(data));
			return new DeviceInfoAttribute();
		}
	}
}
