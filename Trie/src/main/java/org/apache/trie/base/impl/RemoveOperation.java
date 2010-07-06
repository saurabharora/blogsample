package org.apache.trie.base.impl;

import org.apache.trie.base.OperationCodes;
import org.apache.trie.base.TraversalOperationWithBacktrack;
import org.apache.trie.base.Trie;
import org.apache.trie.base.TrieException;
import org.apache.trie.base.TrieIterator;
import org.apache.trie.base.TrieKey;

/**
 * This Traversal Operation is used to remove a node from the Trie. It is used
 * in the {@link Trie#removeElement(TrieKey) }operation.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 * @param <E>
 *          TrieKey used for this Trie.
 */
class RemoveOperation<E extends TrieKey<E>> implements
    TraversalOperationWithBacktrack<E> {

  private boolean removed = false;
  private boolean backTrackRemoved = false;
  private E ele;

  RemoveOperation(E arg) {
    super();
    ele = arg;
  }

  public boolean isRemoved() {
    return removed;
  }

  @Override
  public OperationCodes leafTrieNode(E key) {
    if (removed && !key.isEoln() && (key.getKeyLength() == 1)) {
      return OperationCodes.TRAVERSE_PROCESS;
    } else if (key.equalsTrie(ele)) {
      return OperationCodes.TRAVERSE_PROCESS;
    }
    return OperationCodes.TRAVERSE_SIBLING;
  }

  @Override
  public OperationCodes startInternalTrieNode(E key) {
    int comp = key.compareFirstIndex(ele);
    if (comp == 0) {
      // if (locked) {
      // return OperationCodes.TRAVERSE_UNLOCK_EXCLUSIVE;
      // }
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
  public OperationCodes processNode(TrieIterator<E> iter) throws TrieException {
    iter.removeNode();
    removed = true;
    backTrackRemoved = true;
    return OperationCodes.TRAVERSE_BACKTRACK;
  }

  @Override
  public OperationCodes endInternalTrieNode(E key) {
    if (backTrackRemoved) {
      // reprocess this iteration
      return OperationCodes.TRAVERSE_PROCESS;
    }
    return OperationCodes.TRAVERSE_BACKTRACK;
  }

  @Override
  public OperationCodes endTrie() {
    return OperationCodes.TRAVERSE_END;
  }

  @Override
  public OperationCodes noNextNode() {
    backTrackRemoved = false;
    return OperationCodes.TRAVERSE_BACKTRACK;
  }

  @Override
  public void lockIteration() throws TrieException {
    // lock ignored
  }

  @Override
  public void unlockIteration() throws TrieException {
    // unlock ignored
  }

}