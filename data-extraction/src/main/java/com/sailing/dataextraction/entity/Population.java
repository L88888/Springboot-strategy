package com.sailing.dataextraction.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.*;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className Population
 * @description 人员信息
 * @date 2020/8/5 13:54
 **/
@NodeEntity(label = "Population")
@Data
@NoArgsConstructor
public class Population implements Peers{


//    @Id
//    @Property(name = "uuid")
//    private String uuid;
//
//    @Property(name = "name")
//    private String name;
//
//    @Property(name = "idNumber")
//    private String idNumber;
//
//    @Property(name = "hohRelations")
//    private String hohRelations;
//
//    @Property(name = "hohName")
//    private String hohName;
//
//    @Property(name = "hohIdNumber")
//    private String hohIdNumber;
//
//    @Property(name = "familyNumber")
//    private String familyNumber;
//
//    @Property(name = "homeAddress")
//    private String homeAddress;
//
//    @Property(name = "ssTime")
//    private Long ssTime;

    @Id
    @Property(name = "uuid")
    private String uuid;

    @Property(name = "name")
    private String xm;

    @Property(name = "idNumber")
    private String gmsfhm;

    @Property(name = "hohRelations")
    private String yhzgx;

    @Property(name = "hohName")
    private String hz_xm;

    @Property(name = "hohIdNumber")
    private String hz_gmsfhm;

    @Property(name = "familyNumber")
    private String hh;

    @Property(name = "homeAddress")
    private String hjd_dzmc;

    @Property(name = "ssTime")
    private Long ssTime;
}
