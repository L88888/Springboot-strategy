package com.sailing.dataextraction.dao;

import com.sailing.dataextraction.entity.Train;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className TrainRepository
 * @description Train Repository
 * @date 2020/8/6 8:52
 **/
public interface TrainRepository extends Neo4jRepository<Train, Long> {
}
