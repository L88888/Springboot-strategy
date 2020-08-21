package com.sailing.alarmtask.kafka;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.sailing.alarmtask.entity.ZyKkgcjl;
import com.sailing.alarmtask.service.ZyGzclCompareService;

import lombok.extern.slf4j.Slf4j;

/**
 * 消费卡口过车信息，比对产生预警消息入库
 * 
 * @author sailing
 *
 */
@Component
@Slf4j
public class KkgcjlAlarmConsumer {

	@Autowired
	ZyGzclCompareService zyGzclCompareService;

	@KafkaListener(topics = "#{'${kafka.topics}'.split(',')}", containerFactory = "batchFactory")
	public void consumer(List<ConsumerRecord<String, String>> consumerRecords) {
		List<ZyKkgcjl> zyKkgcjlList = new ArrayList<>(consumerRecords.size());
		/**
		 * 数据转换
		 */
		for (ConsumerRecord<String, String> record : consumerRecords) {
			
			String gcjlStr = record.value();
			String[] gcjlArray = gcjlStr.replaceAll("\"", "").split(",");
			
			if (!this.valid(gcjlArray)) {
				log.info("消费到数据不符合约定的数据规范 record = {}", gcjlStr);
				continue;
			}
			
			ZyKkgcjl zyKkgcjl = buildZyKkgcjl(gcjlArray);
			zyKkgcjlList.add(zyKkgcjl);
			
		}
		
		log.info("消费到kafka有效数据>>>>>>>>>> record.size = {}", zyKkgcjlList.size());
		
		if(zyKkgcjlList.size() ==0) {
			return;
		}
		/**
		 * 关注车辆任务比对
		 */
		zyGzclCompareService.gzclCompare(zyKkgcjlList);
		/**
		 * 关注卡口数据比对
		 */
		 zyGzclCompareService.gzkkCompare(zyKkgcjlList);
	}

	/**
	 * 构建过车记录
	 * 
	 * @param gcjlArray
	 * @return
	 */
	private ZyKkgcjl buildZyKkgcjl(String[] gcjlArray) {
		ZyKkgcjl zyKkgcjl = new ZyKkgcjl();
		try {
			Date jgsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ss").parse(gcjlArray[0]);
			zyKkgcjl.setJgsj(jgsj);
		} catch (ParseException e) {
			log.error("经过时间不符合规范 jgsj = {}", gcjlArray[0]);
		}
		zyKkgcjl.setXzqh(gcjlArray[1]);
		zyKkgcjl.setJlbh(gcjlArray[2]);
		zyKkgcjl.setBzwzdm(gcjlArray[3]);
		zyKkgcjl.setHphm(gcjlArray[4]);
		zyKkgcjl.setHpzl(praseHpzl(gcjlArray[5]));

		return zyKkgcjl;
	}

	/**
	 * 检查数据是否符合规范
	 * 
	 * @param gcjlArray
	 * @return
	 */
	private boolean valid(String[] gcjlArray) {
		if (gcjlArray.length < 6) {
			//不符合约定的消费数据规范
			return false;
		}
		String hphm = gcjlArray[4];
		if (hphm == null || hphm.length() < 7 || hphm.startsWith("??")) {
			//不符合车牌号
			return false;
		}
		return true;
	}

	/**
	 * 转换为符合系统规范的号牌种类
	 * 
	 * @param hpzl
	 * @return 0->0
	 *         <p/>
	 *         1->01
	 *         <p/>
	 *         10->10
	 */
	private String praseHpzl(String hpzl) {
		if (!"0".equals(hpzl) && hpzl.length() == 1) {
			return "0" + hpzl;
		}
		return hpzl;
	}
}
