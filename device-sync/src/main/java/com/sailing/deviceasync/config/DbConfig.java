package com.sailing.deviceasync.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sailing.deviceasync.dao.XszDbDao;
import com.sailing.deviceasync.dao.YjydDbDao;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 数据库相关信息初始化配置
 * 
 * @author sailing
 *
 */
@Configuration
public class DbConfig {
	/**
	 * 一机一档
	 * @return
	 */
	@Primary
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.yjyd")
	public DataSource yjydDataSource() {
		HikariDataSource dataSource = (HikariDataSource) DataSourceBuilder.create().build();
		dataSource.setMinimumIdle(3);
		dataSource.setMaximumPoolSize(5);
		dataSource.setPoolName("yjyd-hikaridatasource");
		dataSource.setConnectionTestQuery("select 1 from dual");
		dataSource.setMaxLifetime(1800000);
		dataSource.setAutoCommit(true);
		return dataSource;
	}
	/**
	 * 新视综
	 * @return
	 */
	@Bean
	@Qualifier("xszDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.xsz")
	public DataSource xszDataSource() {
		HikariDataSource dataSource = (HikariDataSource) DataSourceBuilder.create().build();
		dataSource.setMinimumIdle(3);
		dataSource.setMaximumPoolSize(5);
		dataSource.setPoolName("xsz-hikaridatasource");
		dataSource.setConnectionTestQuery("select 'OK' as txt");
		dataSource.setMaxLifetime(1800000);
		dataSource.setAutoCommit(true);
		return dataSource;
	}
	/**
	 * 一机一档
	 * @return
	 */
	@Bean
	public YjydDbDao yjydBaseDao() {
		YjydDbDao yjydDbDao = new YjydDbDao();
		yjydDbDao.setJdbcTemplate(new JdbcTemplate(yjydDataSource()));
		return yjydDbDao;
	}
	/**
	 * 新视综
	 * @return
	 */
	@Bean
	public XszDbDao xszBaseDao() {
		XszDbDao xszDbDao = new XszDbDao();
		xszDbDao.setJdbcTemplate(new JdbcTemplate(xszDataSource()));
		return xszDbDao;
	}

}
