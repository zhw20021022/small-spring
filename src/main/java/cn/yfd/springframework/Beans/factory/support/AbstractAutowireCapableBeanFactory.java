package cn.yfd.springframework.Beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.yfd.springframework.Beans.BeansException;
import cn.yfd.springframework.Beans.PropertyValue;
import cn.yfd.springframework.Beans.PropertyValues;
import cn.yfd.springframework.Beans.factory.*;
import cn.yfd.springframework.Beans.factory.config.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    InstantiationStrategy instantiationStrategy = new CglibSubClassingInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args){
        // 判断是否返回代理 Bean 对象
        Object bean = resolveBeforeInstantiation(beanName, beanDefinition);
        if (null != bean) {
            return bean;
        }

        return doCreateBean(beanName, beanDefinition, args);
    }

    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition, Object[] args) {
        Object bean = null;
        try {
            // 实例化 Bean
            bean = createBeanInstance(beanName, beanDefinition, args);

            // 处理循环依赖，将实例化后的Bean对象提前放入缓存中暴露出来
            if (beanDefinition.isSingleton()) {
                Object finalBean = bean;
                addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, beanDefinition, finalBean));
            }

            // 实例化后判断
            boolean continueWithPropertyPopulation = applyBeanPostProcessorsAfterInstantiation(beanName, bean);
            if (!continueWithPropertyPopulation) {
                return bean;
            }
            // 在设置 Bean 属性之前，允许 BeanPostProcessor 修改属性值
            applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);
            // 给 Bean 填充属性
            applyPropertyValues(beanName, bean, beanDefinition);
            // 执行 Bean 的初始化方法和 BeanPostProcessor 的前置和后置处理方法
            bean = initializeBean(beanName, bean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed", e);
        }

        // 注册实现了 DisposableBean 接口的 Bean 对象
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);

        // 判断 SCOPE_SINGLETON、SCOPE_PROTOTYPE
        Object exposedObject = bean;
        if (beanDefinition.isSingleton()) {
            // 获取代理对象
            exposedObject = getSingleton(beanName);
            registerSingleton(beanName, exposedObject);
        }
        return exposedObject;

    }

    private boolean applyBeanPostProcessorsAfterInstantiation(String beanName, Object bean) {
        boolean continueWithPropertyPopulation = true;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                InstantiationAwareBeanPostProcessor instantiationAwareBeanPostProcessor = (InstantiationAwareBeanPostProcessor) beanPostProcessor;
                if (!instantiationAwareBeanPostProcessor.postProcessAfterInstantiation(bean, beanName)) {
                    continueWithPropertyPopulation = false;
                    break;
                }
            }
        }
        return continueWithPropertyPopulation;
    }

    protected Object getEarlyBeanReference(String beanName, BeanDefinition beanDefinition, Object bean) {
        Object exposedObject = bean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                exposedObject = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).getEarlyBeanReference(exposedObject, beanName);
                if (null == exposedObject) return exposedObject;
            }
        }

        return exposedObject;
    }

    protected void applyBeanPostProcessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition){
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if(beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
                PropertyValues pvs = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);
                if(pvs != null){
                    for (PropertyValue propertyValue : pvs.getPropertyValues()) {
                        beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
                    }
                }
            }
        }
    }

    protected Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition){
        Object bean = applyBeanPostProcessorsBeforeInstantiation(beanDefinition.getBeanClass(), beanName);
        if(bean != null){
            bean = applyBeanPostProcessorAfterInitialization(bean, beanName);
        }
        return bean;

    }

    protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName){
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if(beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
                Object result = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessBeforeInstantiation(beanClass, beanName);
                if(result != null){
                    return result;
                }
            }
        }
        return null;
    }



    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition){
        //若不是单例Bean,则不执行销毁方法
        if(!(beanDefinition.isSingleton())){
            return;
        }

        if(bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())){
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }
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

        if(bean instanceof Aware){
            if(bean instanceof BeanFactoryAware){
                ((BeanFactoryAware)bean).setBeanFactory(this);
            }
            if(bean instanceof BeanClassLoaderAware){
                ((BeanClassLoaderAware) bean).setBeanClassLoader(getClassLoader());
            }
            if(bean instanceof BeanNameAware){
                ((BeanNameAware)bean).setBeanName(beanName);
            }
        }

        Object wrappedBean = applyBeanPostProcessorBeforeInitialization(bean, beanName);

        try {
            //执行bean对象初始化方法
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Invocation of init method of bean["+ beanName+"] failed", e);
        }

        wrappedBean = applyBeanPostProcessorAfterInitialization(wrappedBean, beanName);

        return wrappedBean;
    }

    public void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {
        if(bean instanceof InitializingBean){
            InitializingBean initializingBean = (InitializingBean) bean;
            initializingBean.afterPropertySet();
        }
        String initMethodName = beanDefinition.getInitMethodName();
        if(StrUtil.isNotEmpty(initMethodName)){
            Method initMethod = beanDefinition.getBeanClass().getMethod(initMethodName);
            if(initMethod == null){
                throw new BeansException("Could not find an init method named '"+ initMethodName +"' on bean with name '"+beanName+"'");
            }
            initMethod.invoke(bean);
        }
    }

    public Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName) throws BeansException{
        Object result = existingBean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object current = beanPostProcessor.postProcessorBeforeInitialization(result, beanName);
            if(current == null) return result;
            result = current;
        }
        return result;
    }

    public Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName) throws BeansException{
        Object result = existingBean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object current = beanPostProcessor.postProcessorAfterInitialization(result, beanName);
            if(current == null) return result;
            result = current;
        }
        return result;
    }
}
