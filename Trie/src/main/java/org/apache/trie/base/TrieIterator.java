package org.apache.trie.base;

import java.util.Iterator;

/**
 * This is an extension for Iterator which provides for method for insertion and
 * access to previous node. The iteration is carried out on the children nodes
 * {@link TrieNodeList}.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 * @param <E>
 *          TrieKey used for this TrieIterator.
 */
public interface TrieIterator<E extends TrieKey<E>> extends
    Iterator<TrieNode<E>> {

  /**
   * Inserts the node before the previously traversed Element. Formally, This
   * method inserts the specified node before the last element returned by the
   * next() method on this iterator. In case of untraversed Iterator, the
   * insertion is at the head.
   * 
   * @see #next()
   * @param node
   *          The node to insert.
   * @throws TrieException
   */
  void insert(TrieNode<E> node) throws TrieException;

  /**
   * Inserts the node at the End of the current Traversal List.
   * 
   * @param node
   *          The node to insert at the end.
   * @throws TrieException
   */
  void insertAtEnd(TrieNode<E> node) throws TrieException;

  /**
   * Obtain the previous element traversed. This method returns the previous
   * element obtain from {@link #next()} method. The return type is null for
   * iteration that is not started.
   * 
   * @return The previous visited node.
   */
  TrieNode<E> previous();

  /**
   * This method replaces the last traversed node. This is provided to make
   * replacement as an atomic operation. Not a series of remove(), insert()
   * operation.
   * 
   * @param node
   *          The node to be replaced.
   * @throws TrieException
   */
  TrieNode<E> replaceNode(TrieNode<E> node) throws TrieException;

  /**
   * This method signals completion of iteration. This is required, to release
   * locks that are held.
   * 
   * @throws TrieException
   */
  void finishIteration() throws TrieException;

  /**
   * This method is used to remove the remove the previous visited Node.
   * 
   * @throws TrieException
   */
  void removeNode() throws TrieException;

  /**
   * Locks the Iterator in an exclusive mode.
   * 
   * @throws TrieException
   */
  void lockExclusive() throws TrieException;

  /**
   * UnLocks the Iterator from an exclusive mode.
   * 
   * @throws TrieException
   */
  void unlockExclusive() throws TrieException;

  /**
   * Reset the iterator to start from first element for the iteration.
   * 
   * @throws TrieException
   */
  void reset() throws TrieException;
}
