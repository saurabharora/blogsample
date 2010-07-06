package org.apache.trie.inmemory;

import java.util.Stack;

import org.apache.trie.base.OperationCodes;
import org.apache.trie.base.TraversalOperation;
import org.apache.trie.base.TraversalOperationWithBacktrack;
import org.apache.trie.base.TraversalProvider;
import org.apache.trie.base.TrieException;
import org.apache.trie.base.TrieIterator;
import org.apache.trie.base.TrieKey;
import org.apache.trie.base.TrieNode;

/**
 * The TraversalProvider for the InMemoryTrie. This class is not Thread safe.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 * @param <E>
 *          TrieKey used for this Trie.
 */
class InMemoryTraversalProvider<E extends TrieKey<E>> implements
    TraversalProvider<E> {
  private TrieNode<E> current;
  private TrieIterator<E> iter;
  private boolean locked;

  private OperationCodes handleCommonOperations(OperationCodes type,
      TraversalOperation<E> operation, boolean modifiable) throws TrieException {
    OperationCodes nextOp = OperationCodes.TRAVERSE_ERROR;
    switch (type) {
    case TRAVERSE_CHILD:
      if (modifiable) {
        iter = current.children().trieWritableIterator();
      } else {
        iter = current.children().trieReadOnlyIterator();
      }

    case TRAVERSE_SIBLING:
      if (iter != null) {
        if (iter.hasNext()) {
          current = iter.next();
          nextOp = getNextOp(operation, current);
        } else {
          nextOp = operation.noNextNode();
        }
      }
      break;
    case TRAVERSE_LOCK_EXCLUSIVE:
      if (!modifiable) {
        throw new TrieException(
            "Cannot set OperationCodes as Lock Exclusive for readOnly Traversal");
      }
      if (iter != null) {
        iter.lockExclusive();
        iter.reset();
      }
      operation.lockIteration();
      locked = true;
      nextOp = OperationCodes.TRAVERSE_SIBLING;
      break;
    // case TRAVERSE_UNLOCK_EXCLUSIVE:
    // if (iter != null) {
    // iter.unlockExclusive();
    // // iter.reset();
    // }
    // operation.unlockNode();
    // locked = false;
    // nextOp = OperationCodes.TRAVERSE_SIBLING;
    // break;
    case TRAVERSE_PROCESS:
      if (modifiable) {
        if (locked) {
          nextOp = operation.processNode(iter);
        } else {
          nextOp = OperationCodes.TRAVERSE_LOCK_EXCLUSIVE;
        }
      } else {
        // no locking if not modifiable.
        nextOp = operation.processNode(iter);
      }
      break;
    case TRAVERSE_ERROR:
      throw new TrieException("Traversal Error");
    default:
      throw new TrieException("Traversal Error for type=" + type);
    }
    return nextOp;
  }

  @Override
  public OperationCodes traverseTrie(TraversalOperation<E> operation,
      TrieNode<E> node, boolean modifiable) throws TrieException {
    current = node;
    iter = null;
    locked = false;

    OperationCodes nextOp = OperationCodes.TRAVERSE_END;
    nextOp = getNextOp(operation, current);
    if ((nextOp == OperationCodes.TRAVERSE_LOCK_EXCLUSIVE) && (modifiable)) {
      operation.lockIteration();
      locked = true;
      nextOp = getNextOp(operation, current);
    }

    try {

      while (nextOp != OperationCodes.TRAVERSE_END) {

        if ((nextOp == OperationCodes.TRAVERSE_CHILD) && (iter != null)) {
          if (locked) {
            operation.unlockIteration();
            locked = false;
          }
          iter.finishIteration();
        }
        nextOp = handleCommonOperations(nextOp, operation, modifiable);

      }
    } finally {

      if (iter != null) {
        locked = false;
        iter.finishIteration();
      }
    }
    return nextOp;
  }

  protected OperationCodes getNextOp(TraversalOperation<E> operation,
      TrieNode<E> node) throws TrieException {
    OperationCodes result;
    switch (node.getType()) {
    case ROOT_NODE:
      result = OperationCodes.TRAVERSE_CHILD;
      break;
    case LEAF_NODE:
      result = operation.leafTrieNode(node.getKey());
      break;
    case INTERNAL_NODE:
      result = operation.startInternalTrieNode(node.getKey());
      break;
    default:
      result = OperationCodes.TRAVERSE_ERROR;
      break;
    }

    return result;
  }

  @Override
  public OperationCodes traverseTrieWithBackTrack(
      TraversalOperationWithBacktrack<E> operation, TrieNode<E> node,
      boolean modifiable) throws TrieException {
    current = node;
    iter = null;
    locked = false;
    OperationCodes nextOp = getNextOp(operation, current);
    if ((nextOp == OperationCodes.TRAVERSE_LOCK_EXCLUSIVE) && (modifiable)) {
      operation.lockIteration();
      locked = true;
      nextOp = getNextOp(operation, current);
    }
    Stack<TrieIterator<E>> stack = new Stack<TrieIterator<E>>();

    try {
      while (nextOp != OperationCodes.TRAVERSE_END) {

        switch (nextOp) {

        case TRAVERSE_BACKTRACK:
          if (iter != null) {
            if (locked) {
              operation.unlockIteration();
              locked = false;
            }
            iter.finishIteration();
          }
          if (stack.isEmpty()) {
            iter = null;
            nextOp = operation.endTrie();
          } else {

            iter = stack.pop();
            current = iter.previous();
            if (current != null) {
              nextOp = operation.endInternalTrieNode(current.getKey());
            }
          }
          break;
        case TRAVERSE_CHILD:
          if (iter != null) {
            if (locked) {
              iter.unlockExclusive();
              operation.unlockIteration();
              locked = false;
            }
            stack.push(iter);
          }
          // intentional fall through
        default:
          nextOp = handleCommonOperations(nextOp, operation, modifiable);
          break;
        }// end of switch

      }

    } finally {

      if (iter != null) {
        iter.finishIteration();
        locked = false;
      }

      if (!stack.isEmpty()) {
        while (!stack.isEmpty()) {
          stack.pop().finishIteration();
        }
      }
    }
    return nextOp;
  }

}
