package com.sailing.dataextraction.service.relationshipdataprocess;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @program: ZY-device-sync
 * @description: 关系数据抽取父类,这是一个抽象类
 * @author: LIULEI
 * @create: 2020-08-19 19:43:子类分别实现该父类方法，进行数据sink至neo4j实体
 **/
@Component
public interface Relationship {

    /**
     * 同行关系数据处理过程,从elasticsearch至neo4j
     * @param bussData
     * @return
     */
    public  Object process(Map<String, String> bussData);
}
