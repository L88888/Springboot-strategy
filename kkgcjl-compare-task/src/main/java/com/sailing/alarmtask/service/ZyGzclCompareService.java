package com.sailing.alarmtask.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.alibaba.fastjson.JSON;
import com.sailing.alarmtask.config.Config;
import com.sailing.alarmtask.dao.ClypDbDao;
import com.sailing.alarmtask.dao.MobileDbDao;
import com.sailing.alarmtask.entity.MobileUserTask;
import com.sailing.alarmtask.entity.ZyBzwz;
import com.sailing.alarmtask.entity.ZyGzcl;
import com.sailing.alarmtask.entity.ZyGzkk;
import com.sailing.alarmtask.entity.ZyJwdxry;
import com.sailing.alarmtask.entity.ZyKkgcjl;

import lombok.extern.slf4j.Slf4j;

/**
 * 关注车辆比对服务
 * 
 * @author sailing
 *
 */
@Slf4j
@Service
public class ZyGzclCompareService {

	@Autowired
	ClypDbDao clypDbDao;

	@Autowired
	MobileDbDao mobileDbDao;

	@PostConstruct
	public void initCache() {
		// 放入关注车辆缓存中
		mobileDbDao.listAllZyGzcl();
		clypDbDao.listMap("select 1 from dual");
	}

	/**
	 * 关注车辆比对
	 * <p>
	 * 1、获取过车数据信息中号牌号码、号牌种类、标准位置代码字段，先用标准位置代码去用户布控卡口配置表（MOBILE_USER_CONFIG_KK）关联设备位置表（B_QJ_SBWZ）获取位置名称；
	 * 2、再用号牌号码、号牌种类去用户关注车牌配置表（MOBILE_USER_CONFIG_CP）中查询关注车牌信息；
	 * 3、根据config_id字段关联用户配置信息表（MOBILE_USER_CONFIG）中字段关注状态attention='1'(1关注，0取消关注)的信息；
	 * 4、将比对过车数据信息中关注车辆命中卡口的信息存入实时预警消息表中（MOBILE_USER_TASK）；
	 * <p>
	 * <b>比对过程</b>：1.获取所有关注车辆信息 2.比对是否是关注车辆 : true 3.获取位置信息 4.构建入库信息 5.入库 6.错误处理
	 * 
	 * @param records
	 *            过车数据
	 */
	@Async("sendDataAsync")
	public void gzclCompare(List<ZyKkgcjl> records) {
		Date compareTime = new Date();//命中时间|比对时间
		StopWatch watch = new StopWatch("关注车辆-比对任务");
		List<MobileUserTask> mobileUserTasks = new ArrayList<>();
		watch.start("关注车辆-比对");

		// 1.获取所有关注车辆信息
		List<ZyGzcl> zyGzclList = mobileDbDao.listAllZyGzcl();
		for (ZyGzcl zyGzcl : zyGzclList) {
			for (ZyKkgcjl zyKkgcjl : records) {
				try {

					// 2.比对是否是关注车辆
					if (!isGzcl(zyKkgcjl, zyGzcl)) {
						continue;
					}

					// 3.获取位置信息
					String bzwzdm = zyKkgcjl.getBzwzdm();
					ZyBzwz zyBzwz = clypDbDao.bqjSbwzByBzwzdm(bzwzdm);
					if (zyBzwz == null) {
						log.warn("标准位置代码bzwzdm={}未关联到位置信息，关注车辆{}比中后无法产生预警信息。", bzwzdm, zyGzcl.getGzcp());
						continue;
					}

					// 4.构建入库信息
					MobileUserTask gzclUserTask = this.buildMobileUserTask(zyKkgcjl, zyGzcl, zyBzwz, compareTime);
					mobileUserTasks.add(gzclUserTask);

				} catch (Exception e) {
					//6.1错误处理
					log.error("关注车辆-比对过程中出现错误，hphm={}-->jlbh={}", zyKkgcjl.getHphm(), zyKkgcjl.getJlbh());
					log.error("关注车辆-比对过程中出现错误", e);
				}
			}
		}
		watch.stop();

		watch.start("关注车辆-比中数据入库");
		log.info("关注车辆-过车数据：{}条，比中数据入库：{}条", records.size(), mobileUserTasks.size());
		// 5.入库
		try {
			mobileDbDao.batchUpdate(mobileUserTasks);
		} catch (Exception e) {
			//6.2错误处理
			String mobileUserTasksJson = JSON.toJSONString(mobileUserTasks, true);
			log.error("关注车辆-批量保存预警信息出错，出错数据={}", mobileUserTasksJson);
			log.error("关注车辆-批量保存预警信息出错", e);
		}
		watch.stop();

		log.info(watch.prettyPrint());
	}

