package cn.yfd.springframework.test.event;

import cn.yfd.springframework.context.ApplicationListener;
import cn.yfd.springframework.context.event.ContextRefreshedEvent;

public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("刷新事件"+this.getClass().getName());
    }
}
