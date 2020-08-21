package com.sailing.di.sysservice.postgres.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface SysrwbMapper {

    public String getRyzpzxsj();

    public int updateRwzhgxsj(Map params);

    public List queryRwlb();

}
