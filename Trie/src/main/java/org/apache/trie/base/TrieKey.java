package org.apache.trie.base;

import java.io.Externalizable;

/**
 * The TrieKey is the data which is store in the Trie. This key specifies
 * special operation which allows any kind of DataType to behave as elements in
 * a Trie.
 * <p>
 * <b>Specification of TrieKey.</b>
 * </p>
 * <ul>
 * <li>TrieKey defines a unique EOLN character.</li>
 * <li>EOLN character can also form a unique TrieKey.</li>
 * <li>All TrieKey must end in a EOLN character Except keys created using
 * {@link #getKeyAt(int)} which must not have EOLN character.</li>
 * <li>Equality of TrieKey is defined by {@link #equalsTrie(TrieKey)}.</li>
 * <li>TrieKey consists of many sub TrieKeys which can be obtained using
 * {@link #getKeyAt(int)},{@link #getKeyFrom(int)},
 * {@link #getKeyFrom(int, int)} method .</li>
 * </ul>
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 * @param <T>
 *          Self Bounding Annotation on TrieKey.
 */
public interface TrieKey<T extends TrieKey<T>> extends Externalizable {

  /**
   * Returns the length of Key. The method returns the number of primitive keys
   * this compound key can be broken down.
   * 
   * @return The length of the key.
   */
  int getKeyLength();

  /**
   * Returns true if key represents a end of line.
   * 
   * @return true for EOLN characters.
   */
  boolean isEoln();

  /**
   * Return the TrieKey at index.
   * 
   * @param index
   *          The TrieKey at index.
   * @return The TrieKey at location index.
   */
  T getKeyAt(int index);

  /**
   * Return the new TrieKey which is a subkey from beginIndex location.
   * 
   * @param beginIndex
   *          index from which to return TrieKey.
   * @return The TrieKey starting at location index.
   */
  T getKeyFrom(int beginIndex);

  /**
   * Return the new TrieKey which is a subkey from beginIndex location to
   * endIndex location.
   * 
   * @param beginIndex
   *          start index for new TrieKey.
   * @param endIndex
   *          end index for new TrieKey.
   * @return The new TrieKey from beginIndex to endIndex
   */
  T getKeyFrom(int beginIndex, int endIndex);

  /**
   * Concatenates the key and returned a new TrieKey.
   * 
   * @param key
   *          The key to concatenate with.
   * @return The new Concatenated TrieKey
   */
  T concat(T key);

  /**
   * The compare method which compares the first keys of both specified TrieKey
   * with this TrieKey.
   * 
   * @param key
   *          The Triekey to compare with.
   * @return 0 for equality,-1 for greater than, +1 for less then.
   */
  int compareFirstIndex(T key);

  /**
   * The equality Function for a Trie. This equality function compares all the
   * subkeys of a Triekey.
   * 
   * @param key
   *          The TrieKey to be checked for quality.
   * @return true for equality, else false.
   */
  boolean equalsTrie(T key);
}
