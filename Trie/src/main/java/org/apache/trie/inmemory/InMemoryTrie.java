package org.apache.trie.inmemory;

import org.apache.trie.base.TrieKey;
import org.apache.trie.base.impl.AbstractTrie;

/**
 * Provides a implementation of Trie which is completely in Memory.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 * @param <E>
 *          TrieKey used for this Trie.
 */
public class InMemoryTrie<E extends TrieKey<E>> extends AbstractTrie<E> {

  public InMemoryTrie() {
    super(new InMemoryTrieBuilder<E>());
  }

}
