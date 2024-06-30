package cn.yfd.springframework.Beans.factory;

import cn.yfd.springframework.Beans.BeansException;

public interface BeanClassLoaderAware extends Aware{

    void setBeanClassLoader(ClassLoader classLoader) ;

}
