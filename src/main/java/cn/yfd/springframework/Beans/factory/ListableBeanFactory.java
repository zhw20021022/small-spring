package cn.yfd.springframework.Beans.factory;

import cn.yfd.springframework.Beans.BeansException;

import java.util.Map;

public interface ListableBeanFactory extends BeanFactory{

    /**
     * 按照类型返回Bean的实例
     * @param type
     * @return
     * @param <T>
     * @throws BeansException
     */
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

    /**
     * 返回注册表中所有Bean的名称
     * @return
     */
    String[] getBeanDefinitionNames();
}