	/**
	 * 关注车辆比对
	 * <p>
	 * 1、获取过车数据信息中号牌号码、号牌种类、标准位置代码字段，先用标准位置代码去用户布控卡口配置表（MOBILE_USER_CONFIG_KK）关联用户配置信息表（MOBILE_USER_CONFIG）中字段关注状态attention='1'(1关注，0取消关注)的信息获取配置id;
	 * 2、再用号牌号码、号牌种类去重点车辆（JKJWDX_CLXX_1）查询车辆持有人的标签信息；
	 * 3、根据获取配置id查询用户关注人员类型配置表（MOBILE_USER_CONFIG_GZRYLX），利用获取的车辆持有人标签信息匹配用户关注人员类型信息；
	 * 4、将比对过车数据信息中命中卡口的信息存入实时预警消息表中（MOBILE_USER_TASK）；
	 * <p>
	 * <b>比对过程</b>：1.判断是否是重点人员车辆 2.判断是否是关注此类重点人员 : true 3.获取位置信息 4.构建入库信息 5.入库 6.错误处理
	 * 
	 * @param records
	 *            过车数据
	 */
	@Async("sendDataAsync")
	public void gzkkCompare(List<ZyKkgcjl> records) {
		Date compareTime = new Date();//命中时间|比对时间
		StopWatch watch = new StopWatch("关注卡口-比对任务");
		List<MobileUserTask> mobileUserTasks = new ArrayList<>();

		watch.start("关注卡口-比对");
		for (ZyKkgcjl zyKkgcjl : records) {

			try {
				String hphm = zyKkgcjl.getHphm();
				String hpzl = zyKkgcjl.getHpzl();
				String bzwzdm = zyKkgcjl.getBzwzdm();
				// 1.判断是否是重点人员
				List<ZyJwdxry> jwdxrys = clypDbDao.listByClhpAndCllx(hphm, hpzl);
				if (jwdxrys == null || jwdxrys.size() ==0) {
					continue;
				}
				
				// 3.判断位置信息是否存在
				ZyBzwz zyBzwz = clypDbDao.bqjSbwzByBzwzdm(bzwzdm);
				if (zyBzwz == null) {
					log.warn("标准位置代码bzwzdm={}未关联到位置信息，关注卡口{}比中后无法产生预警信息。", bzwzdm, hphm);
					continue;
				}
				
				for(ZyJwdxry jwdxry : jwdxrys) {
					
					// 2.判断是否是关注此类重点人员
					String rylbdm = jwdxry.getRylbdm();
					List<ZyGzkk> zyGzkkList = mobileDbDao.listZyGzkkByRylbdmAndBzwzdm(rylbdm, bzwzdm);
					
					if(zyGzkkList == null || zyGzkkList.size() ==0) {
						continue;
					}
					
					// 4.构建入库信息
					List<MobileUserTask> jwdxUserTasks = this.buildMobileUserTask(zyKkgcjl, zyGzkkList, jwdxry, zyBzwz, compareTime);
					mobileUserTasks.addAll(jwdxUserTasks);
				}

			} catch (Exception e) {
				//6.1错误处理
				log.error("关注卡口-比对过程中出现错误，hphm={}-->jlbh={}", zyKkgcjl.getHphm(), zyKkgcjl.getJlbh());
				log.error("关注卡扣-比对过程中出现错误", e);
			}

		}

		watch.stop();

		watch.start("关注卡口-比中数据入库");
		log.info("关注卡口-过车数据：{}条，比中数据入库：{}条", records.size(), mobileUserTasks.size());
		try {
			// 5.入库
			mobileDbDao.batchUpdate(mobileUserTasks);
		} catch (Exception e) {
			//6.2错误处理
			String mobileUserTasksJson = JSON.toJSONString(mobileUserTasks, true);
			log.error("关注卡口-批量保存预警信息出错，出错数据={}", mobileUserTasksJson);
			log.error("关注卡口-批量保存预警信息出错", e);
		}
		watch.stop();

		log.info(watch.prettyPrint());
	}

	/**
	 * 生成预警信息
	 * 
	 * @param zyKkgcjl
	 *            过车信息
	 * @param zyGzcl
	 *            关注车辆信息
	 * @param zyBzwz
	 *            位置信息
	 * @param mzsj 
	 * @return
	 */
	private MobileUserTask buildMobileUserTask(ZyKkgcjl zyKkgcjl, ZyGzcl zyGzcl, ZyBzwz zyBzwz, Date mzsj) {
		MobileUserTask mobileUserTask = new MobileUserTask();
		mobileUserTask.setId(this.getUUID32());
		mobileUserTask.setUser_id(zyGzcl.getUser_id());
		mobileUserTask.setXxnr(this.buildAlarmMsg(zyKkgcjl, zyBzwz));
		mobileUserTask.setOrg_id(zyGzcl.getOrg_id());
		mobileUserTask.setType_(Config.type_cp);
		mobileUserTask.setHithphm(zyKkgcjl.getHphm());
		mobileUserTask.setHithpzl(zyKkgcjl.getHpzl());
		mobileUserTask.setHitbzwzdm(zyKkgcjl.getBzwzdm());
		mobileUserTask.setHitsbmc(zyBzwz.getWzmc());
		mobileUserTask.setHitjlbh(zyKkgcjl.getJlbh());
		mobileUserTask.setJgsj(zyKkgcjl.getJgsj());
		mobileUserTask.setMzsj(mzsj);
		mobileUserTask.setConfigid(zyGzcl.getConfig_id());
		return mobileUserTask;
	}

