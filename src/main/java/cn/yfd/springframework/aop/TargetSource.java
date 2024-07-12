package cn.yfd.springframework.aop;

import cn.hutool.core.util.ClassUtil;
import cn.yfd.springframework.util.ClassUtils;

public class TargetSource {

    private final Object target;

    public TargetSource(Object target) {
        this.target = target;
    }

    public Class<?>[] getTargetClass(){
        Class<?> clazz = this.target.getClass();
        clazz = ClassUtils.isCGlibProxyClass(clazz) ? clazz.getSuperclass() : clazz;
        return clazz.getInterfaces();
    }

    public Object getTarget(){
        return this.target;
    }
}
