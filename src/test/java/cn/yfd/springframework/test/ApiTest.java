package cn.yfd.springframework.test;

import cn.yfd.springframework.Beans.factory.config.BeanDefinition;
import cn.yfd.springframework.Beans.factory.support.DefaultListableBeanFactory;
import cn.yfd.springframework.test.bean.UserService;
import org.junit.Test;

public class ApiTest {

    @Test
    public void test_BeanFactory(){
        //1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        //2.注册 bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        beanFactory.registerBeanDefinition("userService", beanDefinition);

        //3.第一次获取bean
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();
        System.out.println(userService.hashCode());

        //4.第一次获取bean
        UserService userService_singleton = (UserService) beanFactory.getSingleton("userService");
        userService.queryUserInfo();
        System.out.println(userService_singleton.hashCode());
    }
}
