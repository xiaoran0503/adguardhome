package org.fordes.adg.rule.util;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 静态 Bean 获取工具。
 * <p>
 * {@link org.fordes.adg.rule.handler.RuleHandler} 是抽象类、由线程池回调，无法在构造期注入具体实现，
 * 因此保留该组件持有容器上下文。
 */
@Component
public class SpringUtil implements BeanFactoryPostProcessor, ApplicationContextAware {

    private static ConfigurableListableBeanFactory beanFactory;

    private static ApplicationContext applicationContext;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        SpringUtil.beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringUtil.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return beanFactory != null ? beanFactory.getBean(clazz) : applicationContext.getBean(clazz);
    }
}
