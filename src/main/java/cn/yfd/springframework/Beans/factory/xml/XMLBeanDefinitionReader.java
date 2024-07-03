package cn.yfd.springframework.Beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.yfd.springframework.Beans.BeansException;
import cn.yfd.springframework.Beans.PropertyValue;
import cn.yfd.springframework.Beans.factory.config.BeanDefinition;
import cn.yfd.springframework.Beans.factory.config.BeanReference;
import cn.yfd.springframework.Beans.factory.support.AbstractBeanDefinitionReader;
import cn.yfd.springframework.Beans.factory.support.BeanDefinitionRegistry;
import cn.yfd.springframework.core.io.Resource;
import cn.yfd.springframework.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

public class XMLBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public XMLBeanDefinitionReader(BeanDefinitionRegistry registry){
        super(registry);
    }

    public XMLBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader){
        super(registry, resourceLoader);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException{
        try(InputStream inputStream = resource.getInputStream()){
            doLoadBeanDefinition(inputStream);
        }catch (IOException | ClassNotFoundException e){
            throw new BeansException("IOException parsing XML document from "+resource, e);
        }
    }

    @Override
    public void loadBeanDefinitions(Resource... resources) {
        for (Resource resource : resources) {
            loadBeanDefinitions(resource);
        }
    }

    @Override
    public void loadBeanDefinitions(String location) {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }

    public void loadBeanDefinitions(String...strings){
        for (String string : strings) {
            loadBeanDefinitions(string);
        }
    }

    protected void doLoadBeanDefinition(InputStream inputStream) throws ClassNotFoundException {
        Document doc = XmlUtil.readXML(inputStream);
        Element root = doc.getDocumentElement();
        NodeList childNodes = root.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); i++) {
            if(!(childNodes.item(i) instanceof  Element)){
                continue;
            }

            if(!"bean".equals(childNodes.item(i).getNodeName())){
                continue;
            }

            //解析标签
            Element bean = (Element) childNodes.item(i);
            String id = bean.getAttribute("id");
            String name = bean.getAttribute("name");
            String className = bean.getAttribute("class");
            String initMethodName = bean.getAttribute("init-method");
            String destroyMethodName = bean.getAttribute("destroy-method");
            String beanScope = bean.getAttribute("scope");

            //获取class,方便获取类中的名称
            Class<?> clazz = Class.forName(className);
            // beanName优先级 id > name
            String beanName = StrUtil.isNotEmpty(id) ? id : name;
            if(StrUtil.isEmpty(beanName)){
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }

            //定义Bean
            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            beanDefinition.setInitMethodName(initMethodName);
            beanDefinition.setDestroyMethodName(destroyMethodName);
            if(StrUtil.isNotEmpty(beanScope)){
                beanDefinition.setScope(beanScope);
            }

            //读取属性并填充
            for(int j = 0; j < bean.getChildNodes().getLength(); j++){
                if(!(bean.getChildNodes().item(j) instanceof Element)){
                    continue;
                }
                if(!"property".equals(bean.getChildNodes().item(j).getNodeName())){
                    continue;
                }
                Element property = (Element) bean.getChildNodes().item(j);
                String attrName = property.getAttribute("name");
                String attrValue = property.getAttribute("value");
                String attrRef = property.getAttribute("ref");
                //获取属性的 值 或 引用 对象:
                Object value = StrUtil.isNotEmpty(attrRef) ? new BeanReference(attrRef):attrValue;
                //创建属性信息
                PropertyValue propertyValue = new PropertyValue(attrName, value);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }
            if(getRegistry().containsBeanDefinition(beanName)){
                throw new BeansException("Duplicate beanName["+beanName+"] is not allowed");
            }
            //注册beanDefinition
            getRegistry().registerBeanDefinition(beanName, beanDefinition);
        }
    }
}




















