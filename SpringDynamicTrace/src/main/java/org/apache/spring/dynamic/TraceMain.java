package org.apache.spring.dynamic;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class TraceMain {

  private static Logger logger = Logger.getLogger("org.apache.spring.dynamic");

  /**
   * @param args
   */
  public static void main(String[] args) {

    TraceMain main = new TraceMain();
    main.run(args);

  }

  public void run(final String[] args) {
    boolean trace = false;

    try {

      PosixParser parser = new PosixParser();
      CommandLine line = parser.parse(getOptions(), args);
      trace = line.hasOption("trace");

      LoggingSpringBeanFactory springBean = new LoggingSpringBeanFactory(trace,
          "springbean.xml");

      List argsList = line.getArgList();

      WorkerBean work = springBean.getBean(WorkerBean.class);

      for (Iterator numbers = argsList.iterator(); numbers.hasNext();) {
        int i = Integer.valueOf((String) numbers.next());
        System.out.printf("The square of %d is %d \n", i, work.doWork(i));
      }

      logger.info("Completed");

    } catch (ParseException pe) {
      pe.printStackTrace();
    } catch (InterruptedException e) {

      e.printStackTrace();
    }
  }

  private Options getOptions() {
    Options opt = new Options();
    opt.addOption("trace", false, "Trace the application.");
    return opt;
  }
}
