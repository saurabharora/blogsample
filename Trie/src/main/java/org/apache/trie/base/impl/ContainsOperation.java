package org.apache.trie.base.impl;

import org.apache.trie.base.OperationCodes;
import org.apache.trie.base.TraversalOperation;
import org.apache.trie.base.TrieException;
import org.apache.trie.base.TrieIterator;
import org.apache.trie.base.TrieKey;
import org.apache.trie.base.TrieNode;

/**
 * This Traversal Operation allows for finding a node in the trie. It is used in
 * the contains operation.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 * @param <E>
 *          TrieKey used for this Trie.
 */
class ContainsOperation<E extends TrieKey<E>> implements TraversalOperation<E> {
  /**
   * The response Node which is found in the Trie.
   */
  protected TrieNode<E> responseNode;
  /**
   * The key to find in the Trie.
   */
  protected E ele;

  ContainsOperation(E arg) {
    super();
    ele = arg;
  }

  TrieNode<E> getNode() {
    return responseNode;
  }

  @Override
  public OperationCodes leafTrieNode(E key) {
    boolean comp = key.equalsTrie(ele);
    if (comp) {
      return OperationCodes.TRAVERSE_PROCESS;
    }
    return OperationCodes.TRAVERSE_SIBLING;
  }

  @Override
  public OperationCodes startInternalTrieNode(E key) {
    int comp = key.compareFirstIndex(ele);
    if (comp == 0) {
      ele = ele.getKeyFrom(1);
      return OperationCodes.TRAVERSE_CHILD;
    } else if (comp == 1) {
      return OperationCodes.TRAVERSE_SIBLING;
    } else {
      // else comp == -1
      return OperationCodes.TRAVERSE_END;
    }
  }

  @Override
  public OperationCodes noNextNode() {
    return OperationCodes.TRAVERSE_END;
  }

  @Override
  public OperationCodes processNode(TrieIterator<E> iter) {
    responseNode = iter.previous();
    return OperationCodes.TRAVERSE_END;
  }

  /**
   * Lock Node callback, not used in this case.
   */
  @Override
  public void lockIteration() throws TrieException {
    // intentionally ignored
  }

  /**
   * UnLock Node callback, not used in this case.
   */
  @Override
  public void unlockIteration() throws TrieException {
    // intentionally ignored
  }

}