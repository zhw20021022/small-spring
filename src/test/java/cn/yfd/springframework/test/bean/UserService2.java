package cn.yfd.springframework.test.bean;

import cn.yfd.springframework.Beans.BeansException;
import cn.yfd.springframework.Beans.factory.*;
import cn.yfd.springframework.context.ApplicationContext;
import cn.yfd.springframework.context.ApplicationContextAware;

public class UserService2 implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, BeanClassLoaderAware {

    private ApplicationContext applicationContext;
    private BeanFactory beanFactory;

    private String uid;
    private String company;
    private String location;
    private UserDao1 userDao1;


    public String queryUserInfo() {
        return userDao1.queryUserName(uid) + "," + company + "," + location;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UserDao1 getUserDao() {
        return userDao1;
    }

    public void setUserDao(UserDao1 userDao) {
        this.userDao1 = userDao;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("classLoader:" + classLoader);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("Bean Name id"+name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
