package com.sailing.dataextraction.dao;

import com.sailing.dataextraction.entity.Internet;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className InternetRepository
 * @description 上网信息Mapper
 * @date 2020/8/6 13:39
 **/
public interface InternetRepository extends Neo4jRepository<Internet, Long> {

    /**
     * 建立上网同行关系
     * @param ssTime 数据入库时间
     * @return 入库数据总数
     */
    @Query(value = "match (t:Internet),(t1:Internet) where t.uuid <> t1.uuid and t.placeNumber = t1.placeNumber and t.time = t1.time and t.ssTime = {0} merge(t) - [r:上网同行{name:\"上网同行\"+t.title,uuid:t.uuid+t1.uuid}] -> (t1) return count(r)")
    int createPeerRelation(long ssTime);

    /**
     * 建立人与上网关系
     * @param ssTime 数据入库时间
     * @return 入库数据总数
     */
    @Query(value = "match (p:Population),(t:Internet) where p.idNumber = t.idNumber and t.ssTime = {0} merge(p) - [r:上网{name:\"上网\"+t.title,uuid:p.uuid+t.uuid}] -> (t) return count(r)")
    int createPopulationRelation(long ssTime);
}
