package com.sailing.di.sysservice.oracle.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
public interface DagrjbxxZPMapper {

    public List queryDagrjbxxZP(String tjsj);

    public String getRyzpzxsj();

}
