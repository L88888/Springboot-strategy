package com.sailing.dataextraction.dao;

import com.sailing.dataextraction.entity.Population;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className PopulationRepository
 * @description Population Repository
 * @date 2020/8/5 14:09
 **/
public interface PopulationRepository extends Neo4jRepository<Population, Long> {
}
