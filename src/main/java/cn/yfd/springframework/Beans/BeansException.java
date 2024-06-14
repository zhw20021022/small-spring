package cn.yfd.springframework.Beans;

/**
 * 定义Bean的异常
 */

public class BeansException extends RuntimeException{
    public BeansException(String msg){
        super(msg);
    }

    public BeansException(String msg, Throwable cause){
        super(msg, cause);
    }
}
