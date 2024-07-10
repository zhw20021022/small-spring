package cn.yfd.springframework.Beans.factory;

import cn.yfd.springframework.Beans.BeansException;
import cn.yfd.springframework.Beans.PropertyValue;
import cn.yfd.springframework.Beans.PropertyValues;
import cn.yfd.springframework.Beans.factory.config.BeanDefinition;
import cn.yfd.springframework.Beans.factory.config.BeanFactoryPostProcessor;
import cn.yfd.springframework.core.io.DefaultResourceLoader;
import cn.yfd.springframework.core.io.Resource;
import cn.yfd.springframework.util.StringValueResolver;

import java.io.IOException;
import java.util.Properties;

public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {

    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    private String location;

    @Override
    public void postProcessorBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        try{
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(location);
            Properties properties = new Properties();
            properties.load(resource.getInputStream());

            String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
            for (String beanName : beanDefinitionNames) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);

                PropertyValues propertyValues = beanDefinition.getPropertyValues();
                for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                    Object value = propertyValue.getValue();
                    if(!(value instanceof String)) continue;
                    value = resolvePlaceholder((String) value, properties);
                    propertyValues.addPropertyValue(new PropertyValue(propertyValue.getName(), value));
                }

                //像容器中添加字符串解析器，供解析@Value使用
                PlaceholderResolvingStringValueResolver valueResolver = new PlaceholderResolvingStringValueResolver(properties);
                beanFactory.addEmbeddedValueResolver(valueResolver);
            }
        }catch (IOException e){
            throw new BeansException("Could not load properties", e);
        }
    }

    private String resolvePlaceholder(String value, Properties properties){
        String strVal = value;
        StringBuilder buffer = new StringBuilder(strVal);
        int startIdx = strVal.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
        int stopIdx = strVal.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
        if(startIdx != -1 && stopIdx != -1 && startIdx < stopIdx){
            String propKey = strVal.substring(startIdx+2, stopIdx);
            String propVal = properties.getProperty(propKey);
            buffer.replace(startIdx, stopIdx+1, propVal);
        }
        return buffer.toString();
    }

    public void setLocation(String location) {
        this.location = location;
    }


    private class PlaceholderResolvingStringValueResolver implements StringValueResolver{

        private final Properties properties;

        public PlaceholderResolvingStringValueResolver(Properties properties) {
            this.properties = properties;
        }

        @Override
        public String resolveStringValue(String strVal) {
            return PropertyPlaceholderConfigurer.this.resolvePlaceholder(strVal, properties);
        }
    }
}
