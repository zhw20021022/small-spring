package cn.yfd.springframework.Beans.factory.support;

import cn.yfd.springframework.Beans.BeansException;
import cn.yfd.springframework.Beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {
    /**
     * 向注册表中注册
     * @param beanName
     * @param beanDefinition
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    /**
     * 使用Bean名称查询BeanDefinition
     * @param beanName
     * @return
     * @throws BeansException
     */
    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    /**
     * 判断是否包含指定名称的BeanDefinition
     * @param beanName
     * @return
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * 返回所有Bean的名称
     * @return
     */
    String[] getBeanDefinitionNames();
}
