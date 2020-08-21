package com.sailing.dataextraction.dao;

import com.sailing.dataextraction.entity.Flight;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className FlightRepository
 * @description Flight Repository
 * @date 2020/8/6 9:26
 **/
public interface FlightRepository extends Neo4jRepository<Flight, Long> {
}
