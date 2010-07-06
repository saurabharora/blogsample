package org.apache.trie.base;

/**
 * This interface describe the extension interface for BackTrack Traversal in a
 * Trie. The Traversal uses callback mechanism inspired by SAX like api.The
 * extension allows the backtrack to previous node and get callbacks at the end
 * of the elements.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 * @param <E>
 *          TrieKey used for this Trie.
 */
public interface TraversalOperationWithBacktrack<E extends TrieKey<E>> extends
    TraversalOperation<E> {
  /**
   * This endInternalTrieNode method when we backtrack to a parent node in
   * iteration.
   * 
   * @param key
   *          current node which we backtracked to.
   * @return OperationCode returned by traversal.
   */
  OperationCodes endInternalTrieNode(E key);

  /**
   * The endTrie method is called when we backtrack to the root node of a Trie.
   * 
   * @return OperationCode returned by traversal.
   */
  OperationCodes endTrie();

}
