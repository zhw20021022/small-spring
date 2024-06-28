package cn.yfd.springframework.Beans.factory.config;

/**
 * 用于修改实例化Bean对象的扩展点
 */
public interface BeanPostProcessor {

    /**
     * 在Bean对象执行初始化方法之前，执行此方法
     * @param bean
     * @param beanName
     * @return
     */
    Object postProcessorBeforeInitialization(Object bean, String beanName);

    /**
     * 在Bean对象执行初始化后，执行此方法
     * @param bean
     * @param beanName
     * @return
     */
    Object postProcessorAfterInitialization(Object bean, String beanName);

}
