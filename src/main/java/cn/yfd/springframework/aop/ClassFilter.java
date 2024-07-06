package cn.yfd.springframework.aop;

public interface ClassFilter {

    boolean matches(Class<?> clazz);

}
