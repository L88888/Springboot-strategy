package com.sailing.dataextraction;

import com.sailing.dataextraction.dao.PopulationRepository;
import com.sailing.dataextraction.entity.Peers;
import com.sailing.dataextraction.service.relationshipdataprocess.RelationshipContext;
import com.sailing.dataextraction.entity.Population;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * @program: ZY-device-sync
 * @description:
 * @author: LIULEI
 * @create: 2020-09-15 11:43:
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataExtractionApplication.class)
public class DataExtractionApplicationTests {

    @Autowired
    private RelationshipContext relationshipContext;

    @Autowired
    private PopulationRepository populationRepository;

    /**
     * 策略模式存储业务数据对象
     */
    @Test
    void contextLoads() {
        // 构造酒店数据
        Map<String, String> bussData =new HashMap<>();
        bussData.put("name","内达华 酒店001");
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


    /**
     * 批量人员数据写入Neo4j
     */
    @Test
    void batchPersionDataToNeo4j(){
        long starttime = System.currentTimeMillis();
        System.out.println("开始存储人员信息至neo4j.");
        long timeNow = System.currentTimeMillis();

        Map<String, Object> datum = new HashMap();
        datum.put("xm","马克思");
        datum.put("gmsfhm","1234567890");
        datum.put("yhzgx","TEST01");
        datum.put("hz_xm","马克而");
        datum.put("hz_gmsfhm","123456789012345678");
        datum.put("hh","1234567");
        datum.put("hjd_dzmc","NO.德国柏林韦德路003号");
        datum.put("ssTime", timeNow);

        boolean isBatch = false;
        isBatch = true;

        Population population = null;
        // 开始打桩实体对象Datum
        List requestData = new ArrayList();
        int sizeData = 5000;
        for (int i = 0;i< sizeData;i++){
            population = new Population();

            datum.put("uuid", UUID.randomUUID().toString().replaceAll("-",""));

            // 对象拷贝
            BeanMap beanMap = BeanMap.create(population);
            beanMap.putAll(datum);
            // 添加集合
            requestData.add(population);
            if (!isBatch){
                Population ps = populationRepository.save(population);
                System.out.println("单个存储人员信息至neo4j完成."+ ps.toString());
            }
        }

        if (isBatch){
            populationRepository.saveAll(requestData);
            System.out.println("批量存储人员信息至neo4j完成.");
        }

        long endtime = System.currentTimeMillis();
        System.out.println("存储人员实体对象完成,耗时:>>"+(endtime - starttime));
    }

    @Test
    void testPeers(){
        Peers peers = null;
        long timeNow = System.currentTimeMillis();

        Map<String, Object> datum = new HashMap();
        datum.put("xm","马克思A0915");
        datum.put("gmsfhm","1234567890");
        datum.put("yhzgx","TEST01");
        datum.put("hz_xm","马克而");
        datum.put("hz_gmsfhm","123456789012345678");
        datum.put("hh","1234567");
        datum.put("hjd_dzmc","NO.德国柏林韦德路003号");
        datum.put("ssTime", timeNow);
        datum.put("uuid", UUID.randomUUID().toString().replaceAll("-",""));
        // 人员同行对象
        Population population = new Population();
        // 对象拷贝
        BeanMap beanMap = BeanMap.create(population);
        beanMap.putAll(datum);
        peers = population;

        if (false){
            // 单对象存储
            Population ps = populationRepository.save((Population) peers);
            System.out.println("单个存储人员信息至neo4j完成."+ ps.toString());
            System.out.println(peers);
        }

        List<Peers> peersSet = new ArrayList<>();
        peersSet.add(peers);

        Peers peersto = null;
        Map<String, Object> datums = new HashMap();
        datums.put("xm","马克思A0915");
        datums.put("gmsfhm","1234567890");
        datums.put("yhzgx","TEST01");
        datums.put("hz_xm","马克而");
        datums.put("hz_gmsfhm","123456789012345678");
        datums.put("hh","1234567");
        datums.put("hjd_dzmc","NO.德国柏林韦德路003号");
        datums.put("ssTime", timeNow);
        datums.put("uuid", UUID.randomUUID().toString().replaceAll("-",""));
        // 人员同行对象
        Population populationto = new Population();
        // 对象拷贝
        BeanMap beanMapto = BeanMap.create(populationto);
        beanMapto.putAll(datums);
        peersto = populationto;
        peersSet.add(peersto);

        System.out.println("同行对象集合信息为:>" + peersSet);
        // 批量对象存储
        populationRepository.saveAll((List)peersSet);
    }
}
