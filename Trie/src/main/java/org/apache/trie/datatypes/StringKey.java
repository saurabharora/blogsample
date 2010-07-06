package org.apache.trie.datatypes;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.trie.base.TrieKey;

/**
 * This class presents the example Triekey for a String representation.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 */
public class StringKey implements TrieKey<StringKey> {

  private static final String EOLN = "\0";
  private String value = EOLN;

  /**
   * Default Constructor for an StringKey, with no content. it represents an
   * EOLN Key.
   */
  public StringKey() {
    value = EOLN;
  }

  /**
   * Constructor for an StringKey, with specified string as content.
   * 
   * @param arg
   *          The String used as key.
   */
  public StringKey(String arg) {
    if (arg.endsWith(EOLN)) {
      value = arg;
    } else {
      value = arg + EOLN;
    }

  }

  /**
   * Constructor with only one char. Used by getKeyAt() method primarily.
   * 
   * @param charAt
   *          character to store.
   */
  private StringKey(char charAt) {
    char[] data = new char[1];
    data[0] = charAt;
    value = new String(data);
  }

  @Override
  public int compareFirstIndex(StringKey arg) {
    int count = (value.charAt(0) - arg.value.charAt(0));
    if (count > 0) {
      count = 1;
    } else if (count < 0) {
      count = -1;
    }
    return count;
  }

  @Override
  public StringKey concat(StringKey arg) {
    StringKey result;
    if (value.endsWith(EOLN)) {
      String newvalue = value.substring(0, value.length() - 1);
      result = new StringKey(newvalue.concat(arg.value));
    } else {
      result = new StringKey(value.concat(arg.value));
    }
    return result;
  }

  @Override
  public boolean equalsTrie(StringKey arg) {
    return value.equals(arg.value);
  }

  @Override
  public StringKey getKeyAt(int arg) {
    StringKey result = null;
    if (arg <= value.length()) {
      result = new StringKey(value.charAt(arg));
    }
    return result;
  }

  @Override
  public StringKey getKeyFrom(int arg) {
    StringKey result = null;
    if (arg <= value.length()) {
      result = new StringKey(value.substring(arg));
    }
    return result;
  }

  @Override
  public int getKeyLength() {
    return value.length();
  }

  @Override
  public boolean isEoln() {
    return value.equals(EOLN);
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  public StringKey getKeyFrom(int beginIndex, int endIndex) {
    StringKey result = null;
    if (beginIndex <= value.length()) {

      result = new StringKey(value.substring(beginIndex, endIndex));
    }
    return result;
  }

  @Override
  public void readExternal(ObjectInput instream) throws IOException,
      ClassNotFoundException {
    value = instream.readLine();

  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeChars(value);

  }

}
