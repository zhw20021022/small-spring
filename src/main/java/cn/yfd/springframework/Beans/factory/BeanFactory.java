package cn.yfd.springframework.Beans.factory;

public interface BeanFactory {
    Object getBean(String name);

    Object getBean(String name, Object...args);
}
