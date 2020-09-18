package com.sailing.dataextraction.dao;

import com.sailing.dataextraction.entity.Flight;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className FlightRepository
 * @description 航班信息Mapper
 * @date 2020/8/6 9:26
 **/
public interface FlightRepository extends Neo4jRepository<Flight, Long> {

    /**
     * 建立航班同行关系
     * @param ssTime 数据入库时间
     * @return 入库数据总数
     */
    @Query(value = "match (t:Flight),(t1:Flight) where t.uuid <> t1.uuid and t.flightNumber = t1.flightNumber and t.time = t1.time and t.dtCode = t1.dtCode and t.rtCode = t1.rtCode and t.ssTime = {0} merge(t) - [r:飞机同行{name:\"飞机同行\"+t.title,uuid:t.uuid+t1.uuid}] -> (t1) return count(r)")
    int createPeerRelation(long ssTime);

    /**
     * 建立人与航班关系
     * @param ssTime 数据入库时间
     * @return 入库数据总数
     */
    @Query(value = "match (p:Population),(t:Flight) where p.idNumber = t.idNumber and t.ssTime = {0} merge(p) - [r:飞机{name:\"飞机\"+t.title,uuid:p.uuid+t.uuid}] -> (t) return count(r)")
    int createPopulationRelation(long ssTime);
}
