package cn.yfd.springframework.context.support;

import cn.yfd.springframework.Beans.factory.ConfigurableListableBeanFactory;
import cn.yfd.springframework.Beans.factory.support.DefaultListableBeanFactory;

public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext{

    private DefaultListableBeanFactory beanFactory;

    @Override
    protected void refreshBeanFactory() {
        DefaultListableBeanFactory factory = createBeanFactory();
        loadBeanDefinitions(factory);
        this.beanFactory = factory;

    }

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory factory);

    private DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    @Override
    protected ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
}
