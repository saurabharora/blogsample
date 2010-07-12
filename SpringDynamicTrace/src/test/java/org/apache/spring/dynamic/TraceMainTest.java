package org.apache.spring.dynamic;

import org.junit.Before;
import org.junit.Test;

public class TraceMainTest {

  private TraceMain main;

  @Before
  public void setUp() throws Exception {
    main = new TraceMain();
  }

  @Test
  public void noTraceTest() {
    String[] args = { "5", "6" };
    main.run(args);
  }

  @Test
  public void traceTest() {
    String[] args = { "-trace", "5", "6" };
    main.run(args);
  }

}
