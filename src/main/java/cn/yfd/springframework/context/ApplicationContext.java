package cn.yfd.springframework.context;

import cn.yfd.springframework.Beans.factory.HierarchicalBeanFactory;
import cn.yfd.springframework.Beans.factory.ListableBeanFactory;
import cn.yfd.springframework.core.io.ResourceLoader;

/**
 * 应用上下文
 */
public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader, ApplicationEventPublisher {
}
