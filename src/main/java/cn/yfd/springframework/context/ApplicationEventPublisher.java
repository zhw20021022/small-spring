package cn.yfd.springframework.context;

import cn.yfd.springframework.context.ApplicationEvent;

public interface ApplicationEventPublisher {

    void publishEvent(ApplicationEvent event);
}
