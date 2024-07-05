package cn.yfd.springframework.context.event;

import cn.yfd.springframework.context.ApplicationEvent;
import cn.yfd.springframework.context.ApplicationListener;

public interface ApplicationEventMulticaster {

    /**
     * 监听事件
     * @param listener
     */
    void addApplicationListener(ApplicationListener<?> listener);

    /**
     * 删除监听
     * @param listener
     */
    void removeApplicationListener(ApplicationListener<?> listener);

    /**
     * 广播事件
     * @param event
     */
    void multicastEvent(ApplicationEvent event);

}
