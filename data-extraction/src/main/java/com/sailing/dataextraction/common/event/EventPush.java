package com.sailing.dataextraction.common.event;

import com.sailing.dataextraction.entity.IndexJsonData;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className EventPush
 * @description 事件推送
 * @date 2020/8/10 18:03
 **/
@Component
public class EventPush implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Async
    public void messagePush(IndexJsonData indexJsonData){
        publisher.publishEvent(new EventEntity(this, indexJsonData));
    }
}
