package cn.yfd.springframework.test;

import cn.hutool.core.io.IoUtil;
import cn.yfd.springframework.Beans.PropertyValue;
import cn.yfd.springframework.Beans.PropertyValues;
import cn.yfd.springframework.Beans.factory.config.BeanDefinition;
import cn.yfd.springframework.Beans.factory.config.BeanReference;
import cn.yfd.springframework.Beans.factory.support.DefaultListableBeanFactory;
import cn.yfd.springframework.Beans.factory.xml.XMLBeanDefinitionReader;
import cn.yfd.springframework.aop.AdvisedSupport;
import cn.yfd.springframework.aop.TargetSource;
import cn.yfd.springframework.aop.aspectJ.AspectJExpressionPointcut;
import cn.yfd.springframework.aop.framework.Cglib2AopProxy;
import cn.yfd.springframework.aop.framework.JdkDynamicAopProxy;
import cn.yfd.springframework.context.support.ClassPathXmlApplicationContext;
import cn.yfd.springframework.core.io.DefaultResourceLoader;
import cn.yfd.springframework.core.io.Resource;
import cn.yfd.springframework.test.bean.*;
import cn.yfd.springframework.test.common.MyBeanPostFactoryPostProcessor;
import cn.yfd.springframework.test.common.MyBeanPostProcessor;
import cn.yfd.springframework.test.event.CustomEvent;
import net.sf.cglib.proxy.Enhancer;
import org.junit.Before;
import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ApiTest {

    @Test
    public void test_BeanFactory(){
        //1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        //2.注册 bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        beanFactory.registerBeanDefinition("userService", beanDefinition);

        //3.第一次获取bean
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();
        System.out.println(userService.hashCode());

        //4.第一次获取bean
        UserService userService_singleton = (UserService) beanFactory.getSingleton("userService");
        userService.queryUserInfo();
        System.out.println(userService_singleton.hashCode());
    }

    @Test
    public void test_BeanFactoryForCglib(){
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        beanFactory.registerBeanDefinition("userService", beanDefinition);

        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();
    }

    @Test
    public void test_newInstance() throws InstantiationException, IllegalAccessException {
        UserService userService = UserService.class.newInstance();
        System.out.println(userService);
    }

    @Test
    public void test_constructor() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<UserService> userServiceClass = UserService.class;
        Constructor<UserService> declaredConstructor = userServiceClass.getDeclaredConstructor(String.class);
        UserService userService = declaredConstructor.newInstance("羊粪蛋");
        userService.queryUserInfo();
    }

    @Test
    public void test_parameter() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<UserService> userServiceClass = UserService.class;
        Constructor<?>[] declaredConstructors = userServiceClass.getDeclaredConstructors();
        Constructor<?> declaredConstructor = declaredConstructors[0];
        Constructor<UserService> constructor = userServiceClass.getConstructor(declaredConstructor.getParameterTypes());
        UserService userService = constructor.newInstance("羊粪蛋");
        System.out.println(userService);
    }

    @Test
    public void test_Cglib(){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserService.class);
        enhancer.setCallback(new Interecptor());

        UserService userService = (UserService) enhancer.create(new Class[]{String.class}, new Object[]{"羊粪蛋"});
        //System.out.println(userService);
        System.out.println("=============================");
        userService.queryUserInfo();
    }

    @Test
    public void test_Property(){
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class));

        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("uid", "100001"));
        propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));

        BeanDefinition beanDefinition = new BeanDefinition(UserService.class, propertyValues);
        beanFactory.registerBeanDefinition("userService", beanDefinition);

        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();

        UserDao userDao = userService.getUserDao();
        String s = userDao.queryUserName("100002");
        System.out.println(s);
    }

    private DefaultResourceLoader resourceLoader;

    @Before
    public void init(){
        resourceLoader = new DefaultResourceLoader();
    }

    @Test
    public void test_classpath() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:important.properties");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
    }

    @Test
    public void test_file() throws IOException {
        Resource resource = resourceLoader.getResource("src/test/resources/important.properties");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
    }

    @Test
    public void test_url() throws IOException {
        Resource resource = resourceLoader.getResource("https://baidu.com");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
    }

    @Test
    public void test_xml(){
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        XMLBeanDefinitionReader reader = new XMLBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");

        UserService userService = beanFactory.getBean("userService", UserService.class);
        String result = userService.queryUserInfo();
        System.out.println("测试结果"+result);
    }

    @Test
    public void test_BeanFactoryPostProcessorAndBeanPostProcessor(){
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        XMLBeanDefinitionReader beanDefinitionReader = new XMLBeanDefinitionReader(beanFactory);
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");

        MyBeanPostFactoryPostProcessor myBeanPostFactoryPostProcessor = new MyBeanPostFactoryPostProcessor();
        myBeanPostFactoryPostProcessor.postProcessorBeanFactory(beanFactory);

        MyBeanPostProcessor myBeanPostProcessor = new MyBeanPostProcessor();
        beanFactory.addBeanPostProcessor(myBeanPostProcessor);

        UserService userService = beanFactory.getBean("userService", UserService.class);
        String result = userService.queryUserInfo();
        System.out.println(result);
    }

    @Test
    public void test_xmlPostProcessor(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:springPostProcessor.xml");

        UserService userService = applicationContext.getBean("userService", UserService.class);
        String s = userService.queryUserInfo();
        System.out.println(s);
    }

    @Test
    public void test_initAndDestroy(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutDownHook();

        UserService1 userService = applicationContext.getBean("userService1", UserService1.class);
        String result = userService.queryUserInfo();
        System.out.println(result);
    }

    @Test
    public void test_Aware(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring2.xml");
        applicationContext.registerShutDownHook();

        UserService2 userService2 = applicationContext.getBean("userService2", UserService2.class);
        String result = userService2.queryUserInfo();
        System.out.println("测试结果: "+result);
        System.out.println("applicationContextAware: "+userService2.getApplicationContext());
        System.out.println("beanFactoryAware: "+userService2.getBeanFactory());

    }

    @Test
    public void test_factory_bean(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring3.xml");
        applicationContext.registerShutDownHook();

         UserService3 userService1 = applicationContext.getBean("userService3", UserService3.class);
         UserService3 userService2 = applicationContext.getBean("userService3", UserService3.class);

        System.out.println(userService1);
        System.out.println(userService2);

        System.out.println(userService1 + "十六进制哈希:" + Integer.toHexString(userService1.hashCode()));
        System.out.println(ClassLayout.parseInstance(userService1).toPrintable());
    }

    @Test
    public void test_Proxy(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring3.xml");
        applicationContext.registerShutDownHook();

        UserService3 userService = applicationContext.getBean("userService3", UserService3.class);
        System.out.println("测试结果:"+userService.queryUserInfo());
    }

    @Test
    public void test_Event(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring4.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext, 100001L, "成功了"));

        applicationContext.registerShutDownHook();
    }

    @Test
    public void test_aop() throws NoSuchMethodException{
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* cn.yfd.springframework.test.bean.UserService.*(..))");
        Class<UserService> clazz = UserService.class;
        Method method = clazz.getDeclaredMethod("queryUserInfo");

        System.out.println(pointcut.matches(clazz));
        System.out.println(pointcut.matches(method, clazz));
    }

    @Test
    public void test_dynamic(){
        IUserService userService = new UserService4();

        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTargetSource(new TargetSource(userService));
        advisedSupport.setMethodInterceptor(new UserServiceInterceptor());
        advisedSupport.setMethodMatcher(new AspectJExpressionPointcut("execution(* cn.yfd.springframework.test.bean.IUserService.*(..))"));

        IUserService proxy_jdk = (IUserService) new JdkDynamicAopProxy(advisedSupport).getProxy();
        System.out.println("测试结果: "+proxy_jdk.queryUserInfo());

        IUserService proxy_Cglib = (IUserService) new Cglib2AopProxy(advisedSupport).getProxy();
        System.out.println("测试结果: "+proxy_Cglib.register("张张"));


    }

    @Test
    public void test_aopAndBean(){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring5.xml");
        IUserService userService = classPathXmlApplicationContext.getBean("userService4", IUserService.class);
        System.out.println("测试结果: "+userService.queryUserInfo());
    }

    @Test
    public void test_scan(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-scan.xml");
        IUserService userService5 = applicationContext.getBean("userService5", IUserService.class);
        System.out.println("测试结果:"+userService5.queryUserInfo());
    }

    @Test
    public void test_property(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-property.xml");
        IUserService userService5 = applicationContext.getBean("userService5", IUserService.class);
        System.out.println("测试结果：" + userService5);
    }
}
















