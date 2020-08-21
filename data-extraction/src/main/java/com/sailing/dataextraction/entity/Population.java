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
 * @className Population
 * @description 人员信息
 * @date 2020/8/5 13:54
 **/
@NodeEntity(label = "Population")
@Data
@NoArgsConstructor
public class Population {

    @Id
    @GeneratedValue
    Long id;

    @Property(name = "uuid")
    private String uuid;

    @Property(name = "name")
    private String name;

    @Property(name = "idNumber")
    private String idNumber;

    @Property(name = "hohRelations")
    private String hohRelations;

    @Property(name = "hohName")
    private String hohName;

    @Property(name = "hohIdNumber")
    private String hohIdNumber;

    @Property(name = "familyNumber")
    private String familyNumber;

    @Property(name = "homeAddress")
    private String homeAddress;
}
