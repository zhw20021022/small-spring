package cn.yfd.springframework.Beans.factory.config;

import cn.yfd.springframework.Beans.PropertyValues;


public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor{

    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName);

    boolean postProcessAfterInstantiation(Object bean, String name);

    PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName);
}
