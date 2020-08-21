package com.sailing.jdbc.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sailing.jdbc.setter.BatchUpdateSetter;
import com.sailing.jdbc.setter.SingleUpdateSetter;
import com.sailing.jdbc.utils.BeanMapper;

/**
 * 完成对数据库的query和update
 *
 * @author YaoWei
 */
public class BaseDao {
	/**
	 * 日志记录工具类
	 */
	protected Logger logger = LoggerFactory.getLogger(BaseDao.class);
	/**
	 * 数据库操作对象
	 */
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 将查询结果封装为map对象
	 *
	 * @param sql
	 *            sql语句
	 * @param params
	 *            参数
	 * @return
	 */
	public Map<String, Object> queryForMap(String sql, Object... params) {
		return this.jdbcTemplate.queryForMap(sql, params);
	}

	/**
	 * 将查询结果封装为T对象
	 *
	 * @param sql
	 *            sql语句
	 * @param params
	 *            参数
	 * @return
	 */
	public <T> T query(Class<T> clazz, String sql, Object... params) {
		try {
			Map<String, Object> queryRs = this.queryForMap(sql, params);
			return BeanMapper.map(queryRs, clazz);
		}  catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 将查询结果封装为List<Map<String,Object>>对象
	 *
	 * @param sql
	 *            sql语句
	 * @param params
	 *            参数
	 * @return
	 */
	public List<Map<String, Object>> listMap(String sql, Object... params) {
		return this.jdbcTemplate.queryForList(sql, params);
	}

	/**
	 * 将查询结果封装为List<T>对象
	 *
	 * @param sql
	 *            sql语句
	 * @param params
	 *            参数
	 * @return
	 */
	public <T> List<T> list(Class<T> clazz, String sql, Object... params) {
		List<Map<String, Object>> listMap = this.listMap(sql, params);
		return BeanMapper.mapList(listMap, clazz);
	}

	/**
	 * 批量保存/修改/删除
	 * 
	 * @param setter
	 *            also see {@linkplain BatchPreparedStatementSetter}
	 * @return
	 */
	public int[] batchUpdate(BatchUpdateSetter setter) {
		return jdbcTemplate.batchUpdate(setter.getSql(), setter);
	}

	/**
	 * 保存/修改/删除
	 * 
	 * @param setter
	 *            also see {@linkplain SingleUpdateSetter}
	 * @return
	 */
	public int update(String sql, Object param) {
		if (param == null) {
			return jdbcTemplate.update(sql);
		} else {
			return jdbcTemplate.update(sql, new SingleUpdateSetter(sql, param));
		}
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
