package org.apache.spring.dynamic;

public class WorkerBean {

  public int doWork(final int value) throws InterruptedException {
    return value * value;
  }

}
