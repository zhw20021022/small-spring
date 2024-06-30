package cn.yfd.springframework.context;

import cn.yfd.springframework.Beans.factory.Aware;

public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext);

}
