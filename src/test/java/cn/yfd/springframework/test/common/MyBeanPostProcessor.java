package cn.yfd.springframework.test.common;

import cn.yfd.springframework.Beans.factory.config.BeanPostProcessor;
import cn.yfd.springframework.test.bean.UserService;

public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessorBeforeInitialization(Object bean, String beanName) {
        if("userService".equals(beanName)){
            UserService userService = (UserService) bean;
            userService.setLocation("改为：北京");
        }
        return bean;
    }

    @Override
    public Object postProcessorAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
