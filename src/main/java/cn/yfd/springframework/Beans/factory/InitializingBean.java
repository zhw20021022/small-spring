package cn.yfd.springframework.Beans.factory;

public interface InitializingBean {

    /**
     * 处理了属性填充后调用
     */
    void afterPropertySet() throws  Exception;

}
