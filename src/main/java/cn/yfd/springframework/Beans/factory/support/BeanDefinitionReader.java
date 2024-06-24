package cn.yfd.springframework.Beans.factory.support;


import cn.yfd.springframework.core.io.Resource;
import cn.yfd.springframework.core.io.ResourceLoader;

public interface BeanDefinitionReader {

    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(Resource resource);

    void loadBeanDefinitions(Resource ...resources);

    void loadBeanDefinitions(String location);
}
