package cn.yfd.springframework.test.common;

import cn.yfd.springframework.Beans.BeansException;
import cn.yfd.springframework.Beans.PropertyValue;
import cn.yfd.springframework.Beans.PropertyValues;
import cn.yfd.springframework.Beans.factory.ConfigurableListableBeanFactory;
import cn.yfd.springframework.Beans.factory.config.BeanDefinition;
import cn.yfd.springframework.Beans.factory.config.BeanFactoryPostProcessor;

public class MyBeanPostFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessorBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("userService");
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("company", "改为：字节"));
    }
}
