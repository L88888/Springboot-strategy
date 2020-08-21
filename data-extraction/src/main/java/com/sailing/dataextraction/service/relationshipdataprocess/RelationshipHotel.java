package com.sailing.dataextraction.service.relationshipdataprocess;

import com.sailing.dataextraction.entity.Hotel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @program: ZY-device-sync
 * @description: 住店数据对象抽取并写入neo4j实体
 * @author: LIULEI
 * @create: 2020-08-19 19:50:
 **/
@Slf4j
@Component("relationshipHotelImpl")
public class RelationshipHotel implements Relationship{

    public RelationshipHotel(){
        log.info("init 住店数据对象抽取并写入neo4j实体");
    }

    /**
     * 住店数据对象抽取并写入neo4j实体
     * @param bussData
     * @return
     */
    @Override
    public Hotel process(Map<String, String> bussData) {
        log.info("RelationshipHotel is value data :>{}", bussData.toString());
        return null;
    }
}
