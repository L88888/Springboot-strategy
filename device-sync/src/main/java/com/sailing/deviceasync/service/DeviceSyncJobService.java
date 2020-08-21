package com.sailing.deviceasync.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.sailing.deviceasync.entity.DeviceInfoAttribute;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.sailing.deviceasync.config.Config;
import com.sailing.deviceasync.dao.XszDbDao;
import com.sailing.deviceasync.dao.YjydDbDao;
import com.sailing.deviceasync.entity.DeviceInfo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * 设备信息同步定时服务
 * 
 * @author sailing
 *
 */
@Slf4j
@Service
public class DeviceSyncJobService {

	@Autowired
	XszDbDao xszDbDao;

	@Autowired
	YjydDbDao yjydDbDao;

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Scheduled(cron = "${DeviceSyncJobService.startSyncJob.cron}")
	public void startSyncJob() {
		DateTime currentTime = new DateTime();// 当前时间前1天
		Date startTime = currentTime.minusDays(1).toDate();// 当前时间减去1天
		Date endTime = currentTime.toDate();// 当前时间前1天的23时

		long count = 0;
		if (Config.updateOrInsterDeviceInfoAll){
			count = yjydDbDao.count(Config.yjydDbDaoFindAllSql);
			log.info("开始进行设备增量更新 {} 条数据....", count);
		}else {
			count = yjydDbDao.count(startTime, endTime);
			log.info("开始根据更新时间进行设备增量更新 {} 条数据....开始时间：{} - 结束时间：{}", count, dateFormat.format(startTime), dateFormat.format(endTime));
		}

		int totolPage = this.getTotalPage(count);
		for (int page = 0; page < totolPage; page++) {
			try {
				StopWatch watch = new StopWatch("第" + (page + 1) + "/" + totolPage + "页");
				watch.start("第" + page + "/" + totolPage + "页-查询一机一档数据");
				List<DeviceInfo> deviceInfos = new ArrayList<>();
				List<DeviceInfoAttribute> deviceInfoAttributes = new ArrayList<>();
				if (Config.updateOrInsterDeviceInfoAll) {
					deviceInfos = yjydDbDao.findAll(page, Config.pageSize);
					deviceInfoAttributes = yjydDbDao.findAllDeviceAttribute(page, Config.pageSize);
				}else {
					deviceInfos = yjydDbDao.findAllByUpdateDate(page, Config.pageSize, startTime, endTime);
					deviceInfoAttributes = yjydDbDao.findAllByUpdateDeviceAttributeDate(page, Config.pageSize, startTime, endTime);
				}

				// 需要插入的
				List<DeviceInfo> insertDeviceInfos = new ArrayList<>();
				// 需要更新的
				List<DeviceInfo> updateDeviceInfos = new ArrayList<>();

				// 需要插入的设备属性
				List<DeviceInfoAttribute> insertDeviceAttributes = new ArrayList<>();
				// 需要更新的设备属性
				List<DeviceInfoAttribute> updateDeviceAttributes = new ArrayList<>();

				deviceInfos.parallelStream().forEach(e -> {
					String device_id = e.getDevice_id();
					if (!StringUtils.isEmpty(device_id)) {
						if (xszDbDao.exists(Config.xszDbDaoDeviceInfoRecordSql, device_id)) {
							updateDeviceInfos.add(e);
						} else {
							insertDeviceInfos.add(e);
						}
					}
				});
				deviceInfoAttributes.parallelStream().forEach(e -> {
					String device_id = e.getDevice_id();
					if (!StringUtils.isEmpty(device_id)) {
						if (xszDbDao.exists(Config.xszDbDaoDeviceAttributeRecordSql, device_id)) {
							updateDeviceAttributes.add(e);
						} else {
							insertDeviceAttributes.add(e);
						}
					}
				});

				watch.stop();
				try {
					watch.start("第" + (page + 1) + "/" + totolPage + "页-批量保存设备增量");
					log.info("第" + (page + 1) + "/" + totolPage + "页-批量保存设备增量 {} 条", insertDeviceInfos.size() + updateDeviceInfos.size());
					log.info("第" + (page + 1) + "/" + totolPage + "页-批量保存设备属性增量 {} 条", insertDeviceAttributes.size() + updateDeviceAttributes.size());

					xszDbDao.batchInsert(insertDeviceInfos);
					xszDbDao.batchUpdate(updateDeviceInfos);
					xszDbDao.batchInsertDeviceAttribute(insertDeviceAttributes);
					xszDbDao.batchUpdateDeviceAttribute(updateDeviceAttributes);
					watch.stop();
					log.info(watch.prettyPrint());
				} catch (Exception e) {
					log.error("第" + (page + 1) + "/" + totolPage + "页-批量保存设备增量出错", e);
				}
			} catch (Exception e) {
				log.error("第" + (page + 1) + "/" + totolPage + "页-设备增量出错", e);
			}
		}

		if (Config.updateOrInsterDeviceInfoAll){
			log.info("完成根据更新时间设备增量更新  {} 条....", count);
		}else {
			log.info("完成根据更新时间设备增量更新  {} 条....开始时间：{} - 结束时间：{}", count, dateFormat.format(startTime), dateFormat.format(endTime));
		}
	}

	/**
	 * 设备基础信息更新
	 */
	@Scheduled(cron = "${DeviceSyncJobService.updateDeviceBaseInfo.cron}")
	public void updateDeviceBaseInfo() {
		long count = yjydDbDao.count(Config.yjydDbDaoFindAllSql);

		log.info("开始更新的设备基础信息 {} 条", count);

		int totolPage = this.getTotalPage(count);
		for (int page = 0; page < totolPage; page++) {

			try {

				StopWatch watch = new StopWatch("第" + (page + 1) + "/" + totolPage + "页");
				watch.start("第" + page + "/" + totolPage + "页-查询一机一档数据");

				List<DeviceInfo> deviceInfos = yjydDbDao.findAll(page, Config.pageSize);
				List<DeviceInfo> updateDeviceInfos = deviceInfos.parallelStream().filter(e -> e.getDevice_id() != null).collect(Collectors.toList());

				List<DeviceInfoAttribute> deviceInfoAttributes = yjydDbDao.findAllDeviceAttribute(page, Config.pageSize);

				try {
					watch.stop();
					watch.start("第" + (page + 1) + "/" + totolPage + "页-批量保存设备基础信息");
					log.info("第" + (page + 1) + "/" + totolPage + "页-批量保存设备基础信息 {} 条", updateDeviceInfos.size());
					log.info("第" + (page + 1) + "/" + totolPage + "页-批量保存设备属性信息 {} 条", deviceInfoAttributes.size());

					xszDbDao.batchUpdate(updateDeviceInfos);
					xszDbDao.batchUpdateDeviceAttribute(deviceInfoAttributes);

					watch.stop();
					log.info(watch.prettyPrint());
				} catch (Exception e) {
					log.error("第" + (page + 1) + "/" + totolPage + "页-批量保存设备基础信息出错", e);
				}

			} catch (Exception e) {
				log.error("第" + (page + 1) + "/" + totolPage + "页-设备基础信息更新出错", e);
			}

		}

		log.info("完成设备基础信息更新 {} 条", count);
	}

	/**
	 * 获取分页总页数
	 * 
	 * @param count
	 *            数据总数
	 * @return
	 */
	public int getTotalPage(long count) {
		return (int) (count % Config.pageSize == 0 ? count / Config.pageSize : count / Config.pageSize + 1);
	}

}
