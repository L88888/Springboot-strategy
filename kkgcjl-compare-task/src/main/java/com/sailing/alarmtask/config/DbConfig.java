package com.sailing.alarmtask.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sailing.alarmtask.dao.ClypDbDao;
import com.sailing.alarmtask.dao.MobileDbDao;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 数据库相关信息初始化配置
 * 
 * @author sailing
 *
 */
@Configuration
public class DbConfig {

	@Primary
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.mobile")
	public DataSource mobileDataSource() {
		HikariDataSource dataSource = (HikariDataSource) DataSourceBuilder.create().build();
		dataSource.setMinimumIdle(50);
		dataSource.setMaximumPoolSize(100);
		dataSource.setPoolName("mobile-hikaridatasource");
		dataSource.setConnectionTestQuery("select 1 from dual");
		dataSource.setMaxLifetime(1800000);
		dataSource.setAutoCommit(true);
		return dataSource;
	}

	@Bean
	@Qualifier("clypDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.clyp")
	public DataSource clypDataSource() {
		HikariDataSource dataSource = (HikariDataSource) DataSourceBuilder.create().build();
		dataSource.setMinimumIdle(50);
		dataSource.setMaximumPoolSize(100);
		dataSource.setPoolName("clyp-hikaridatasource");
		dataSource.setConnectionTestQuery("select 1 from dual");
		dataSource.setMaxLifetime(1800000);
		dataSource.setAutoCommit(true);
		return dataSource;
	}

	@Bean
	public MobileDbDao mobileBaseDao() {
		MobileDbDao mobileDbDao = new MobileDbDao();
		mobileDbDao.setJdbcTemplate(new JdbcTemplate(mobileDataSource()));
		return mobileDbDao;
	}

	@Bean
	public ClypDbDao clypBaseDao() {
		ClypDbDao clypDbDao = new ClypDbDao();
		clypDbDao.setJdbcTemplate(new JdbcTemplate(clypDataSource()));
		return clypDbDao;
	}

}
