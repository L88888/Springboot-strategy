package com.sailing.dataextraction.dao;

import com.sailing.dataextraction.entity.Hotel;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className HotelRepository
 * @description Hotel Repository
 * @date 2020/8/6 13:55
 **/
public interface HotelRepository extends Neo4jRepository<Hotel, Long>{
}
