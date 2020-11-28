package com.lee.knife4j.util;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class SpringUtil {

    private static ApplicationContext applicationContext;
    private static Environment environment;

    @Autowired
    public synchronized void setApplicationContext(ApplicationContext applicationContext) {
        SpringUtil.applicationContext = applicationContext;
    }

    @Autowired
    public synchronized void setEnvironment(Environment environment) {
        SpringUtil.environment = environment;
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(Class<T> clazz, String name) {
        return applicationContext.getBean(name, clazz);
    }

    public static <T> T getBeanByName(Class<T> clazz, String name) {
        Object bean = null;
        try {
            bean = applicationContext.getBean(name);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
        if (bean == null || bean.toString().equals("null")) {
            return null;
        }
        return (T) bean;
    }

    public static <T> T getValue(Class<T> clazz, String key) {
        return environment.getProperty(key, clazz);
    }
}
