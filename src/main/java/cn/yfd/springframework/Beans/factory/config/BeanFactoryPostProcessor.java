package cn.yfd.springframework.Beans.factory.config;

import cn.yfd.springframework.Beans.factory.ConfigurableListableBeanFactory;

/**
 * 允许自定义修改BeanDefinition属性信息
 */
public interface BeanFactoryPostProcessor {

    /**
     * 在所有的BeanDefinition加载完成后，实例化Bean对象，提供修改BeanDefinition属性机制
     * @param beanFactory
     */
    void postProcessorBeanFactory(ConfigurableListableBeanFactory beanFactory);
}
