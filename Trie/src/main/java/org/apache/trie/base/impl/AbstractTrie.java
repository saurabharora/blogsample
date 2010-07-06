package org.apache.trie.base.impl;

import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import org.apache.trie.base.Trie;
import org.apache.trie.base.TrieBuilder;
import org.apache.trie.base.TrieException;
import org.apache.trie.base.TrieIterator;
import org.apache.trie.base.TrieKey;
import org.apache.trie.base.TrieNode;

/**
 * Abstract implementation of a Trie. This default implementation uses a Builder
 * to created the nodes and traverse the trie.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 * @param <E>
 *          TrieKey used for this Trie
 */
public abstract class AbstractTrie<E extends TrieKey<E>> implements Trie<E> {

  private final TrieBuilder<E> builder;
  /**
   * Root node of the Trie Data Structure.
   */
  protected final TrieNode<E> root;

  protected AbstractTrie(TrieBuilder<E> abuild) {
    builder = abuild;
    root = builder.createRootNode();
  }

  protected AbstractTrie(TrieBuilder<E> abuild, TrieNode<E> node) {
    builder = abuild;
    root = node;
  }

  @Override
  public void addElement(E ele) throws TrieException {
    InsertOperation<E> insertOps = new InsertOperation<E>(ele, builder);
    builder.getTraversalProvider().traverseTrie(insertOps, root, true);
  }

  @Override
  public boolean contains(E ele) throws TrieException {
    ContainsOperation<E> containOps = new ContainsOperation<E>(ele);
    builder.getTraversalProvider().traverseTrie(containOps, root, false);

    return (containOps.getNode() != null);
  }

  @Override
  public boolean removeElement(E ele) throws TrieException {
    RemoveOperation<E> removeop = new RemoveOperation<E>(ele);
    builder.getTraversalProvider().traverseTrieWithBackTrack(removeop, root,
        true);

    return removeop.isRemoved();
  }

  @Override
  public List<E> findPrefix(E ele) throws TrieException {
    List<E> result;
    FindPrefixOperation<E> prefixOps = new FindPrefixOperation<E>(ele);
    builder.getTraversalProvider().traverseTrie(prefixOps, root, false);
    TrieNode<E> prefixNode = prefixOps.getNode();

    if (prefixNode == null) {
      result = Collections.emptyList();
    } else {
      int count = ele.getKeyLength();
      E prefix = ele.getKeyFrom(0, Math.max(count - 2, 0));
      TraverseAllOperation<E> op1 = new TraverseAllOperation<E>(prefix);
      builder.getTraversalProvider().traverseTrieWithBackTrack(op1, prefixNode,
          false);
      result = op1.getElements();
    }
    return result;

  }

  public void printNodes(PrintStream out) throws TrieException {
    out.println("root");
    printNode(root, "", out);
  }

  private void printNode(TrieNode<E> current, String indent, PrintStream out)
      throws TrieException {

    String indentstr = indent + " ";
    out.println(indentstr + "node=" + current.hashCode());
    out.println(indentstr + "key=" + current.getKey());
    TrieIterator<E> iter = current.children().trieReadOnlyIterator();
    if (iter.hasNext()) {
      out.println(indentstr + "children------------");
      while (iter.hasNext()) {
        TrieNode<E> child = iter.next();
        printNode(child, indentstr, out);
      }
      out
          .println(indentstr + "--------------------------"
              + current.hashCode());

    } else {
      out.println(indentstr + "child EMPTY");
    }
    iter.finishIteration();
  }

}
