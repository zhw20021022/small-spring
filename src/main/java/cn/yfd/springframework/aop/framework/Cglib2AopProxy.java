package cn.yfd.springframework.aop.framework;

import cn.yfd.springframework.aop.AdvisedSupport;
import cn.yfd.springframework.util.ClassUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class Cglib2AopProxy implements AopProxy{

    private final AdvisedSupport advised;

    public Cglib2AopProxy(AdvisedSupport advised) {
        this.advised= advised;
    }

    @Override
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        Class<?> aClass = advised.getTargetSource().getTarget().getClass();
        aClass = ClassUtils.isCGlibProxyClass(aClass)?aClass.getSuperclass():aClass;
        enhancer.setSuperclass(aClass);
        enhancer.setInterfaces(advised.getTargetSource().getTargetClass());
        enhancer.setCallback(new DynamicAdvisedInterceptor(advised));
        return enhancer.create();
    }

    public static class DynamicAdvisedInterceptor implements MethodInterceptor {

        private final AdvisedSupport advised;

        public DynamicAdvisedInterceptor(AdvisedSupport advised) {
            this.advised = advised;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            CglibMethodInvocation methodInvocation = new CglibMethodInvocation(advised.getTargetSource().getTarget(), method, objects, methodProxy);
            if(advised.getMethodMatcher().matches(method, advised.getTargetSource().getClass())){
                return advised.getMethodInterceptor().invoke(methodInvocation);
            }
            return methodInvocation.proceed();
        }
    }

    public static class CglibMethodInvocation extends ReflectiveMethodInvocation{

        private final MethodProxy methodProxy;

        public CglibMethodInvocation(Object target, Method method, Object[] arguments, MethodProxy methodProxy) {
            super(target, method, arguments);
            this.methodProxy = methodProxy;
        }

        @Override
        public Object proceed() throws Throwable {
            return this.methodProxy.invoke(this.target, this.arguments);
        }
    }
}
