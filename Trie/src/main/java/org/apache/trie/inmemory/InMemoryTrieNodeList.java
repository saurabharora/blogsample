package org.apache.trie.inmemory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

import org.apache.trie.base.TrieException;
import org.apache.trie.base.TrieIterator;
import org.apache.trie.base.TrieKey;
import org.apache.trie.base.TrieNode;
import org.apache.trie.base.TrieNodeList;

/**
 * The class represents the TrieNodeList for the InMemoryTrie. It stores the
 * children nodes and provide api for its manipulation.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 * @param <E>
 *          TrieKey used for this Trie.
 */
class InMemoryTrieNodeList<E extends TrieKey<E>> implements TrieNodeList<E> {

  private static final Logger logger = Logger // NOPMD
      .getLogger("org.apache.trie.inmemory");

  @SuppressWarnings("unchecked")
  private TrieNode nodes[] = new TrieNode[0];
  private final InMemoryTrieNode<E> parent;
  private int size = 0;
  private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(
      true);
  private final Lock writeOnlyLock;
  private static final String READLOCK = "readOnlyLock";

  InMemoryTrieNodeList(InMemoryTrieNode<E> aparentNode) {
    writeOnlyLock = readWriteLock.writeLock();
    parent = aparentNode;
  }

  @Override
  public TrieIterator<E> trieReadOnlyIterator() throws TrieException {
    return new ReadOnlyIterator(readWriteLock.readLock());
  }

  @Override
  public TrieIterator<E> trieWritableIterator() throws TrieException {
    return new InMemoryWriteableIterator(readWriteLock.readLock());
  }

  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public String toString() {
    return "InMemoryTrieNodeList [node=" + parent + "]";
  }

  private void locker(Lock lock, String mesg) throws TrieException {
    logger.fine("locking " + mesg + " on node" + parent);
    try {
      while (!lock.tryLock(5, TimeUnit.SECONDS)) {
        logger.fine("Trying to acquire readWriteLock:" + mesg + "in " + parent);
        logger.fine("readCount=" + readWriteLock.getReadLockCount()
            + "writeLocked=" + readWriteLock.isWriteLocked());
      }
    } catch (InterruptedException e) {
      throw new TrieException(
          "InterruptedException in readWriteLock operation", e);
    }
  }

  private void unlocker(Lock lock, String mesg) throws TrieException {
    logger.fine("unlocking " + mesg + " on node" + parent);
    lock.unlock();
  }

  /**
   * TrieIterator which supports writable semantics.
   */
  class ReadOnlyIterator extends AbstractIterator<E> {
    /**
     * The index for the iteration operations.
     */
    protected int count = 0;

    /**
     * ReadLock that we acquire.
     */
    protected final Lock readOnlyLock;
    private boolean locked = true;

    ReadOnlyIterator(Lock arg) throws TrieException {
      super();
      readOnlyLock = arg;
      locker(readOnlyLock, READLOCK);
    }

    @Override
    public boolean hasNext() {
      checkLock();
      return size > count;
    }

    @SuppressWarnings("unchecked")
    @Override
    public TrieNode<E> next() {
      checkLock();
      return nodes[count++];
    }

    @SuppressWarnings("unchecked")
    @Override
    public TrieNode<E> previous() {
      checkLock();
      if (count > 0) {
        return nodes[count - 1];
      }
      return null;
    }

    @Override
    public void finishIteration() throws TrieException {
      logger.fine("finishIteration");
      checkLock();
      unlocker(readOnlyLock, READLOCK);
      locked = false;
    }

    private void checkLock() {
      if (!locked) {
        throw new IllegalStateException("read Lock missing");
      }
    }

    @Override
    public void reset() throws TrieException {
      count = 0;
    }

  }

  /**
   * TrieIterator which supports read and write semantics.
   */
  class InMemoryWriteableIterator extends ReadOnlyIterator {
    private boolean writable = false;

    InMemoryWriteableIterator(Lock readOnly) throws TrieException {
      super(readOnly);
    }

    private void checkLock() throws TrieException {
      if (!writable) {
        throw new IllegalStateException("Write Lock missing");
      }
    }

    @Override
    public void insert(final TrieNode<E> node) throws TrieException {
      checkLock();
      insertNode(node, count - 1);
    }

    @Override
    public void removeNode() throws TrieException {
      checkLock();
      if (size == 0 || count == 0) {
        // nothing to remove
        throw new IllegalArgumentException(
            "Cannot remove from empty list or before first element");
      }

      int index = count - 1;

      for (int j = index; j < (size - 1); j++) {
        nodes[j] = nodes[j + 1];
      }
      nodes[size - 1] = null;
      size--;

    }

    @Override
    public void insertAtEnd(TrieNode<E> node) throws TrieException {
      checkLock();
      insertNode(node, size);
    }

    @SuppressWarnings("unchecked")
    private void insertNode(TrieNode<E> node, int index) throws TrieException {
      TrieNode[] tmp = null;
      if (nodes.length == 0) {
        // insert first node.
        tmp = new InMemoryTrieNode[1];
        tmp[0] = node;
      } else {

        if (size == nodes.length) {
          tmp = new TrieNode[size + 2];

          for (int j = index - 1; j >= 0; j--) {
            tmp[j] = nodes[j];
          }

        } else {
          tmp = nodes;
        }

        for (int j = size; j > index; j--) {
          tmp[j] = nodes[j - 1];
        }
        tmp[index] = node;

      }
      nodes = tmp;
      size++;
    }

    @SuppressWarnings("unchecked")
    @Override
    public TrieNode<E> replaceNode(TrieNode<E> newnode) throws TrieException {
      checkLock();
      TrieNode<E> tmp;
      if (count == 0) {
        if (!nodes[0].children().isEmpty()) {
          throw new IllegalStateException("Insertion Node state changed");
        }
        tmp = nodes[0];
        nodes[0] = newnode;
      } else {
        if (!nodes[count - 1].children().isEmpty()) {
          throw new IllegalStateException("Insertion Node state changed");
        }
        tmp = nodes[count - 1];
        nodes[count - 1] = newnode;
      }
      return tmp;
    }

    @Override
    public void lockExclusive() throws TrieException {
      logger.fine("lockExclusive");
      unlocker(readOnlyLock, READLOCK);
      locker(writeOnlyLock, "writeOnlyLock");
      writable = true;
    }

    @Override
    public void unlockExclusive() throws TrieException {
      if (writable) {
        logger.fine("unlockExclusive");
        locker(readOnlyLock, READLOCK);
        unlocker(writeOnlyLock, "writeOnlyLock");
        writable = false;
      }
    }

    @Override
    public void finishIteration() throws TrieException {
      if (writable) {
        unlockExclusive();
      }
      super.finishIteration();
    }
  }
}
