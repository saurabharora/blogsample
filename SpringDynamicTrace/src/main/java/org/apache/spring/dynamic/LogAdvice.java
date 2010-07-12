package org.apache.spring.dynamic;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

public class LogAdvice implements MethodBeforeAdvice, ThrowsAdvice,
    AfterReturningAdvice {
  private static Logger logger = Logger.getLogger("org.apache.spring.dynamic");

  @Override
  public void before(Method method, Object[] args, Object target)
      throws Throwable {
    logger.entering(target.getClass().getCanonicalName(), method.getName());
  }

  public void afterThrowing(Method method, Object[] args, Object target,
      Exception ex) {
    logger.throwing(target.getClass().getCanonicalName(), method.getName(), ex);
  }

  @Override
  public void afterReturning(Object returnValue, Method method, Object[] args,
      Object target) throws Throwable {
    logger.exiting(target.getClass().getCanonicalName(), method.getName());
  }
}
