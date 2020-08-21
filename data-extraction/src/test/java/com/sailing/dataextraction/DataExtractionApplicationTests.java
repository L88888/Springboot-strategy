package com.sailing.dataextraction;

import com.sailing.dataextraction.service.relationshipdataprocess.RelationshipContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataExtractionApplication.class)
class DataExtractionApplicationTests {

    @Autowired
    private RelationshipContext relationshipContext;

    @Test
    void contextLoads() {
        // 构造酒店数据
        Map<String, String> bussData =new HashMap<>();
        bussData.put("name","内大华 酒店001");
        // 酒店实例对象
        Object resData = relationshipContext.getRelationshipImpl("relationshipHotelImpl", bussData);
        log.info("test hotel value:>{}", resData);

        // 构造网吧数据
        Map<String, String> wbData =new HashMap<>();
        wbData.put("name", "金悦网吧A001");
        // 网吧实例对象
        resData = relationshipContext.getRelationshipImpl("relationshipWb", wbData);
        log.info("test Internet value:>{}", resData);
    }

}
