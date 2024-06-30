package cn.yfd.springframework.context;

import cn.yfd.springframework.Beans.BeansException;

public interface ConfigurableApplicationContext extends ApplicationContext{

    /**
     * 刷新容器
     */
    void refresh() throws BeansException;

    void registerShutDownHook();

    void close();
}
