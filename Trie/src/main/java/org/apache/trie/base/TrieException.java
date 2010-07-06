package org.apache.trie.base;

/**
 * This TrieException is the base Exception for the Trie based system.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 */

public class TrieException extends Exception {
  public TrieException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public TrieException(String arg) {
    super(arg);
  }
}
