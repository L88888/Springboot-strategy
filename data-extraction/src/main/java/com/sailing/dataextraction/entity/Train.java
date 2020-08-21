package com.sailing.dataextraction.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className Train
 * @description 火车信息
 * @date 2020/8/6 8:45
 **/
@NodeEntity(label = "Train")
@Data
@NoArgsConstructor
public class Train {

    @Id
    @GeneratedValue
    Long id;

    @Property(name = "uuid")
    private String uuid;

    @Property(name = "trainNumber")
    private String trainNumber;

    @Property(name = "departureTime")
    private String departureTime;

    @Property(name = "dt")
    private String dt;

    @Property(name = "rt")
    private String rt;

    @Property(name = "passengers")
    private String passengers;

    @Property(name = "idNumber")
    private String idNumber;

    @Property(name = "title")
    private String title;

    @Property(name = "time")
    private Long time;
}
