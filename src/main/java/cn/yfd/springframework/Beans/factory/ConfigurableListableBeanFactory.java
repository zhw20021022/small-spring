package cn.yfd.springframework.Beans.factory;

import cn.yfd.springframework.Beans.BeansException;
import cn.yfd.springframework.Beans.factory.config.AutowireCapableBeanFactory;
import cn.yfd.springframework.Beans.factory.config.BeanDefinition;
import cn.yfd.springframework.Beans.factory.config.ConfigurableBeanFactory;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory, ConfigurableBeanFactory, AutowireCapableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    void preInstantiateSingletons() throws BeansException;

}
