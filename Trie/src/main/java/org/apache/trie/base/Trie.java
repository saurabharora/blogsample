package org.apache.trie.base;

import java.util.List;

/**
 * This interface defines the operation on a Trie DataStructure.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 * @param <E>
 *          TrieKey used for this Trie
 */
public interface Trie<E extends TrieKey<E>> {

  /**
   * Add elements to the Trie.
   * 
   * @param ele
   *          The element to add.
   * @throws TrieException
   */
  void addElement(E ele) throws TrieException;

  /**
   * List the Elements based on the prefix.
   * 
   * @param ele
   *          The prefix element to search.
   * @return List of Elements found.
   * @throws TrieException
   */
  List<E> findPrefix(E ele) throws TrieException;

  /**
   * Returns true if the Trie contains the element.
   * 
   * @param ele
   *          The element to check.
   * @return true if the element is found.
   * @throws TrieException
   */
  boolean contains(E ele) throws TrieException;

  /**
   * Returns true if the Element is removed from the trie. The removeElement
   * tries to remove the element from the trie and returns true if it exists and
   * removal is successfull.
   * 
   * @param ele
   *          The element to remove.
   * @return true if removal is successfull.
   * @throws TrieException
   */
  boolean removeElement(E ele) throws TrieException;
}