	/**
	 * 生成预警信息
	 * 
	 * @param zyKkgcjl
	 *            过车数据
	 * @param zyGzkkList
	 *            用户配置的关注卡口信息
	 * @param jwdxry
	 * @param zyBzwz
	 *            标准位置信息
	 * @param compareTime 
	 * @return
	 */
	private List<MobileUserTask> buildMobileUserTask(ZyKkgcjl zyKkgcjl, List<ZyGzkk> zyGzkkList, ZyJwdxry jwdxry,
			ZyBzwz zyBzwz, Date compareTime) {
		List<MobileUserTask> mobileUserTasks = new ArrayList<>();
		if (zyGzkkList == null) {
			return mobileUserTasks;
		}
		for (ZyGzkk gzkk : zyGzkkList) {
			MobileUserTask mobileUserTask = new MobileUserTask();
			mobileUserTask.setId(this.getUUID32());
			mobileUserTask.setUser_id(gzkk.getUser_id());
			mobileUserTask.setXxnr(this.buildAlarmMsg(zyKkgcjl, jwdxry, zyBzwz));
			mobileUserTask.setOrg_id(gzkk.getOrg_id());
			mobileUserTask.setType_(Config.type_kk);
			mobileUserTask.setHithphm(zyKkgcjl.getHphm());
			mobileUserTask.setHithpzl(zyKkgcjl.getHpzl());
			mobileUserTask.setHitbzwzdm(zyKkgcjl.getBzwzdm());
			mobileUserTask.setHitsbmc(zyBzwz.getWzmc());
			mobileUserTask.setHitjlbh(zyKkgcjl.getJlbh());
			mobileUserTask.setJgsj(zyKkgcjl.getJgsj());
			mobileUserTask.setMzsj(compareTime);
			mobileUserTask.setConfigid(gzkk.getConfig_id());
			mobileUserTasks.add(mobileUserTask);
		}
		return mobileUserTasks;
	}

	/**
	 * 生产预警信息消息体
	 * 
	 * @param zyKkgcjl
	 *            过车信息
	 * @param zyBzwz
	 *            位置信息
	 * @return
	 */
	private String buildAlarmMsg(ZyKkgcjl zyKkgcjl, ZyBzwz zyBzwz) {
		String msg = "关注车辆{hphm}于{jgsj}经过{wzmc}。";
		Date jgsj = zyKkgcjl.getJgsj();
		String jgsjStr = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分").format(jgsj);
		return msg.replace("{hphm}", zyKkgcjl.getHphm()).replace("{jgsj}", jgsjStr).replace("{wzmc}", zyBzwz.getWzmc());
	}

	/**
	 * 生产预警信息消息体
	 * 
	 * @param zyKkgcjl
	 *            过车信息
	 * @param gzkk
	 * @param zyBzwz
	 *            位置信息
	 * @return
	 */
	private String buildAlarmMsg(ZyKkgcjl zyKkgcjl, ZyJwdxry jwdxry, ZyBzwz zyBzwz) {
		String msg = "{xm}（{rylb}）关联车辆{hphm}于{jgsj}经过{wzmc}。";
		String xm = jwdxry.getXm();
		String rylb = jwdxry.getRylb();
		String hphm = zyKkgcjl.getHphm();
		String wzmc = zyBzwz.getWzmc();
		Date jgsj = zyKkgcjl.getJgsj();
		String jgsjStr = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分").format(jgsj);
		return msg.replace("{xm}", xm).replace("{rylb}", rylb).replace("{hphm}", hphm).replace("{jgsj}", jgsjStr)
				.replace("{wzmc}", wzmc);
	}

	/**
	 * 判断本条过车数据是否是关注车辆
	 * 
	 * @param zyKkgcjl
	 *            过车记录
	 * @param zyGzcl
	 *            关注车辆信息
	 * @return
	 */
	private boolean isGzcl(ZyKkgcjl zyKkgcjl, ZyGzcl zyGzcl) {
		String hphm = zyGzcl.getGzcp();
		String hpzl = zyGzcl.getCpzl();
		String kkgcjl_hphm = zyKkgcjl.getHphm();
		String kkgcjl_hpzl = zyKkgcjl.getHpzl();
		boolean isGzcl = hphm.equals(kkgcjl_hphm) && hpzl.equals(kkgcjl_hpzl);
//		log.info("关注车辆：{}-{}，卡口过车车辆：{}-{}，是否比中：{}", hphm, hpzl, kkgcjl_hphm, kkgcjl_hpzl, isGzcl);
		return isGzcl;
	}

	/**
	 * 生成UUID
	 * 
	 * @return
	 */
	private String getUUID32() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
