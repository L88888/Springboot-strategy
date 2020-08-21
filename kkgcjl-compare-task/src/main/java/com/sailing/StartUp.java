package com.sailing;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableCaching
//@EnableScheduling
@EnableAsync
public class StartUp {
	
    public static void main(String[] args) {
    	new SpringApplicationBuilder().sources(StartUp.class).web(false).run(args);
    }
}
