package org.apache.trie.base;

/**
 * This interface describe the basic interface for Traversal in a Trie. The
 * Traversal uses callback mechanism inspired by SAX like api.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 * @param <E>
 *          TrieKey used for this Trie.
 */
public interface TraversalOperation<E extends TrieKey<E>> {

  /**
   * The callback interface when an internal node is found.
   * 
   * @param node
   *          current internal node
   * @return Next operation
   */
  OperationCodes startInternalTrieNode(E key) throws TrieException;

  /**
   * The processNode method is used to modify the Trie using a TrieIterator
   * interface.
   * 
   * @param iter
   * @return Next operation
   * @throws TrieException
   */
  OperationCodes processNode(TrieIterator<E> iter) throws TrieException;

  /**
   * The callback when we reach end of try traversal.
   * 
   * @return Next operation
   * @throws TrieException
   */
  OperationCodes noNextNode() throws TrieException;

  /**
   * The callback interface when an leaf node is found.
   * 
   * @param node
   * @return Next operation
   * @throws TrieException
   */
  OperationCodes leafTrieNode(E key) throws TrieException;

  /**
   * Callback when the Iterator is locked in exclusive mode.
   * 
   * @throws TrieException
   */
  void lockIteration() throws TrieException;

  /**
   * Callback when Iterator is unlocked from exclusive mode.
   * 
   * @throws TrieException
   */
  void unlockIteration() throws TrieException;
}
