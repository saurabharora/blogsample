package org.apache.trie.inmemory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.PrintStream;
import java.util.List;

import org.apache.trie.datatypes.IntegerKey;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for IntegerKey Trie.
 */
public class InMemoryIntegerTrieTest {
  private InMemoryTrie<IntegerKey> trie;
  private PrintStream stream;

  @Before
  public void setupData() {
    stream = System.out;
    trie = new InMemoryTrie<IntegerKey>();
  }

  @Test
  public void checkOperations() throws Exception {

    int[] lists = { 123, 145, 100, 154, 121, 144 };
    // int[] lists = { 123, 121 };
    IntegerKey key;
    for (int value : lists) {
      key = new IntegerKey(value);
      trie.addElement(key);
      assertTrue(key.toString(), trie.contains(key));
    }
    trie.printNodes(stream);
    assertTrue("checking 123", trie.contains(new IntegerKey(123)));

    key = new IntegerKey(1);
    List<IntegerKey> keys = trie.findPrefix(key);
    for (IntegerKey value : keys) {
      stream.println(value);
    }
    assertSame("keys size:" + keys.size(), keys.size(), lists.length);

    stream.println("-----------------");
    for (int value : lists) {
      key = new IntegerKey(value);
      stream.println("Removing:" + key.toString());
      assertTrue(key.toString(), trie.removeElement(key));
      assertFalse(key.toString(), trie.contains(key));
      key = new IntegerKey(1);
      keys = trie.findPrefix(key);
      for (IntegerKey value1 : keys) {
        stream.println(value1);
      }
      stream.println("-----------------");
    }

    key = new IntegerKey(1);
    keys = trie.findPrefix(key);
    for (IntegerKey value : keys) {
      stream.println(value);
    }
    assertSame("keys size:" + keys.size(), keys.size(), 0);
  }

}
