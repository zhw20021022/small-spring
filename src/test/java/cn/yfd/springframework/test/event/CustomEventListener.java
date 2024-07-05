package cn.yfd.springframework.test.event;

import cn.yfd.springframework.context.ApplicationListener;

import java.util.Date;

public class CustomEventListener implements ApplicationListener<CustomEvent> {
    @Override
    public void onApplicationEvent(CustomEvent event) {
        System.out.println("收到： "+event.getMessage() + " 消息；时间:"+new Date());
        System.out.println("收到： "+event.getId() + " :"+event.getMessage());
    }
}
