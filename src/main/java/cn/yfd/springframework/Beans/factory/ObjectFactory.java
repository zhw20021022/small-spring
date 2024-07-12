package cn.yfd.springframework.Beans.factory;

import cn.yfd.springframework.Beans.BeansException;

public interface ObjectFactory<T> {
    T getObject() throws BeansException;
}
