package com.sailing.dataextraction.dao;

import com.sailing.dataextraction.entity.Hotel;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className HotelRepository
 * @description 旅店信息Mapper
 * @date 2020/8/6 13:55
 **/
public interface HotelRepository extends Neo4jRepository<Hotel, Long>{

    /**
     * 建立旅店同行关系
     * @param ssTime 数据入库时间
     * @return 入库数据总数
     */
    @Query(value = "match (t:Hotel),(t1:Hotel) where t.uuid <> t1.uuid and t.placeNumber = t1.placeNumber and t.time = t1.time and t.ssTime = {0} merge(t) - [r:住宿同行{name:\"住宿同行\"+t.title,uuid:t.uuid+t1.uuid}] -> (t1) return count(r)")
    int createPeerRelation(long ssTime);

    /**
     * 建立人与旅店关系
     * @param ssTime 数据入库时间
     * @return 入库数据总数
     */
    @Query(value = "match (p:Population),(t:Hotel) where p.idNumber = t.idNumber and t.ssTime = {0} merge(p) - [r:住宿{name:\"住宿\"+t.title,uuid:p.uuid+t.uuid}] -> (t) return count(r)")
    int createPopulationRelation(long ssTime);
}
