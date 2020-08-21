package com.sailing.alarmtask.config;

import java.util.concurrent.Executor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix="executor.data-compare")
@Data
public class ExecutorConfig {
	
	private int corePoolSize;
	
	private int failPoolSize = 3;
	
	/**
	 * 数据比对线程池
	 * @return
	 */
	@Bean
	public Executor sendDataAsync() {
		ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setThreadNamePrefix("compareDataAsync-");
		executor.initialize();
		return executor;
	}
	
}
