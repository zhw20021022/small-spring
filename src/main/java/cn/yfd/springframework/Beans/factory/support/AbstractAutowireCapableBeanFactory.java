package cn.yfd.springframework.Beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import cn.yfd.springframework.Beans.BeansException;
import cn.yfd.springframework.Beans.PropertyValue;
import cn.yfd.springframework.Beans.PropertyValues;
import cn.yfd.springframework.Beans.factory.config.AutowireCapableBeanFactory;
import cn.yfd.springframework.Beans.factory.config.BeanDefinition;
import cn.yfd.springframework.Beans.factory.config.BeanPostProcessor;
import cn.yfd.springframework.Beans.factory.config.BeanReference;

import java.lang.reflect.Constructor;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    InstantiationStrategy instantiationStrategy = new CglibSubClassingInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args){
        Object bean = null;
        try{
            bean = createBeanInstance(beanName, beanDefinition, args);
            //给bean填充属性
            applyPropertyValues(beanName, bean, beanDefinition);
            //执行bean的初始化方法，和前置后置处理方法
            bean = initializeBean(beanName, bean, beanDefinition);
        }catch (Exception e) {
            throw new BeansException("Instantiation of bean failed", e);
        }
        addSingleton(beanName, bean);
        return bean;
    }

    protected Object createBeanInstance(String beanName, BeanDefinition beanDefinition, Object[] arg){
        Constructor constructor = null;
        Class beanClass = beanDefinition.getBeanClass();
        Constructor[] declaredConstructors = beanClass.getDeclaredConstructors();
        for (Constructor declaredConstructor : declaredConstructors) {
            if(arg != null && arg.length == declaredConstructor.getParameterTypes().length){
                constructor = declaredConstructor;
                break;
            }
        }

        Object instantiate = getInstantiationStrategy().instantiate(beanDefinition, beanName, constructor, arg);
        return instantiate;
    }

    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition){
        try {
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();

                if(value instanceof BeanReference){
                    BeanReference v = (BeanReference) value;
                    value = getBean(v.getBeanName());
                }
                BeanUtil.setFieldValue(bean, name, value);
            }
        } catch (BeansException e) {
            throw new BeansException("Error setting property value"+beanName);
        }
    }

    public InstantiationStrategy getInstantiationStrategy(){
        return instantiationStrategy;
    }

    public void SetInstantiationStrategy(InstantiationStrategy instantiationStrategy){
        this.instantiationStrategy = instantiationStrategy;
    }

    public Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition){
        Object wrappedBean = applyBeanPostProcessorBeforeInitiation(bean, beanName);

        //待完成处理
        invokeInitMethods(beanName, wrappedBean, beanDefinition);

        wrappedBean = applyBeanPostProcessorAfterInitiation(wrappedBean, beanName);

        return wrappedBean;
    }

    public void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition){

    }

    public Object applyBeanPostProcessorBeforeInitiation(Object existingBean, String beanName) throws BeansException{
        Object result = existingBean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object current = beanPostProcessor.postProcessorBeforeInitialization(result, beanName);
            if(current == null) return result;
            result = current;
        }
        return result;
    }

    public Object applyBeanPostProcessorAfterInitiation(Object existingBean, String beanName) throws BeansException{
        Object result = existingBean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object current = beanPostProcessor.postProcessorAfterInitialization(result, beanName);
            if(current == null) return result;
            result = current;
        }
        return result;
    }
}
