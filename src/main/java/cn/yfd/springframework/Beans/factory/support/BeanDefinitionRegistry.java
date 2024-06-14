package cn.yfd.springframework.Beans.factory.support;

import cn.yfd.springframework.Beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {
    /**
     * 向注册表中注册
     * @param beanName
     * @param beanDefinition
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}
