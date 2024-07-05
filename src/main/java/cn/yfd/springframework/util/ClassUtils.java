package cn.yfd.springframework.util;

public class ClassUtils {

    public static ClassLoader getDefaultClassLoader(){
        ClassLoader cl = null;

        try{
            cl = Thread.currentThread().getContextClassLoader();
        }catch (Throwable ex){

        }

        if(cl == null){
            cl = ClassUtils.class.getClassLoader();
        }

        return cl;
    }

    public static boolean isCGlibProxyClass(Class<?> clazz){
        return (clazz != null && isCGlibProxyClassName(clazz.getName()));
    }

    public static boolean isCGlibProxyClassName(String className){
        return (className != null && className.contains("$$"));
    }
}
