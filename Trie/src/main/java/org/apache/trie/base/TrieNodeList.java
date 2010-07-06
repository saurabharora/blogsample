package org.apache.trie.base;

/**
 * The class in a List of TrieNodes. This class is used to store the children of
 * a TrieNode.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 * @param <E>
 *          Self Bounding Annotation on TrieKey.
 */
public interface TrieNodeList<E extends TrieKey<E>> {

  /**
   * Obtain a ReadOnly {@link TrieIterator} on the nodes stored. Any This
   * Iterator is readOnly and not support any of the modification write
   * Operation {@link TrieIterator#insert(TrieNode)},
   * {@link TrieIterator#insertAtEnd(TrieNode)}, {@link TrieIterator#remove()},
   * 
   * @return The iterator store in this Node.
   * @throws TrieException
   */
  TrieIterator<E> trieReadOnlyIterator() throws TrieException;

  /**
   * Obtain a Writeable{@link TrieIterator} on the nodes stored.
   * 
   * @return The iterator store in this Node.
   */
  TrieIterator<E> trieWritableIterator() throws TrieException;

  /**
   * Returns if the nodelist is empty.
   * 
   * @return true if nodelist has no nodes.
   */
  boolean isEmpty();
}
