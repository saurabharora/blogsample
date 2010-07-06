/**
 * 
 */
package org.apache.trie.inmemory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.trie.datatypes.StringKey;

/**
 * This class in used to run the Trie methods. It has a functional approach to
 * execution. Each instance is immutable once created for the outside the class.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 * 
 */
class RunnableOperation {

  private static final Logger logger = Logger.getLogger(Runnable.class //NOPMD
      .getName());

  private final OperationType type;
  private final RunnableOperation parent;
  private final InMemoryTrie<StringKey> trie;
  private final ThreadPool pool;
  private final String keyArr[];
  private final int size;
  private final int parallelops;
  /**
   * Variable to track no of threads created.
   */
  static long totalThreads = 0;

  RunnableOperation(InMemoryTrie<StringKey> atrie, RunnableOperation par,
      OperationType atype, int pops, ThreadPool apool) {
    parent = par;
    trie = atrie;
    type = atype;
    parallelops = pops;
    pool = apool;
    size = 0;
    keyArr = null;
  }

  RunnableOperation(InMemoryTrie<StringKey> atrie, RunnableOperation par,
      OperationType atype, int pops, ThreadPool apool, int asize, String[] akey) {
    parent = par;
    trie = atrie;
    type = atype;
    parallelops = pops;
    pool = apool;
    size = asize;
    keyArr = akey;
  }

  RunnableOperation(InMemoryTrie<StringKey> atrie) {
    parent = null;
    trie = atrie;
    type = OperationType.NULL_OP;
    parallelops = 0;
    pool = new ThreadPool();
    size = 0;
    keyArr = null;
  }

  public void run() throws Exception {
    try {
      if (parent != null && type != OperationType.PARALLEL) {
        parent.run();
      }
      switch (type) {
      case NULL_OP:
        break;
      case INSERT:
        logger.info("Inserting...");
        for (String value : keyArr) {
          StringKey key = new StringKey(value);
          trie.addElement(key);
          assertTrue("contains fails" + key.toString(), trie.contains(key));
        }
        break;
      case CONTAINS:
        logger.info("Contains...");
        for (String value : keyArr) {
          StringKey key = new StringKey(value);
          assertTrue(key.toString(), trie.contains(key));
        }
        break;
      case PRINT_NODE:
        logger.info("Print Node...");
        trie.printNodes(System.out);
        break;
      case FIND_PREFIX:
        logger.info("Find Prefix...");
        for (String value : keyArr) {
          StringKey key = new StringKey(value);
          List<StringKey> keys = trie.findPrefix(key);
          for (StringKey arg : keys) {
            logger.info(arg.toString());
          }
          assertSame("keys size:" + keys.size(), keys.size(), size);
        }
        break;
      case REMOVE:
        logger.info("Removing ...");
        for (String value : keyArr) {
          StringKey key = new StringKey(value);
          assertTrue("Remove " + key.toString(), trie.removeElement(key));
          assertFalse("Contains should fail " + key.toString(), trie
              .contains(key));
        }
        break;
      case PARALLEL:
        if (parent != null) {
          try {
            totalThreads++;
            pool.execute(new Runnable() {

              @Override
              public void run() {
                logger.info("parallel...");
                try {
                  parent.run();
                } catch (Exception e) {
                  try {
                    pool.addException(e);
                  } catch (InterruptedException e1) {
                    logger.log(Level.INFO,
                        "exception in parallel addException", e);
                  }
                }
                logger.info("End parallel...");
              }
            });
          } catch (InterruptedException e) {
            logger.log(Level.INFO, "exception in parallel", e);
          }
        }
        break;
      case WAIT:
        pool.waitForCompletion(parallelops);
        break;
      default:
        throw new UnsupportedOperationException();
      }
    } catch (AssertionError e) {
      trie.printNodes(System.out);
      throw e;
    }
  }

  private int calculateParallelCalls() {
    if (type == OperationType.WAIT) {
      return 0;
    } else {
      return parallelops;
    }
  }

  public RunnableOperation insert(String... keys) {
    RunnableOperation runner = new RunnableOperation(trie, this,
        OperationType.INSERT, calculateParallelCalls(), pool, 0, keys);
    return runner;
  }

  public RunnableOperation contains(String... keys) {
    RunnableOperation runner = new RunnableOperation(trie, this,
        OperationType.CONTAINS, calculateParallelCalls(), pool, 0, keys);
    return runner;
  }

  public RunnableOperation printNodes() {
    RunnableOperation runner = new RunnableOperation(trie, this,
        OperationType.PRINT_NODE, calculateParallelCalls(), pool);
    return runner;
  }

  public RunnableOperation findPrefix(int size, String keys) {
    String tmp[] = new String[1];
    tmp[0] = keys;
    RunnableOperation runner = new RunnableOperation(trie, this,
        OperationType.FIND_PREFIX, calculateParallelCalls(), pool, size, tmp);
    return runner;
  }

  public RunnableOperation remove(String... keys) {
    RunnableOperation runner = new RunnableOperation(trie, this,
        OperationType.REMOVE, calculateParallelCalls(), pool, 0, keys);
    return runner;
  }

  public RunnableOperation parallel() {
    RunnableOperation runner = new RunnableOperation(trie, this,
        OperationType.PARALLEL, calculateParallelCalls() + 1, pool);
    return runner;
  }

  public RunnableOperation waitCompletion() {
    if (parallelops <= 0) {
      throw new IllegalStateException("Found no parallel calls");
    }
    return new RunnableOperation(trie, this, OperationType.WAIT,
        calculateParallelCalls(), pool);
  }

  /**
   * Internal Enum to define Operation Type for RunnableOperation.
   */
  enum OperationType {
    INSERT, NULL_OP, CONTAINS, PRINT_NODE, FIND_PREFIX, REMOVE, PARALLEL, WAIT
  }

  /**
   * This ThreadPool is used to create threads and wait for their completion.
   * 
   */
  static class ThreadPool {

    private final BlockingQueue<Thread> threads = new ArrayBlockingQueue<Thread>(
        10);
    private final BlockingQueue<Exception> exceptions = new ArrayBlockingQueue<Exception>(
        1);

    void execute(Runnable arg) throws InterruptedException {
      logger.info("Adding Thread");
      Thread runnerThread = new Thread(arg);
      runnerThread.start();
      threads.put(runnerThread);
    }

    void addException(Exception e) throws InterruptedException {
      exceptions.put(e);
    }

    void waitForCompletion(int take) throws Exception {
      logger.fine("Take=" + take);
      for (int i = 0; i < take; i++) {
        try {
          Thread th;

          logger.fine("counter=" + i);
          th = threads.poll(5, TimeUnit.SECONDS);
          if (th == null) {
            i--;
          } else {
            logger.fine("joining=" + i);
            try {
              th.join(1000);
            } catch (InterruptedException e) {
              logger.log(Level.INFO, "Cannot join now Lets us put it back", e);
              threads.put(th);
            }
          }

        } catch (InterruptedException e) {
          logger.log(Level.INFO, "Exception in thread cleanup", e);

        }
        Exception e = exceptions.poll();
        if (e != null) {
          throw e;
        }
      }
    }
  }
}
