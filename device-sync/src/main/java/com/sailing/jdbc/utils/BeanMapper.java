package com.sailing.jdbc.utils;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dozer.DozerBeanMapper;

/**
 * 
 * BeanMapper：简单封装Dozer, 实现深度转换Bean<->Bean的Mapper.实现:
 * <p>
 * <li>1. 持有Mapper的单例</li>
 * <li>2. 返回值类型转换</li>
 * <li>3. 批量转换Collection中的所有对象</li>
 * <li>4. 区分创建新的B对象与将对象A值复制到已存在的B对象两种函数</li>
 */
public class BeanMapper {

	/**
	 * 持有Dozer单例, 避免重复创建DozerMapper消耗资源.
	 */
	private static DozerBeanMapper dozer = new DozerBeanMapper();

	/**
	 * 基于Dozer转换对象的类型.
	 */
	public static <T> T map(Object source, Class<T> destinationClass) {
		return dozer.map(source, destinationClass);
	}

	/**
	 * mapList:基于Dozer转换Collection中对象的类型.
	 * 
	 * @param sourceList
	 * @param destinationClass
	 * @return List<T>
	 */
	public static <T> List<T> mapList(Collection<?> sourceList, Class<T> destinationClass) {
		List<T> destinationList = new ArrayList<T>(sourceList.size());
		for (Object sourceObject : sourceList) {
			T destinationObject = dozer.map(sourceObject, destinationClass);
			destinationList.add(destinationObject);
		}
		return destinationList;
	}

	/**
	 * 
	 * copy:基于Dozer将对象A的值拷贝到对象B中.
	 * 
	 * @param source
	 * @param destinationObject
	 */
	public static void copy(Object source, Object destinationObject) {
		dozer.map(source, destinationObject);
	}
}