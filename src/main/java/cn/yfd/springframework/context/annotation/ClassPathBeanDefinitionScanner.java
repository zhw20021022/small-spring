package cn.yfd.springframework.context.annotation;

import cn.hutool.core.util.StrUtil;
import cn.yfd.springframework.Beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import cn.yfd.springframework.Beans.factory.config.BeanDefinition;
import cn.yfd.springframework.Beans.factory.support.BeanDefinitionRegistry;
import cn.yfd.springframework.stereotype.Component;

import java.util.Set;

public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider{

    private BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void doScan(String... basePackages){
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition beanDefinition : candidates) {
                String beanScope = resolveBeanScope(beanDefinition);
                if(StrUtil.isNotEmpty(beanScope)){
                    beanDefinition.setScope(beanScope);
                }
                registry.registerBeanDefinition(determineBeanName(beanDefinition), beanDefinition);
            }
        }

        registry.registerBeanDefinition("cn.yfd.springframework.context.annotation.internalAutowiredAnnotationProcessor", new BeanDefinition(AutowiredAnnotationBeanPostProcessor.class));
    }

    private String resolveBeanScope(BeanDefinition beanDefinition){
        Class<?> beanClass = beanDefinition.getBeanClass();
        Scope scope = beanClass.getAnnotation(Scope.class);
        if(scope != null){
            return scope.value();
        }
        return StrUtil.EMPTY;
    }

    private String determineBeanName(BeanDefinition beanDefinition){
        Class<?> clazz = beanDefinition.getBeanClass();
        Component component = clazz.getAnnotation(Component.class);
        String value = component.value();
        if(StrUtil.isEmpty(value)){
            value = StrUtil.lowerFirst(clazz.getSimpleName());
        }
        return value;
    }
}
