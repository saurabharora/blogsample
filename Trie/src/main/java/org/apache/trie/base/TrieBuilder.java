package org.apache.trie.base;

/**
 * TrieBuilder is a builder for Trie Nodes.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 * @param <E>
 *          TrieKey used for this TrieBuilder
 */
public interface TrieBuilder<E extends TrieKey<E>> {

  /**
   * Creates the Trie Node with Type as Root Node.
   * 
   * @return Root trie node.
   */
  TrieNode<E> createRootNode();

  /**
   * Creates a Trie Node. Type is not set as Root Node.
   * 
   * @param ele
   *          The element stored in this TrieNode.
   * @return The Trie Node created by Builder.
   */
  TrieNode<E> createNode(E ele);

  /**
   * The TraversalProvider provides the capability to traverse the nodes created
   * using this Trie Builder.
   * 
   * @return TraversalProvider for the created Nodes.
   */
  TraversalProvider<E> getTraversalProvider();
}
