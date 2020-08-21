package com.sailing.dataextraction.dao;

import com.sailing.dataextraction.entity.Bus;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className BusRepository
 * @description Bus Repository
 * @date 2020/8/6 13:31
 **/
public interface BusRepository extends Neo4jRepository<Bus, Long> {
}
