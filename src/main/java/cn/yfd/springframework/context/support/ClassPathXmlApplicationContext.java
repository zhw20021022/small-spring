package cn.yfd.springframework.context.support;

public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext{

    private String[] configLocations;

    /**
     * 从XML中加载BeanDefinition,并刷新上下文
     */
    public ClassPathXmlApplicationContext(){

    }

    /**
     * 从XML中加载BeanDefinition,并刷新上下文
     * @param configLocation
     */
    public ClassPathXmlApplicationContext(String configLocation){
        this(new String[]{configLocation});
    }

    /**
     * 从XML中加载BeanDefinition,并刷新上下文
     * @param configLocations
     */
    public ClassPathXmlApplicationContext(String[] configLocations) {
        this.configLocations = configLocations;
        refresh();
    }


    @Override
    protected String[] getConfigLocations() {
        return configLocations;
    }
}
