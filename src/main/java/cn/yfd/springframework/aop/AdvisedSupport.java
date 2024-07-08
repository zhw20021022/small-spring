package cn.yfd.springframework.aop;


import org.aopalliance.intercept.MethodInterceptor;

public class AdvisedSupport {

    private boolean proxyTargetClass = false;

    //被代理的对象
    private TargetSource targetSource;

    //方法拦截器,由用户自己实现 invoke()方法
    private MethodInterceptor methodInterceptor;

    //方法匹配器，检查目标方法是否符合目标通知条件
    private MethodMatcher methodMatcher;

    public boolean isProxyTargetClass() {
        return  proxyTargetClass;
    }

    public void setProxyTargetClass(boolean proxyTargetClass){
        this.proxyTargetClass = proxyTargetClass;
    }

    public TargetSource getTargetSource() {
        return targetSource;
    }

    public void setTargetSource(TargetSource targetSource) {
        this.targetSource = targetSource;
    }

    public MethodInterceptor getMethodInterceptor() {
        return methodInterceptor;
    }

    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    public MethodMatcher getMethodMatcher() {
        return methodMatcher;
    }

    public void setMethodMatcher(MethodMatcher methodMatcher) {
        this.methodMatcher = methodMatcher;
    }
}
