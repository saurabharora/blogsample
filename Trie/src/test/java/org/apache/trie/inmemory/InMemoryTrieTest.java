package org.apache.trie.inmemory;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.trie.datatypes.StringKey;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for StringKey also support parallel Operations.
 * 
 */
public class InMemoryTrieTest {

  private static final Logger logger = Logger.getLogger(InMemoryTrieTest.class //NOPMD
      .getName());
  private RunnableOperation runner;
  private final String[] lists = { "abc", "abc1", "abc3", "acb" };

  // private String[] lists = { "abc", "abc1", "acb"};

  @Before
  public void setupData() {
    runner = new RunnableOperation(new InMemoryTrie<StringKey>());
    logger.setLevel(Level.ALL);
    ConsoleHandler handler = new ConsoleHandler();
    handler.setLevel(Level.ALL);
    logger.addHandler(handler);
  }

  @Test
  public void sequentialOperationTests() throws Exception {

    runner.insert(lists).run();

    runner.contains("abc1").printNodes().run();

    runner.findPrefix(3, "ab").run();

    logger.info("-----------------");

    runner.remove(lists).printNodes().run();

    logger.info("-----------------");

    runner.findPrefix(0, "a").run();

  }

 
//  public void loopTests() throws Exception {
//    for (int i = 0; i < 20; i++) {
//      parallelOperationTests();
//    }
//    logger.info("TOTAL THREADS=" + RunnableOperation.totalThreads);
//  }
  
  @Test
  public void parallelOperationTests() throws Exception {

    String list2[] = { "ddd", "ddb", "dty" };
    runner.insert("this", "is", "great");
    runner.insert(lists).run();

    runner.findPrefix(3, "ab").run();

    // Now lets do some parallel
    RunnableOperation oper = runner;
    for (String key : lists) {
      oper = oper.remove(key).parallel();
    }

    oper.insert(list2).waitCompletion().run();

    runner.findPrefix(3, "d").run();
    logger.info("-------------------------------");

    runner.insert(lists).parallel().remove(list2).parallel().waitCompletion()
        .run();

    runner.findPrefix(3, "ab").run();

    runner.printNodes().run();

  }

}
