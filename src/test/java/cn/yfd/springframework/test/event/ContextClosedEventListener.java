package cn.yfd.springframework.test.event;

import cn.yfd.springframework.context.ApplicationListener;
import cn.yfd.springframework.context.event.ContextClosedEvent;

public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("关闭事件"+event.getClass().getName());
    }
}
