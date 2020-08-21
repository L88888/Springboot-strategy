package com.sailing.alarmtask.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "KafkaConfig.listener")
@Data
public class KafkaConfig {

	private int concurrency = 1;

	@Bean
	public KafkaListenerContainerFactory<?> batchFactory(ConsumerFactory<String, String> consumerFactory) {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setBatchListener(true);
		factory.setConsumerFactory(consumerFactory);
		factory.setConcurrency(concurrency);
		return factory;
	}

}
