package com.sailing.dataextraction.dao;

import com.sailing.dataextraction.entity.Bus;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className BusRepository
 * @description 大巴信息Mapper
 * @date 2020/8/6 13:31
 **/
public interface BusRepository extends Neo4jRepository<Bus, Long> {

    /**
     * 建立大巴同行关系
     * @param ssTime 数据入库时间
     * @return 入库数据总数
     */
    @Query(value = "match (t:Bus),(t1:Bus) where t.uuid <> t1.uuid and t.shiftNumber = t1.shiftNumber and t.time = t1.time and t.dt = t1.dt and t.rt = t1.rt and t.ssTime = {0} merge(t) - [r:大巴同行{name:\"大巴同行\"+t.title,uuid:t.uuid+t1.uuid}] -> (t1) return count(r)")
    int createPeerRelation(long ssTime);

    /**
     * 建立人与大巴关系
     * @param ssTime 数据入库时间
     * @return 入库数据总数
     */
    @Query(value = "match (p:Population),(t:Bus) where p.idNumber = t.idNumber and t.ssTime = {0} merge(p) - [r:大巴{name:\"大巴\"+t.title,uuid:p.uuid+t.uuid}] -> (t) return count(r)")
    int createPopulationRelation(long ssTime);
}
