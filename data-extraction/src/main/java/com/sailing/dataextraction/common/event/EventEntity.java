package com.sailing.dataextraction.common.event;

import com.sailing.dataextraction.entity.IndexJsonData;
import org.springframework.context.ApplicationEvent;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className EventEntity
 * @description 事件实体类
 * @date 2020/8/25 17:23
 **/
public class EventEntity extends ApplicationEvent {

    private IndexJsonData indexJsonData;

    public EventEntity(Object source, IndexJsonData indexJsonData) {
        super(source);
        this.indexJsonData = indexJsonData;
    }

    public IndexJsonData getIndexJsonData(){
        return this.indexJsonData;
    }
}
