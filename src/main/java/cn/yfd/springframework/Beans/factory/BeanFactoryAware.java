package cn.yfd.springframework.Beans.factory;

import cn.yfd.springframework.Beans.BeansException;

public interface BeanFactoryAware extends Aware{

    void setBeanFactory(BeanFactory beanFactory) throws BeansException;

}
