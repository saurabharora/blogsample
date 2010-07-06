package org.apache.trie.base;

/**
 * This Enumeration defines the OperationCode used to control Traversal
 * Operation.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 */
public enum OperationCodes {
  /**
   * Operation code to represent an error occurred in traversal.
   */
  TRAVERSE_ERROR,
  /**
   * Operation code to execute processNodes().
   */
  TRAVERSE_PROCESS,
  /**
   * Operation code to visit child nodes..
   */
  TRAVERSE_CHILD,
  /**
   * Operation code to visit Sibling nodes.
   */
  TRAVERSE_SIBLING,
  /**
   * Operation code to represent end of Operation.
   */
  TRAVERSE_END,
  /**
   * Operation code to support BackTrack in a Trie.
   */
  TRAVERSE_BACKTRACK,
  /**
   * Operation code to lock the Node exclusively.
   */
  TRAVERSE_LOCK_EXCLUSIVE;

}