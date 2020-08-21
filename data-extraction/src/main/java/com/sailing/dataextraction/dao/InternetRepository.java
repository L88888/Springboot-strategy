package com.sailing.dataextraction.dao;

import com.sailing.dataextraction.entity.Internet;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className InternetRepository
 * @description Internet Repository
 * @date 2020/8/6 13:39
 **/
public interface InternetRepository extends Neo4jRepository<Internet, Long> {
}
