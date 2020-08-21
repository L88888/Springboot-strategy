package com.sailing.jdbc.setter;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlPsUtils {
	/**
	 * 根据sql得到需要注入的参数的名称<p>
	 * 如insert into Table(id,name,TJSJ) values(?,?,?)得到list[id,name,TJSJ]
	 * @param sql 如：insert into Table values(:id,:name,:TJSJ)
	 * @return list[id,name,TJSJ]
	 */
	public static List<String> getInsertSqlParamNames(String sql){
		//从SQl中识别出注入参数变量名称的正则
		String patternStr = "\\w+(?=\\s{0,},|\\s{0,}\\))";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher m = pattern.matcher(sql);
		List<String> sqlParamNames = new ArrayList<String>();
	    while(m.find()){
	    	sqlParamNames.add(m.group());
	    }
	    return sqlParamNames;
	}
	
	/**
	 * 根据sql得到需要注入的参数的名称<p>
	 * 如 update SIP_ORG set name=?,TJSJ=? where id=? 得到list[id,name,TJSJ]
	 * @param sql 如：update SIP_ORG set name=?,TJSJ=? where id=?
	 * @return list[id,name,TJSJ]
	 */
	public static List<String> getUpdateSqlParamNames(String sql){
		//从SQl中识别出注入参数变量名称的正则
		String patternStr = "\\w+(?=\\s{0,}\\=\\s{0,}\\?|\\=\\s{0,}\\?|\\s{0,}\\=\\?|\\=\\?)";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher m = pattern.matcher(sql);
		List<String> sqlParamNames = new ArrayList<String>();
	    while(m.find()){
	    	sqlParamNames.add(m.group());
	    }
	    return sqlParamNames;
	}
	
	/**
	 * 根据sql得到需要注入的参数的名称<p>
	 * 如 delete from SIP_ORG where id=? 得到list[id]
	 * @param sql 如：delete from SIP_ORG where id=?
	 * @return list[id]
	 */
	public static List<String> getDeleteSqlParamNames(String sql){
		//从SQl中识别出注入参数变量名称的正则
		String patternStr = "\\w+(?=\\s{0,}\\=\\s{0,}\\?|\\=\\s{0,}\\?|\\s{0,}\\=\\?|\\=\\?)";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher m = pattern.matcher(sql);
		List<String> sqlParamNames = new ArrayList<String>();
	    while(m.find()){
	    	sqlParamNames.add(m.group());
	    }
	    return sqlParamNames;
	}
	
	public static void setValue(PreparedStatement ps, Object param, List<String> sqlParamNames) throws SQLException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException{
		Class<?> clazz = param.getClass();
		for(int i=0; i<sqlParamNames.size(); i++){
			Field field = clazz.getDeclaredField(sqlParamNames.get(i));
			field.setAccessible(true);
			setPsValue(ps, i+1, field.get(param));
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void setValueForMap(PreparedStatement ps, Map param, List<String> sqlParamNames) throws SQLException{
		for(int i=0; i<sqlParamNames.size(); i++){
			Object value =  param.get(sqlParamNames.get(i));
			setPsValue(ps, i+1, value);
		}
	}
	
	private static void setPsValue(PreparedStatement ps, int index, Object value) throws SQLException{
		if(value == null){
			ps.setObject(index, value);
		}else if(value.getClass() == Date.class){
			ps.setTimestamp(index, new java.sql.Timestamp(((Date)value).getTime()));
		}
		else{
			ps.setObject(index, value);
		}
	}
}
