package cn.yfd.springframework.Beans.factory;

import cn.yfd.springframework.Beans.BeansException;

public interface FactoryBean<T> {

    T getObject() throws Exception;

    Class<?> getObjectType();

    boolean isSingleton();
}
