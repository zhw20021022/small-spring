package cn.yfd.springframework.Beans.factory.config;

import cn.yfd.springframework.Beans.PropertyValues;


public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor{

    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName);

    PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName);
}
