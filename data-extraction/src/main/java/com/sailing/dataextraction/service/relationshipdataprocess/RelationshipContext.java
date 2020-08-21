package com.sailing.dataextraction.service.relationshipdataprocess;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: ZY-device-sync
 * @description: 关系数据抽取上下文对象
 * @author: LIULEI
 * @create: 2020-08-19 19:52:
 **/
@Service
public class RelationshipContext {

    /**
     * 自动加载初始化的配置RelationshipConfiguration对象 initRelation Bean实例,父类Relationship对象
     * ConcurrentHashMap 放置多线程并发时安全问题出现
     *
     * initRelationshipMap 数据结构 key --> value 示例:
     * {key : relationshipHotelImpl, value : RelationshipHotel()}
     * {key : relationshipWb, value : RelationshipWb()}
     *
     */
    @Autowired
    private Map<String, Relationship> initRelationshipMap = new ConcurrentHashMap<>(6);

    /**
     * 通过实现类的枚举key,初始化子类实现类对象
     * @param bussKey 例如住店、网吧、汽车、小旅馆等的同行实现类对象
     * @return 返回nep4j存储后的对象信息,返回null则表示存储数据异常
     */
    public Object getRelationshipImpl(String bussKey, Map<String, String> bussData){
        if (!StringUtils.isBlank(bussKey) && initRelationshipMap != null){
            // 返回nep4j存储后的对象信息
            Relationship relationship = initRelationshipMap.get(bussKey);
            return relationship.process(bussData);
        }
        return null;
    }

}
