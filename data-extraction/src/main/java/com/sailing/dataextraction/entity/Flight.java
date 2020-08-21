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
 * @className Flight
 * @description 飞机信息
 * @date 2020/8/6 9:10
 **/
@NodeEntity(label = "Flight")
@Data
@NoArgsConstructor
public class Flight {
    @Id
    @GeneratedValue
    Long id;

    @Property(name = "uuid")
    private String uuid;

    @Property(name = "flightNumber")
    private String flightNumber;

    @Property(name = "airlines")
    private String airlines;

    @Property(name = "departureTime")
    private String departureTime;

    @Property(name = "dtCode")
    private String dtCode;

    @Property(name = "dt")
    private String dt;

    @Property(name = "rtCode")
    private String rtCode;

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
