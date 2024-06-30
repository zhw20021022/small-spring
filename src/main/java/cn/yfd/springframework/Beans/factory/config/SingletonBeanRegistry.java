package cn.yfd.springframework.Beans.factory.config;

/**
 * 单例bean注册表
 */
public interface SingletonBeanRegistry {
    Object getSingleton(String beanName);

    //销毁单例对象
    void destroySingletons();
}
