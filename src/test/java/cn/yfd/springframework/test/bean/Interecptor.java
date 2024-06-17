package cn.yfd.springframework.test.bean;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class Interecptor implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("原始对象方法执行前");
        Object o1 = methodProxy.invokeSuper(o, objects);
        System.out.println("原始对象方法执行后");
        return o1;
    }
}
