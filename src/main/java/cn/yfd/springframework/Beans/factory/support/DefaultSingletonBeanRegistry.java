package cn.yfd.springframework.Beans.factory.support;

import cn.yfd.springframework.Beans.BeansException;
import cn.yfd.springframework.Beans.factory.DisposableBean;
import cn.yfd.springframework.Beans.factory.ObjectFactory;
import cn.yfd.springframework.Beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    protected static final Object NULL_OBJECT = new Object();

    //一级缓存，存放普通对象
    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    //二级缓存，存放半成品对象，没有完全实例化的对象
    protected final Map<String, Object> earlySingletonObjects = new HashMap<>();

    //三级缓存，存放代理对象
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>();

    private final Map<String, DisposableBean> disposableBeans = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        Object singleObject = singletonObjects.get(beanName);
        if(singleObject == null){
            singleObject = earlySingletonObjects.get(beanName);
            if(singleObject == null){
                ObjectFactory<?> singletonFactory = singletonFactories.get(beanName);
                if(singletonFactory != null){
                    singleObject = singletonFactory.getObject();
                    earlySingletonObjects.put(beanName, singleObject);
                    singletonFactories.remove(beanName);
                }
            }
        }
        return singleObject;
    }

    @Override
    public void registerSingleton(String beanName, Object singleObject) {
        singletonObjects.put(beanName, singleObject);
        earlySingletonObjects.remove(beanName);
        singletonFactories.remove(beanName);
    }

    protected void addSingleFactory(String beanName, ObjectFactory<?> singletonFactory){
        if(!this.singletonObjects.containsKey(beanName)){
            this.singletonFactories.put(beanName, singletonFactory);
            this.earlySingletonObjects.remove(beanName);
        }
    }

    public void registerDisposableBean(String beanName, DisposableBean disposableBean){
        disposableBeans.put(beanName, disposableBean);
    }

    @Override
    public void destroySingletons() {
        Set<String> keySet = this.disposableBeans.keySet();
        Object[] disposableBeanNames = keySet.toArray();

        for(int i = disposableBeanNames.length-1; i >= 0; i--){
            Object beanName = disposableBeanNames[i];
            DisposableBean remove = disposableBeans.remove(beanName);
            try {
                remove.destroy();
            } catch (Exception e) {
                throw new BeansException("Destroy method on bean with name '"+beanName+"'throw an exception",e);
            }
        }
    }


}
