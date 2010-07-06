package org.apache.trie.datatypes;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.trie.base.TrieKey;

/**
 * This class presents the example Triekey for a Integer representation.
 * 
 * @author <a href="mailto:saurabh@saurabharora.me">Saurabh Arora</a>
 */
public class IntegerKey implements TrieKey<IntegerKey> {

  private byte[] intdigits;
  private static final byte EOLN = -1;

  /**
   * Default Constructor.It represents an EOLN Key.
   */
  public IntegerKey() {
    intdigits = new byte[0];
    intdigits[0] = EOLN;
  }

  /**
   * Constructor which store Integer data in key.
   */
  public IntegerKey(int arg) {
    fillbytes(arg);
  }

  private void fillbytes(int arg) {
    int size = 0;
    int tmp = arg;
    while (tmp > 0) {
      tmp = tmp / 10;
      size++;
    }

    intdigits = new byte[size + 1];
    tmp = arg;
    int count = size - 1;
    while (tmp > 0) {
      intdigits[count--] = (byte) (tmp % 10);
      tmp = tmp / 10;
    }
    intdigits[size] = EOLN;
  }

  @Override
  public String toString() {
    StringBuffer buf = new StringBuffer();
    for (byte i : intdigits) {
      buf.append(Integer.toString((int) i));
    }
    return buf.toString();
  }

  public IntegerKey(byte[] digits) {
    intdigits = new byte[digits.length];
    System.arraycopy(digits, 0, intdigits, 0, digits.length);
  }

  IntegerKey(byte digits) {

    intdigits = new byte[1];
    intdigits[0] = digits;
  }

  @Override
  public int compareFirstIndex(IntegerKey arg) {
    int count = (intdigits[0] - arg.intdigits[0]);
    if (count > 0) {
      count = 1;
    } else if (count < 0) {
      count = -1;
    }
    return count;
  }

  @Override
  public IntegerKey concat(IntegerKey arg) {
    int len;
    if (intdigits[intdigits.length - 1] == EOLN) {
      len = Math.max(intdigits.length - 1, 0);
    } else {
      len = intdigits.length;
    }
    int count = len + arg.intdigits.length;
    byte[] tmp = new byte[count];
    System.arraycopy(intdigits, 0, tmp, 0, len);
    System.arraycopy(arg.intdigits, 0, tmp, len, arg.intdigits.length);
    return new IntegerKey(tmp);
  }

  @Override
  public boolean equalsTrie(IntegerKey arg) {
    boolean equal = false;
    if (intdigits.length == arg.intdigits.length) {
      equal = true;
      for (int i = 0; i < intdigits.length && equal; i++) {
        if (intdigits[i] != arg.intdigits[i]) {
          equal = false;
        }
      }
    }
    return equal;
  }

  @Override
  public IntegerKey getKeyAt(int arg) {
    return new IntegerKey(intdigits[arg]);
  }

  @Override
  public IntegerKey getKeyFrom(int arg) {
    byte[] tmp = new byte[intdigits.length - arg];
    System.arraycopy(intdigits, arg, tmp, 0, tmp.length);
    return new IntegerKey(tmp);
  }

  @Override
  public int getKeyLength() {
    return intdigits.length;
  }

  @Override
  public boolean isEoln() {
    return (intdigits.length == 1 && intdigits[0] == EOLN);
  }

  @Override
  public IntegerKey getKeyFrom(int beginIndex, int endIndex) {
    byte[] tmp = new byte[endIndex - beginIndex + 1];
    System.arraycopy(intdigits, beginIndex, tmp, 0, tmp.length);
    return new IntegerKey(tmp);
  }

  @Override
  public void readExternal(ObjectInput instream) throws IOException,
      ClassNotFoundException {
    int len = instream.read();
    intdigits = new byte[len];
    if (instream.read(intdigits) != len) {
      throw new IOException("Cannot read IntegerKey completely");
    }

  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.write(intdigits.length);
    out.write(intdigits);
  }

}
