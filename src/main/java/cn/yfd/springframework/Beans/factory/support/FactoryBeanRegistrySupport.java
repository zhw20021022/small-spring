package cn.yfd.springframework.Beans.factory.support;

import cn.yfd.springframework.Beans.BeansException;
import cn.yfd.springframework.Beans.factory.FactoryBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry{

    private final Map<String, Object> factoryObjectBeanCache = new ConcurrentHashMap<>();

    protected Object getCachedObjectForFactoryBean(String beanName){
        Object object = this.factoryObjectBeanCache.get(beanName);
        return (object != NULL_OBJECT ? object : null);
    }

    protected Object getObjectFromFactoryBean(FactoryBean factoryBean, String beanName){
        if(factoryBean.isSingleton()){
            Object object = this.factoryObjectBeanCache.get(beanName);
            if(object == null){
                object = doGetFactoryFromFactoryBean(factoryBean, beanName);
                this.factoryObjectBeanCache.put(beanName, (object != null ? object:NULL_OBJECT));
            }
            return object != NULL_OBJECT ? object : null;
        }else{
            return doGetFactoryFromFactoryBean(factoryBean, beanName);
        }
    }

    private Object doGetFactoryFromFactoryBean(final FactoryBean factory, final String beanName){
        try{
            return factory.getObject();
        }catch (Exception e){
            throw new BeansException("FactoryBean throw exception on object ["+beanName+"] creation",e);
        }
    }
}
