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
 * @className Hotel
 * @description 旅店信息
 * @date 2020/8/6 13:52
 **/
@NodeEntity(label = "Hotel")
@Data
@NoArgsConstructor
public class Hotel implements Peers{

    @Id
    @Property(name = "uuid")
    private String uuid;

    @Property(name = "placeNumber")
    private String placeNumber;

    @Property(name = "departureTime")
    private String departureTime;

    @Property(name = "dt")
    private String dt;

    @Property(name = "passengers")
    private String passengers;

    @Property(name = "idNumber")
    private String idNumber;

    @Property(name = "title")
    private String title;

    @Property(name = "time")
    private Long time;

    @Property(name = "ssTime")
    private Long ssTime;
}
