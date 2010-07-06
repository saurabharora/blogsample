package org.apache.trie.inmemory;

import org.apache.trie.base.TraversalProvider;
import org.apache.trie.base.TrieBuilder;
import org.apache.trie.base.TrieKey;
import org.apache.trie.base.TrieNode;

/**
 * The TrieBuilder for the InMemoryTrie. It creates the nodes for the InMemory
 * Tries.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 * @param <E>
 *          TrieKey used for this Trie.
 */
class InMemoryTrieBuilder<E extends TrieKey<E>> implements TrieBuilder<E> {

  @Override
  public TrieNode<E> createNode(E ele) {

    InMemoryTrieNode<E> node = new InMemoryTrieNode<E>(TrieNode.Type.LEAF_NODE,
        ele);
    return node;
  }

  @Override
  public TrieNode<E> createRootNode() {

    return new InMemoryTrieNode<E>(TrieNode.Type.ROOT_NODE, null);
  }

  @Override
  public TraversalProvider<E> getTraversalProvider() {
    return new InMemoryTraversalProvider<E>();
  }

}
