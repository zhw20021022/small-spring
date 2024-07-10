package cn.yfd.springframework.Beans.factory.support;

import cn.yfd.springframework.Beans.BeansException;
import cn.yfd.springframework.Beans.factory.BeanFactory;
import cn.yfd.springframework.Beans.factory.FactoryBean;
import cn.yfd.springframework.Beans.factory.config.BeanDefinition;
import cn.yfd.springframework.Beans.factory.config.BeanPostProcessor;
import cn.yfd.springframework.Beans.factory.config.ConfigurableBeanFactory;
import cn.yfd.springframework.util.ClassUtils;
import cn.yfd.springframework.util.StringValueResolver;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    private final List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    private final List<StringValueResolver> embeddedValueResolvers = new ArrayList<>();

    @Override
    public Object getBean(String name) throws BeansException{
        Object bean = doGetBean(name, null);
        return bean;
    }

    @Override
    public Object getBean(String name, Object...args) throws BeansException{
        Object bean = doGetBean(name, args);
        return bean;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return (T) getBean(name);
    }

    protected <T> T doGetBean(final String name, final Object[] args){
        Object sharedInstance = getSingleton(name);
        if(sharedInstance != null){
            return (T) getObjectForBeanInstance(sharedInstance, name);
        }

        BeanDefinition beanDefinition = getBeanDefinition(name);
        Object bean1 = createBean(name, beanDefinition, args);
        return (T) getObjectForBeanInstance(bean1, name);
    }

    private Object getObjectForBeanInstance(Object beanInstance, String beanName){
        if(!(beanInstance instanceof FactoryBean)){
            return beanInstance;
        }

        Object object = getCachedObjectForFactoryBean(beanName);

        if(object == null){
            FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;
            object = getObjectFromFactoryBean(factoryBean, beanName);
        }
        return object;
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessorList.remove(beanPostProcessor);
        this.beanPostProcessorList.add(beanPostProcessor);
    }

    @Override
    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        this.embeddedValueResolvers.add(valueResolver);
    }

    @Override
    public String resolveEmbeddedValue(String value) {
        String result = value;
        for (StringValueResolver resolver : this.embeddedValueResolvers) {
            result = resolver.resolveStringValue(value);
        }
        return result;
    }

    public List<BeanPostProcessor> getBeanPostProcessors(){
        return beanPostProcessorList;
    }

    public ClassLoader getClassLoader(){
        return this.beanClassLoader;
    }
}
