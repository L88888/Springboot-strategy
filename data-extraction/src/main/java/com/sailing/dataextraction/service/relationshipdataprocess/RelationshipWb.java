package com.sailing.dataextraction.service.relationshipdataprocess;

import com.sailing.dataextraction.entity.Internet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @program: ZY-device-sync
 * @description: 网吧数据对象抽取并写入neo4j实体
 * @author: LIULEI
 * @create: 2020-08-20 21:13:
 **/
@Slf4j
@Component("relationshipWb")
public class RelationshipWb implements Relationship {

    public RelationshipWb(){
        log.info("init 网吧数据对象抽取并写入neo4j实体");
    }

    /**
     * 网吧数据抽neo4j实体
     * @param bussData
     * @return
     */
    @Override
    public Internet process(Map<String, String> bussData) {
        log.info("RelationshipWb is value data :{}", bussData);
        return null;
    }
}
