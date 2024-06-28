package cn.yfd.springframework.context.support;

import cn.yfd.springframework.Beans.factory.support.DefaultListableBeanFactory;
import cn.yfd.springframework.Beans.factory.xml.XMLBeanDefinitionReader;

public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext{
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory factory) {
        XMLBeanDefinitionReader beanDefinitionReader = new XMLBeanDefinitionReader(factory, this);
        String[] configLocations = getConfigLocations();
        if(configLocations != null){
            beanDefinitionReader.loadBeanDefinitions(configLocations);
        }
    }

    protected abstract String[] getConfigLocations() ;
}
