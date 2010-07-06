package org.apache.trie.inmemory;

import org.apache.trie.base.TrieException;
import org.apache.trie.base.TrieIterator;
import org.apache.trie.base.TrieKey;
import org.apache.trie.base.TrieNode;

/**
 * The Decorator implementation for TrieIterator which throws
 * UnSupportedOperationException for each method.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 * @param <E>
 *          TrieKey used for this Trie
 * 
 */
public class AbstractIterator<E extends TrieKey<E>> implements TrieIterator<E> {

  @Override
  public void finishIteration() throws TrieException {
    throw new UnsupportedOperationException("finishIteration");
  }

  @Override
  public void insert(TrieNode<E> node) throws TrieException {
    throw new UnsupportedOperationException("insert");
  }

  @Override
  public void insertAtEnd(TrieNode<E> node) throws TrieException {
    throw new UnsupportedOperationException("insertAtEnd");
  }

  @Override
  public void lockExclusive() throws TrieException {
    throw new UnsupportedOperationException("lockExclusive");
  }

  @Override
  public TrieNode<E> previous() {
    throw new UnsupportedOperationException("previous");
  }

  @Override
  public void removeNode() throws TrieException {
    throw new UnsupportedOperationException("removeNode");
  }

  @Override
  public TrieNode<E> replaceNode(TrieNode<E> node) throws TrieException {
    throw new UnsupportedOperationException("replaceNode");
  }

  @Override
  public void reset() throws TrieException {
    throw new UnsupportedOperationException("reset");
  }

  @Override
  public void unlockExclusive() throws TrieException {
    throw new UnsupportedOperationException("unlockExclusive");
  }

  @Override
  public boolean hasNext() {
    throw new UnsupportedOperationException("hasNext");
  }

  @Override
  public TrieNode<E> next() {
    throw new UnsupportedOperationException("next");
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("remove");
  }

}
