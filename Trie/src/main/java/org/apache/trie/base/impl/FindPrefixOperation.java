/**
 * 
 */
package org.apache.trie.base.impl;

import org.apache.trie.base.OperationCodes;
import org.apache.trie.base.Trie;
import org.apache.trie.base.TrieKey;

/**
 * This Traversal Operation is used for prefix search in a Trie. It is used in
 * the {@link Trie#findPrefix(TrieKey) }operation.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 * @param <E>
 *          TrieKey used for this Trie.
 */
class FindPrefixOperation<E extends TrieKey<E>> extends ContainsOperation<E> {

  FindPrefixOperation(E arg) {
    super(arg);

  }

  @Override
  public OperationCodes startInternalTrieNode(E key) {
    int comp = key.compareFirstIndex(ele);
    if (comp == 0) {
      ele = ele.getKeyFrom(1);
      if (ele.isEoln()) {
        return OperationCodes.TRAVERSE_PROCESS;
      }
      return OperationCodes.TRAVERSE_CHILD;
    } else if (comp == 1) {
      return OperationCodes.TRAVERSE_SIBLING;
    } else {
      // else comp == -1
      return OperationCodes.TRAVERSE_END;
    }
  }
}