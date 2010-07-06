package org.apache.trie.base;

/**
 * The Tree Node dataStructure which is used in Trie. It contains an element of
 * type TrieKey. The TrieNode is classified into Root Node, Internal Node and
 * Leaf Node. This classification is imposed using @link {@link Type}
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 * @param <E>
 *          Self Bounding Annotation on TrieKey.
 */
public interface TrieNode<E extends TrieKey<E>> {

  /**
   * Returns the TrieKey stored in this TrieNode.
   * 
   * @return The TrieKey stored in this Node.
   */
  E getKey();

  /**
   * Returns a children elements of this Node as a {@link TrieNodeList}.
   * 
   * @return The childrens of this TrieNode.
   */
  TrieNodeList<E> children();

  /**
   * The Type of this TrieNode. The Type is defined by the enumeration
   * {@link Type}.
   * 
   * @return The Type of this TrieNode.
   */
  Type getType();

  /**
   * Enumeration defining the Type of TrieNode.
   * 
   * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
   */
  enum Type {
    /**
     * The Enum constant to define a Root Node which has no parent Node.
     */
    ROOT_NODE,
    /**
     * The Enum constant to define a Internal Node which has children nodes.
     */
    INTERNAL_NODE,
    /**
     * The Enum constant to define a leaf Node which has no children nodes.
     */
    LEAF_NODE;
  }

}
