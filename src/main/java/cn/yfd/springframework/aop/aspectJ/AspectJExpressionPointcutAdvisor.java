package cn.yfd.springframework.aop.aspectJ;

import cn.yfd.springframework.aop.Pointcut;
import cn.yfd.springframework.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;

public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

    //切面
    private AspectJExpressionPointcut pointcut;

    //具体的拦截方法
    private Advice advice;

    //表达式
    private String expression;

    @Override
    public Advice getAdvice() {
        return advice;
    }

    @Override
    public Pointcut getPointcut() {
        if(pointcut == null){
            return new AspectJExpressionPointcut(expression);
        }
        return pointcut;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
