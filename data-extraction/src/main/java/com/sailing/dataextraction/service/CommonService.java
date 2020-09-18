package com.sailing.dataextraction.service;

import com.sailing.dataextraction.common.event.EventEntity;
import com.sailing.dataextraction.entity.IndexJsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className CommonService
 * @description 公共服务
 * @date 2020/8/27 18:07
 **/
@Slf4j
@Service
public class CommonService {

    @Autowired
    private Neo4jService neo4jService;

    /**
     * 同户信息
     */
    @Value("${sameInfoIdentifiers}")
    private String sameInfoIdentifiers;

    /**
     * 火车信息
     */
    @Value("${trainTripIdentifiers}")
    private String trainTripIdentifiers;

    /**
     * 飞机信息
     */
    @Value("${flightTripIdentifiers}")
    private String flightTripIdentifiers;

    /**
     * 客运信息
     */
    @Value("${busTripIdentifiers}")
    private String busTripIdentifiers;

    /** 旅店信息 */
    @Value("${accommodationIdentifiers}")
    private String accommodationIdentifiers;

    /**
     * 上网信息
     */
    @Value("${surfInternetIdentifiers}")
    private String surfInternetIdentifiers;

    public void onApplicationEvent(IndexJsonData  indexJsonData) {
        log.info("开始处理{}数据量{}",indexJsonData.getResourceId(), indexJsonData.getData().size());
        String resourceId = indexJsonData.getResourceId();
        if (sameInfoIdentifiers.equals(resourceId)){
            neo4jService.addPopulation(indexJsonData.getData());
        } else if (trainTripIdentifiers.equals(resourceId)){
            neo4jService.addTrain(indexJsonData.getData());
        } else if (flightTripIdentifiers.equals(resourceId)){
            neo4jService.addFlight(indexJsonData.getData());
        } else if (busTripIdentifiers.equals(resourceId)){
            neo4jService.addBus(indexJsonData.getData());
        } else if (accommodationIdentifiers.equals(resourceId)){
            neo4jService.addHotel(indexJsonData.getData());
        } else if (surfInternetIdentifiers.equals(resourceId)){
            neo4jService.addInternet(indexJsonData.getData());
        } else {
            log.info("{}索引数据未匹配到对应所需索引", indexJsonData.getResourceId());
        }
    }

}
