package cn.yfd.springframework.Beans.factory.annotation;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.yfd.springframework.Beans.BeansException;
import cn.yfd.springframework.Beans.PropertyValues;
import cn.yfd.springframework.Beans.factory.BeanFactory;
import cn.yfd.springframework.Beans.factory.BeanFactoryAware;
import cn.yfd.springframework.Beans.factory.ConfigurableListableBeanFactory;
import cn.yfd.springframework.Beans.factory.config.InstantiationAwareBeanPostProcessor;
import cn.yfd.springframework.util.ClassUtils;

import java.lang.reflect.Field;

public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) {
        Class<?> clazz = bean.getClass();
        clazz = ClassUtils.isCGlibProxyClass(clazz) ? clazz.getSuperclass() : clazz;

        Field[] declaredFields = clazz.getDeclaredFields();

        for (Field field : declaredFields) {
            Value valueAnnotation = field.getAnnotation(Value.class);
            if(valueAnnotation != null){
                String value = valueAnnotation.value();
                value = beanFactory.resolveEmbeddedValue(value);
                BeanUtil.setFieldValue(bean, field.getName(), value);
            }
        }

        for (Field declaredField : declaredFields) {
            Autowired autowiredAnnotation = declaredField.getAnnotation(Autowired.class);
            if(autowiredAnnotation != null){
                Class<?> fieldType = declaredField.getType();
                String dependentBeanName = null;
                Qualifier qualifierAnnotation = declaredField.getAnnotation(Qualifier.class);
                Object dependentBean = null;
                if(qualifierAnnotation != null){
                    dependentBeanName = qualifierAnnotation.value();
                    dependentBean = beanFactory.getBean(dependentBeanName,fieldType);
                }else{
                    dependentBean = beanFactory.getBean(fieldType);
                }
                BeanUtil.setFieldValue(bean, declaredField.getName(), dependentBean);
            }
        }
        return pvs;
    }


    @Override
    public Object postProcessorBeforeInitialization(Object bean, String beanName) {
        return null;
    }

    @Override
    public Object postProcessorAfterInitialization(Object bean, String beanName) {
        return null;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String name) {
        return true;
    }


}
