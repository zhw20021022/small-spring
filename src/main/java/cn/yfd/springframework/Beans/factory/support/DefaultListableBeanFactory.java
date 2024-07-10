package cn.yfd.springframework.Beans.factory.support;

import cn.yfd.springframework.Beans.BeansException;
import cn.yfd.springframework.Beans.factory.ConfigurableListableBeanFactory;
import cn.yfd.springframework.Beans.factory.config.BeanDefinition;

import java.util.*;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if(beanDefinition == null){
            throw new BeansException("No bean named '"+beanName);
        }
        return beanDefinition;
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        Set<String> beanSet = beanDefinitionMap.keySet();
        return beanSet.toArray(new String[0]);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException{
        Map<String, T> result = new HashMap<>();
        beanDefinitionMap.forEach((beanName, beanDefinition)->{
            Class beanClass = beanDefinition.getBeanClass();
            if(type.isAssignableFrom(beanClass)){
                result.put(beanName, (T) getBean(beanName));
            }
        });
        return result;
    }

    @Override
    public void preInstantiateSingletons() throws BeansException {
        Set<String> keys = beanDefinitionMap.keySet();
        for (String key : keys) {
            this.getBean(key);
        }
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        List<String> beanNames = new ArrayList<>();
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            Class beanClass = entry.getValue().getBeanClass();
            if(requiredType.isAssignableFrom(beanClass)){
                beanNames.add(entry.getKey());
            }
        }
        if(beanNames.size() == 1){
            return getBean(beanNames.get(0), requiredType);
        }

        throw new BeansException(requiredType + "expected single bean but found " + beanNames.size() +":" +beanNames);
    }
}
