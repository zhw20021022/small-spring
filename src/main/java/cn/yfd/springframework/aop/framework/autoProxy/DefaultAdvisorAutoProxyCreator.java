package cn.yfd.springframework.aop.framework.autoProxy;

import cn.yfd.springframework.Beans.BeansException;
import cn.yfd.springframework.Beans.PropertyValues;
import cn.yfd.springframework.Beans.factory.BeanFactory;
import cn.yfd.springframework.Beans.factory.BeanFactoryAware;
import cn.yfd.springframework.Beans.factory.config.InstantiationAwareBeanPostProcessor;
import cn.yfd.springframework.Beans.factory.support.DefaultListableBeanFactory;
import cn.yfd.springframework.aop.*;
import cn.yfd.springframework.aop.aspectJ.AspectJExpressionPointcutAdvisor;
import cn.yfd.springframework.aop.framework.ProxyFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.Collection;

public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }


    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String name) {
        return true;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) {
        return pvs;
    }

    private boolean isInfrastructureClass(Class<?> beanClass){
        return Advice.class.isAssignableFrom(beanClass) || Advisor.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass);
    }

    @Override
    public Object postProcessorBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override
    public Object postProcessorAfterInitialization(Object bean, String beanName) {
        if(isInfrastructureClass(bean.getClass())) return bean;
        Collection<AspectJExpressionPointcutAdvisor> advisors =  beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();

        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();

            if(!classFilter.matches(bean.getClass())) continue;

            AdvisedSupport advisedSupport = new AdvisedSupport();
            TargetSource targetSource = new TargetSource(bean);

            advisedSupport.setTargetSource(targetSource);
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            advisedSupport.setProxyTargetClass(false);

            return new ProxyFactory(advisedSupport).getProxy();
        }
        return bean;
    }
}
