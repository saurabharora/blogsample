package org.apache.spring.dynamic;

import java.util.logging.Logger;

import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class LoggingSpringBeanFactory implements BeanPostProcessor {

  private final XmlBeanFactory fact;
  private final boolean logging;
  private static Logger logger = Logger.getLogger("org.apache.spring.dynamic");

  public LoggingSpringBeanFactory(boolean log, String springFileName) {
    fact = new XmlBeanFactory(new ClassPathResource(springFileName,
        LoggingSpringBeanFactory.class));
    logging = log;
    processBeans();
  }

  private void processBeans() {
    if (logging) {
      logger.info("setting up logging");
      BeanNameAutoProxyCreator creator = fact.getBean("aspectlogging",
          BeanNameAutoProxyCreator.class);
      fact.addBeanPostProcessor(creator);
    }
  }

  public <T> T getBean(String name, Class<T> requiredType)
      throws BeansException {
    return fact.getBean(name, requiredType);
  }

  public <T> T getBean(Class<T> requiredType) throws BeansException {
    return fact.getBean(requiredType);
  }

  public Object postProcessAfterInitialization(Object bean, String beanName)
      throws BeansException {

    return bean;
  }

  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    return bean;
  }

}
