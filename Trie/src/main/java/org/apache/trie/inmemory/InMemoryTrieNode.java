package org.apache.trie.inmemory;

import org.apache.trie.base.TrieKey;
import org.apache.trie.base.TrieNode;
import org.apache.trie.base.TrieNodeList;

/**
 * The TrieNode for the InMemoryTrie. It represents the nodes using in InMemory
 * Tries.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 * @param <E>
 *          TrieKey used for this Trie.
 */
class InMemoryTrieNode<E extends TrieKey<E>> implements TrieNode<E> {

  private final Type type;
  private final E key;
  private final InMemoryTrieNodeList<E> nodes = new InMemoryTrieNodeList<E>(
      this);

  public InMemoryTrieNode(Type atype, E ele) {
    type = atype;
    key = ele;
  }

  @Override
  public TrieNodeList<E> children() {
    return nodes;
  }

  @Override
  public E getKey() {
    return key;
  }

  @Override
  public org.apache.trie.base.TrieNode.Type getType() {
    if (type != Type.ROOT_NODE) {
      return ((nodes.isEmpty()) ? Type.LEAF_NODE : Type.INTERNAL_NODE);
    }
    return type;
  }

  @Override
  public String toString() {
    if (key == null) {
      return "[InMemoryTrieNode]key=null";
    } else {
      return "[InMemoryTrieNode]" + key.toString();
    }
  }

}
