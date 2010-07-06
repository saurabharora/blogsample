package org.apache.trie.base;

/**
 * This Provider interface provides method to traverse an Trie. The Traversal in
 * carried out using {@link TraversalOperation } callback handlers.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 * @param <E>
 *          TrieKey used for this Trie.
 */
public interface TraversalProvider<E extends TrieKey<E>> {

  /**
   * Traverse a TrieNode without backtracking support. The traversal can lock
   * and modify an node only when modifiable is set to true.
   * 
   * @param operation
   * @param node
   * @param modifiable
   *          Can the traversal modify the nodes.
   * @return OperationCode returned by traversal.
   * @throws TrieException
   */
  OperationCodes traverseTrie(TraversalOperation<E> operation,
      TrieNode<E> node, boolean modifiable) throws TrieException;

  /**
   * Traverse a TrieNode <b>with</b> backtracking support. The traversal can
   * lock and modify an node only when modifiable is set to true. The @link
   * {@link OperationCodes#TRAVERSE_BACKTRACK} is used to backtrack in this
   * case.
   * 
   * @param operation
   * @param node
   * @param modifiable
   *          Can the traversal modify the nodes.
   * @return OperationCode returned by traversal.
   * @throws TrieException
   */
  OperationCodes traverseTrieWithBackTrack(
      TraversalOperationWithBacktrack<E> operation, TrieNode<E> node,
      boolean modifiable) throws TrieException;

}
