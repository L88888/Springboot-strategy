package com.sailing.dataextraction.dao;

import com.sailing.dataextraction.entity.Train;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className TrainRepository
 * @description 火车信息Mapper
 * @date 2020/8/6 8:52
 **/
public interface TrainRepository extends Neo4jRepository<Train, Long> {

    /**
     * 建立火车同行关系
     * @param ssTime 数据入库时间
     * @return 入库数据总数
     */
    @Query(value = "match (t:Train),(t1:Train) where t.uuid <> t1.uuid and t.trainNumber = t1.trainNumber and t.time = t1.time and t.dt = t1.dt and t.rt = t1.rt and t.ssTime = {0} merge(t) - [r:火车同行{name:\"火车同行\"+t.title,uuid:t.uuid+t1.uuid}] -> (t1) return count(r)")
    int createPeerRelation(long ssTime);

    /**
     * 建立人与火车关系
     * @param ssTime 数据入库时间
     * @return 入库数据总数
     */
    @Query(value = "match (p:Population),(t:Train) where p.idNumber = t.idNumber and t.ssTime = {0} merge(p) - [r:火车{name:\"火车\"+t.title,uuid:p.uuid+t.uuid}] -> (t) return count(r)")
    int createPopulationRelation(long ssTime);
}
