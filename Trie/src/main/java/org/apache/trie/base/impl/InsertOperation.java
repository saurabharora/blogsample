/**
 * 
 */
package org.apache.trie.base.impl;

import org.apache.trie.base.OperationCodes;
import org.apache.trie.base.TraversalOperation;
import org.apache.trie.base.Trie;
import org.apache.trie.base.TrieBuilder;
import org.apache.trie.base.TrieException;
import org.apache.trie.base.TrieIterator;
import org.apache.trie.base.TrieKey;
import org.apache.trie.base.TrieNode;

/**
 * This Traversal Operation is used to insert a node in the Trie. It is used in
 * the {@link Trie#addElement(TrieKey) }operation.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 * @param <E>
 *          TrieKey used for this Trie.
 */
class InsertOperation<E extends TrieKey<E>> implements TraversalOperation<E> {

  private boolean inserted = false;
  private boolean insertAtEnd = false;
  private boolean locked = false;
  private final TrieBuilder<E> builder;
  private E ele;
  private TrieNode<E> replaceNode = null;

  InsertOperation(E arg, TrieBuilder<E> abuilder) {
    super();
    ele = arg;
    builder = abuilder;
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
      return OperationCodes.TRAVERSE_PROCESS;
    }
  }

  @Override
  public OperationCodes processNode(TrieIterator<E> iter) throws TrieException {
    if (insertAtEnd) {
      iter.insertAtEnd(builder.createNode(ele));
    } else {
      if (replaceNode == null) {
        iter.insert(builder.createNode(ele));
      } else {
        iter.replaceNode(replaceNode);
        replaceNode = null;
      }
    }
    inserted = true;
    return OperationCodes.TRAVERSE_END;
  }

  @Override
  public OperationCodes leafTrieNode(E key) throws TrieException {
    int compvalue = key.compareFirstIndex(ele);
    if (compvalue == 0) {

      if (key.equalsTrie(ele)) {
        inserted = true;
        return OperationCodes.TRAVERSE_END;
      }

      if (key.getKeyLength() > 1) {

        if (!locked) {
          return OperationCodes.TRAVERSE_LOCK_EXCLUSIVE;
        }

        InsertOperation<E> insertOps = new InsertOperation<E>(key, builder);
        E newkey1 = key.getKeyAt(0);
        TrieNode<E> newnode = builder.createNode(newkey1);
        builder.getTraversalProvider().traverseTrie(insertOps, newnode, true);
        insertOps = new InsertOperation<E>(ele, builder);
        builder.getTraversalProvider().traverseTrie(insertOps, newnode, true);
        replaceNode = newnode;
        return OperationCodes.TRAVERSE_PROCESS;
      } else {
        ele = ele.getKeyFrom(1);
        return OperationCodes.TRAVERSE_CHILD;
      }
    } else if (compvalue == -1) {
      // position to add as previous node.
      return OperationCodes.TRAVERSE_PROCESS;
    }

    return OperationCodes.TRAVERSE_SIBLING;
  }

  public boolean isInserted() {
    return inserted;
  }

  @Override
  public OperationCodes noNextNode() throws TrieException {
    if (!inserted) {
      // time to lock the node.
      insertAtEnd = true;
      return OperationCodes.TRAVERSE_PROCESS;
    }
    return OperationCodes.TRAVERSE_ERROR;
  }

  @Override
  public void lockIteration() throws TrieException {
    locked = true;
  }

  @Override
  public void unlockIteration() throws TrieException {
    locked = false;
  }

}