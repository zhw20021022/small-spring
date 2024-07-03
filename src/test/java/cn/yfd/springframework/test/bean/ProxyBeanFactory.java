package cn.yfd.springframework.test.bean;

import cn.yfd.springframework.Beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class ProxyBeanFactory implements FactoryBean<IUserDao> {
    @Override
    public IUserDao getObject() throws Exception {
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("100001", "羊粪蛋");
                hashMap.put("100002", "i丽丽");
                hashMap.put("100003", "小白");

                return "你被代理了"+method.getName()+":"+hashMap.get(args[0].toString());
            }
        };
        return (IUserDao) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{IUserDao.class},handler );
    }

    @Override
    public Class<?> getObjectType() {
        return IUserDao.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
