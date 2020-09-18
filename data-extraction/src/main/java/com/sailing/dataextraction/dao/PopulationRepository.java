package com.sailing.dataextraction.dao;

import com.sailing.dataextraction.entity.Population;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className PopulationRepository
 * @description 户籍信息Mapper
 * @date 2020/8/5 14:09
 **/
public interface PopulationRepository extends Neo4jRepository<Population, Long> {

    /**
     * 建立户籍同户关系
     * @param ssTime 数据入库时间
     * @return 入库数据总数
     */
    @Query(value = "match (p:Population{hohRelations:\"户主\"}),(p1:Population) where p.idNumber <> p1.idNumber and p.familyNumber = p1.familyNumber and p.ssTime = {0} merge(p) - [r:同户{name:p1.hohRelations,uuid:p.uuid+p1.uuid}] -> (p1) return count(r)")
    int createPeerRelation(long ssTime);

    /**
     * 建立人与大巴关系
     * @param ssTime 数据入库时间
     * @return 入库数据总数
     */
    @Query(value = "match (p:Population),(t:Bus) where p.idNumber = t.idNumber and p.ssTime = {0} merge(p) - [r:大巴{name:\"大巴\"+t.title,uuid:p.uuid+t.uuid}] -> (t) return count(r)")
    int createBusRelation(long ssTime);

    /**
     * 建立人与航班关系
     * @param ssTime 数据入库时间
     * @return 入库数据总数
     */
    @Query(value = "match (p:Population),(t:Flight) where p.idNumber = t.idNumber and p.ssTime = {0} merge(p) - [r:飞机{name:\"飞机\"+t.title,uuid:p.uuid+t.uuid}] -> (t) return count(r)")
    int createFlightRelation(long ssTime);

    /**
     * 建立人与旅店关系
     * @param ssTime 数据入库时间
     * @return 入库数据总数
     */
    @Query(value = "match (p:Population),(t:Hotel) where p.idNumber = t.idNumber and p.ssTime = {0} merge(p) - [r:住宿{name:\"住宿\"+t.title,uuid:p.uuid+t.uuid}] -> (t) return count(r)")
    int createHotelRelation(long ssTime);

    /**
     * 建立人与上网关系
     * @param ssTime 数据入库时间
     * @return 入库数据总数
     */
    @Query(value = "match (p:Population),(t:Internet) where p.idNumber = t.idNumber and p.ssTime = {0} merge(p) - [r:上网{name:\"上网\"+t.title,uuid:p.uuid+t.uuid}] -> (t) return count(r)")
    int createInternetRelation(long ssTime);

    /**
     * 建立人与火车关系
     * @param ssTime 数据入库时间
     * @return 入库数据总数
     */
    @Query(value = "match (p:Population),(t:Train) where p.idNumber = t.idNumber and p.ssTime = {0} merge(p) - [r:火车{name:\"火车\"+t.title,uuid:p.uuid+t.uuid}] -> (t) return count(r)")
    int createTrainRelation(long ssTime);
}
