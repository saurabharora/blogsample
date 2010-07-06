/**
 * 
 */
package org.apache.trie.base.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.trie.base.OperationCodes;
import org.apache.trie.base.TraversalOperationWithBacktrack;
import org.apache.trie.base.TrieException;
import org.apache.trie.base.TrieIterator;
import org.apache.trie.base.TrieKey;

/**
 * This traversal returns the list of all the TrieKeys stored in the current
 * Trie starting at the specified node.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 * @param <E>
 */
class TraverseAllOperation<E extends TrieKey<E>> implements
    TraversalOperationWithBacktrack<E> {

  private final List<E> elements = new ArrayList<E>();
  private final Stack<E> prefixs = new Stack<E>();

  private E treePrefix;

  TraverseAllOperation(E prefix) {
    super();
    treePrefix = prefix;
  }

  public List<E> getElements() {
    return elements;
  }

  @Override
  public OperationCodes leafTrieNode(E key) {
    elements.add(treePrefix.concat(key));
    return OperationCodes.TRAVERSE_SIBLING;
  }

  @Override
  public OperationCodes startInternalTrieNode(E key) {
    prefixs.push(treePrefix);
    treePrefix = treePrefix.concat(key);
    return OperationCodes.TRAVERSE_CHILD;
  }

  @Override
  public OperationCodes endInternalTrieNode(E key) {
    if (prefixs.isEmpty()) {
      return OperationCodes.TRAVERSE_ERROR;
    } else {
      treePrefix = prefixs.pop();
      return OperationCodes.TRAVERSE_SIBLING;
    }
  }

  @Override
  public OperationCodes endTrie() {
    return OperationCodes.TRAVERSE_END;
  }

  @Override
  public OperationCodes processNode(TrieIterator<E> iter) {
    return OperationCodes.TRAVERSE_ERROR;
  }

  @Override
  public OperationCodes noNextNode() {
    return OperationCodes.TRAVERSE_BACKTRACK;
  }

  @Override
  public void lockIteration() throws TrieException {
    // TODO Auto-generated method stub

  }

  @Override
  public void unlockIteration() throws TrieException {
    // TODO Auto-generated method stub

  }

}