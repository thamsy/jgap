/*
 * This file is part of JGAP.
 *
 * JGAP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * JGAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with JGAP; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.jgap.impl;

import java.util.*;

import org.jgap.*;

/**
 * A Gene implementation that supports a string for its allele. The valid
 * alphabet as well as the minimum and maximum length of the string can be
 * specified.<p>
 * An alphabet == null indicates that all characters are seen as valid.<br>
 * An alphabet == "" indicates that no character is seen to be valid.<p>
 * Partly copied from IntegerGene.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class StringGene
    implements Gene {
  //Constants for ready-to-use alphabets or serving as part of concetenation
  public static final String ALPHABET_CHARACTERS_UPPER =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  public static final String ALPHABET_CHARACTERS_LOWER =
      "abcdefghijklmnopqrstuvwxyz";

  public static final String ALPHABET_CHARACTERS_DIGITS = "0123456789";

  public static final String ALPHABET_CHARACTERS_SPECIAL = "+.*/\\,;@";

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.16 $";

  private int m_minLength;

  private int m_maxLength;

  private String m_alphabet;

  /**
   * Holds the configuration object associated with the Gene. The configuration
   * object is important to obtain referenced objects from it, like the
   * RandomGenerator.
   */
  private transient Configuration m_configuration;

  /**
   * Optional helper class for checking if a given allele value to be set
   * is valid. If not the allele value may not be set for the gene!
   */
  private IGeneConstraintChecker m_geneAlleleChecker;

  /**
   * References the internal String value (allele) of this Gene.
   */
  private String m_value;

  /**
   * @author Klaus Meffert
   * @since 1.1
   */
  public StringGene() {
    this(0,0);
  }

  /**
   *
   * @param a_minLength minimum valid length of allele
   * @param a_maxLength maximum valid length of allele
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public StringGene(int a_minLength, int a_maxLength) {
    this(a_minLength, a_maxLength, new DefaultConfiguration());
  }

  /**
   *
   * @param a_minLength int
   * @param a_maxLength int
   * @param a_configuration Configuration
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public StringGene(int a_minLength, int a_maxLength, Configuration a_configuration) {
    this(a_minLength, a_maxLength,  null, a_configuration);
  }

  /**
   *
   * @param a_minLength minimum valid length of allele
   * @param a_maxLength maximum valid length of allele
   * @param a_alphabet valid aplhabet for allele
   * @param a_configuration
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public StringGene(int a_minLength, int a_maxLength, String a_alphabet, Configuration a_configuration) {
    if (a_minLength < 0) {
      throw new IllegalArgumentException(
          "minimum length must be greater than"
          + " zero!");
    }
    if (a_maxLength < a_minLength) {
      throw new IllegalArgumentException(
          "minimum length must be smaller than"
          + " or equal to maximum length!");
    }
    m_minLength = a_minLength;
    m_maxLength = a_maxLength;
    m_configuration = a_configuration;
    setAlphabet(a_alphabet);
  }

  /**
   *
   * @param a_minLength int
   * @param a_maxLength int
   * @param a_alphabet String
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public StringGene(int a_minLength, int a_maxLength, String a_alphabet) {
    this(a_minLength, a_maxLength, a_alphabet, new DefaultConfiguration());
  }

  /**
   * Executed by the genetic engine when this Gene instance is no
   * longer needed and should perform any necessary resource cleanup.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void cleanup() {
    // No specific cleanup is necessary for this implementation.
    // ---------------------------------------------------------
  }

  /**
   * Sets the value (allele) of this Gene to a random String according to the
   * valid alphabet and boundaries of length
   *
   * @param a_numberGenerator The random number generator that should be
   *                          used to create any random values. It's important
   *                          to use this generator to maintain the user's
   *                          flexibility to configure the genetic engine
   *                          to use the random number generator of their
   *                          choice.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void setToRandomValue(RandomGenerator a_numberGenerator) {
    if (m_alphabet == null || m_alphabet.length() < 1) {
      throw new IllegalStateException("The valid alphabet is empty!");
    }
    if (m_maxLength < m_minLength || m_maxLength < 1) {
      throw new IllegalStateException(
          "Illegal valid maximum and/or minimum "
          + "length of alphabet!");
    }
    //randomize length of string
    //--------------------------
    int length;
    char value;
    int index;
    length = m_maxLength - m_minLength + 1;
    int i = a_numberGenerator.nextInt() % length;
    if (i < 0) {
      i = -i;
    }
    length = m_minLength + i;
    //for each character: randomize character value (which can be represented
    //by an integer value)
    //-----------------------------------------------------------------------
    m_value = "";
    final int alphabetLength = m_alphabet.length();
    for (int j = 0; j < length; j++) {
      index = a_numberGenerator.nextInt(alphabetLength);
      value = m_alphabet.charAt(index);
      m_value += value;
    }
  }

  /**
   * Sets the value and internal state of this Gene from the string
   * representation returned by a previous invocation of the
   * getPersistentRepresentation() method. This is an optional method but,
   * if not implemented, XML persistence and possibly other features will not
   * be available. An UnsupportedOperationException should be thrown if no
   * implementation is provided.
   *
   * @param a_representation the string representation retrieved from a
   *                         prior call to the getPersistentRepresentation()
   *                         method.
   *
   * @throws UnsupportedOperationException to indicate that no implementation
   *         is provided for this method.
   * @throws UnsupportedRepresentationException if this Gene implementation
   *         does not support the given string representation.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void setValueFromPersistentRepresentation(String a_representation)
      throws
      UnsupportedRepresentationException {
    if (a_representation != null) {
      StringTokenizer tokenizer =
          new StringTokenizer(a_representation,
                              PERSISTENT_FIELD_DELIMITER);
      // Make sure the representation contains the correct number of
      // fields. If not, throw an exception.
      // -----------------------------------------------------------
      if (tokenizer.countTokens() != 4) {
        throw new UnsupportedRepresentationException(
            "The format of the given persistent representation " +
            "is not recognized: it does not contain four tokens.");
      }
      String valueRepresentation = tokenizer.nextToken();
      String minLengthRepresentation = tokenizer.nextToken();
      String maxLengthRepresentation = tokenizer.nextToken();
      String alphabetRepresentation = tokenizer.nextToken();
      // Now parse and set the minimum length.
      // -------------------------------------
      try {
        m_minLength = Integer.parseInt(minLengthRepresentation);
      }
      catch (NumberFormatException e) {
        throw new UnsupportedRepresentationException(
            "The format of the given persistent representation " +
            "is not recognized: field 2 does not appear to be " +
            "an integer value.");
      }
      // Now parse and set the maximum length.
      // -------------------------------------
      try {
        m_maxLength = Integer.parseInt(maxLengthRepresentation);
      }
      catch (NumberFormatException e) {
        throw new UnsupportedRepresentationException(
            "The format of the given persistent representation " +
            "is not recognized: field 3 does not appear to be " +
            "an integer value.");
      }
      String tempValue;
      // Parse and set the representation of the value.
      // ----------------------------------------------
      if (valueRepresentation.equals("null")) {
        tempValue = null;
      }
      else {
        if (valueRepresentation.equals( ("\"\""))) {
          tempValue = "";
        }
        else {
          tempValue = valueRepresentation;
        }
      }
      //check if minLength and maxLength are violated
      //---------------------------------------------
      if (tempValue != null) {
        if (m_minLength > tempValue.length()) {
          throw new UnsupportedRepresentationException(
              "The value given"
              + " is shorter than the allowed maximum length.");
        }
        if (m_maxLength < tempValue.length()) {
          throw new UnsupportedRepresentationException(
              "The value given"
              + " is longer than the allowed maximum length.");
        }
      }
      //check if all characters are within the alphabet
      //-----------------------------------------------
      if (!isValidAlphabet(tempValue, alphabetRepresentation)) {
        throw new UnsupportedRepresentationException("The value given"
            + " contains invalid characters.");
      }
      m_value = tempValue;
      // Now set the alphabet that should be valid
      // -----------------------------------------
      m_alphabet = alphabetRepresentation;
    }
  }

  /**
   * Retrieves a string representation of this Gene that includes any
   * information required to reconstruct it at a later time, such as its
   * value and internal state. This string will be used to represent this
   * Gene in XML persistence. This is an optional method but, if not
   * implemented, XML persistence and possibly other features will not be
   * available. An UnsupportedOperationException should be thrown if no
   * implementation is provided.
   *
   * @return A string representation of this Gene's current state.
   * @throws UnsupportedOperationException to indicate that no implementation
   *         is provided for this method.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public String getPersistentRepresentation()
      throws
      UnsupportedOperationException {
    // The persistent representation includes the value, minimum length,
    // maximum length and valid alphabet. Each is separated by a colon.
    // ----------------------------------------------------------------
    return toString() + PERSISTENT_FIELD_DELIMITER + m_minLength +
        PERSISTENT_FIELD_DELIMITER + m_maxLength +
        PERSISTENT_FIELD_DELIMITER + m_alphabet;
  }

  /**
   * Retrieves the value (allele) represented by this Gene. All values
   * returned by this class will be String instances.
   *
   * @return the String value of this Gene.
   *
   * @since 1.1
   */
  public Object getAllele() {
    return m_value;
  }

  /**
   * Sets the value (allele) of this Gene to the new given value. This class
   * expects the value to be a String instance. If the value is shorter or
   * longer than the minimum or maximum length or any character is not within
   * the valid alphabet an exception is throwsn
   *
   * @param a_newValue the new value of this Gene instance.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void setAllele(Object a_newValue) {
    if (a_newValue != null) {
      String temp = (String) a_newValue;
      if (temp.length() < m_minLength ||
          temp.length() > m_maxLength) {
        throw new IllegalArgumentException(
            "The given value is too short or too long!");
      }
      //check for validity of alphabet
      //------------------------------
      if (!isValidAlphabet(temp, m_alphabet)) {
        throw new IllegalArgumentException("The given value contains"
                                           + " at least one invalid character.");
      }
      if (m_geneAlleleChecker != null) {
        if (!m_geneAlleleChecker.verify(this, a_newValue)) {
          return;
        }
      }
      m_value = temp;
    }
    else {
      m_value = null;
    }
  }

  /**
   * Sets the constraint checker to be used for this gene whenever method
   * setAllele(Object a_newValue) is called
   * @param a_constraintChecker the constraint checker to be set
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void setConstraintChecker(IGeneConstraintChecker a_constraintChecker) {
    m_geneAlleleChecker = a_constraintChecker;
  }

  /**
   * @return IGeneConstraintChecker the constraint checker to be used whenever
   * method setAllele(Object a_newValue) is called
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public IGeneConstraintChecker getConstraintChecker() {
    return m_geneAlleleChecker;
  }

  /**
   * Provides an implementation-independent means for creating new Gene
   * instances. The new instance that is created and returned should be
   * setup with any implementation-dependent configuration that this Gene
   * instance is setup with (aside from the actual value, of course). For
   * example, if this Gene were setup with bounds on its value, then the
   * Gene instance returned from this method should also be setup with
   * those same bounds. This is important, as the JGAP core will invoke this
   * method on each Gene in the sample Chromosome in order to create each
   * new Gene in the same respective gene position for a new Chromosome.
   * <p>
   * It should be noted that nothing is guaranteed about the actual value
   * of the returned Gene and it should therefore be considered to be
   * undefined.
   *
   * @param a_activeConfiguration The current active configuration.
   * @return A new Gene instance of the same type and with the same
   *         setup as this concrete Gene.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public Gene newGene(Configuration a_activeConfiguration) {
    if (a_activeConfiguration == null) {
      return new StringGene(m_minLength, m_maxLength, m_alphabet);
    }
    else {
      return new StringGene(m_minLength, m_maxLength, m_alphabet, a_activeConfiguration);
    }
  }

  /**
   * Compares this StringGene with the specified object (which must also
   * be a StringGene) for order, which is determined by the String
   * value of this Gene compared to the one provided for comparison.
   *
   * @param  other the StringGene to be compared to this StringGene.
   * @return a negative int, zero, or a positive int as this object
   *	       is less than, equal to, or greater than the object provided for
   *         comparison.
   *
   * @throws ClassCastException if the specified object's type prevents it
   *         from being compared to this StringGene.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public int compareTo(Object other) {
    StringGene otherStringGene = (StringGene) other;
    // First, if the other gene (or its value) is null, then this is
    // the greater allele. Otherwise, just use the String's compareTo
    // method to perform the comparison.
    // ---------------------------------------------------------------
    if (otherStringGene == null) {
      return 1;
    }
    else if (otherStringGene.m_value == null) {
      // If our value is also null, then we're the same. Otherwise,
      // this is the greater gene.
      // ----------------------------------------------------------
      return m_value == null ? 0 : 1;
    }
    else {
      try {
        return m_value.compareTo(otherStringGene.m_value);
      }
      catch (ClassCastException e) {
        e.printStackTrace();
        throw e;
      }
    }
  }

  public int size() {
    return m_value.length();
  }

  public int getMaxLength() {
    return m_maxLength;
  }

  public int getMinLength() {
    return m_minLength;
  }

  public void setMinLength(int m_minLength) {
    this.m_minLength = m_minLength;
  }

  public void setMaxLength(int m_maxLength) {
    this.m_maxLength = m_maxLength;
  }

  public String getAlphabet() {
    return m_alphabet;
  }

  /**
   * Sets the valid alphabet of the StringGene
   * @param a_alphabet valid aplhabet for allele
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void setAlphabet(String a_alphabet) {
    //check if a substring is equal to the PERSISTENT_FIELD_DELIMITER
    //which is not allowed currently
    //---------------------------------------------------------------
    if (containsString(a_alphabet, PERSISTENT_FIELD_DELIMITER)) {
      throw new IllegalArgumentException(
          "The alphabet may not contain a "
          + "substring equal to the persistent field delimiter (which is "
          + PERSISTENT_FIELD_DELIMITER
          + " currently).");
    }
    /**@todo optionally check if alphabet contains doublettes*/
    this.m_alphabet = a_alphabet;
  }

  /**
   * Retrieves a string representation of this StringGene's value that
   * may be useful for display purposes.
   *
   * @return a string representation of this StringGene's value.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public String toString() {
    if (m_value == null) {
      return "null";
    }
    else {
      if (m_value.equals("")) {
        return "\"\"";
      }
      else {
        return m_value.toString();
      }
    }
  }

  /**
   * Compares this Gene with the given object and returns true if
   * the other object is a Gene of this type and has the same value (allele) as
   * this Gene. Otherwise it returns false.
   *
   * @param other the object to compare to this Gene for equality.
   * @return true if this Gene is equal to the given object,
   *         false otherwise.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public boolean equals(Object other) {
    try {
      return compareTo(other) == 0;
    }
    catch (ClassCastException e) {
      // If the other object isn't a StringGene, then we're not equal.
      // -------------------------------------------------------------
      return false;
    }
  }

  /**
   * Retrieves the hash code value for this StringGene.
   *
   * @return this StringGene's hash code.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public int hashCode() {
    // If our internal String is null, then return zero. Otherwise,
    // just return the hash code of the String.
    // -------------------------------------------------------------
    if (m_value == null) {
      return 0;
    }
    else {
      return m_value.hashCode();
    }
  }

  /**
   * Retrieves the String value of this Gene, which may be more convenient in
   * some cases than the more general getAllele() method.
   *
   * @return the String value of this Gene.
   *
   * @since 1.1
   */
  public String stringValue() {
    return (String) m_value;
  }

  /**
   * Checks whether a substring is contained within another string
   * @param totalString the total string to examine
   * @param subString the substring to look for
   * @return true: the totalString contains the subString
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  private boolean containsString(String totalString, String subString) {
    if (totalString == null || subString == null) {
      return false;
    }
    return totalString.indexOf(subString) >= 0;
  }

  /**
   * Checks whether a string value is valid concerning a given alphabet
   * @param a_value the value to check
   * @param a_alphabet the valid alphabet to check against
   * @return true: given string value is valid
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  private boolean isValidAlphabet(String a_value, String a_alphabet) {
    if (a_value == null || a_value.length() < 1) {
      return true;
    }
    if (a_alphabet == null) {
      return true;
    }
    if (a_alphabet.length() < 1) {
      return false;
    }
    //loop over all characters of a_value
    //-----------------------------------
    int length = a_value.length();
    char c;
    for (int i = 0; i < length; i++) {
      c = a_value.charAt(i);
      if (a_alphabet.indexOf(c) < 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * Applies a mutation of a given intensity (percentage) onto the atomic
   * element at given index (NumberGenes only have one atomic element)
   * @param index index of atomic element, between 0 and size()-1
   * @param a_percentage percentage of mutation (greater than -1 and smaller
   *        than 1).
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void applyMutation(int index, double a_percentage) {
    String s = stringValue();
    int index2 = -1;
    boolean randomize;
    int len = 0;
    if (m_alphabet != null) {
      len = m_alphabet.length();
      if (len < 1) {
        randomize = true;
      }
      else {
        randomize = false;
      }
    }
    else {
      randomize = true;
    }
    char newValue;
    if (!randomize) {
      int indexC = m_alphabet.indexOf(s.charAt(index));
      index2 = indexC + (int) Math.round(len * a_percentage);
      // If index of new character out of bounds then randomly choose a new
      // character. This randomness helps in the process of evolution
      // ------------------------------------------------------------------
      if (index2 < 0 || index2 >= len) {
        index2 = m_configuration.getRandomGenerator().nextInt(len);
      }
      newValue = m_alphabet.charAt(index2);
    }
    else {
      index2 = m_configuration.getRandomGenerator().nextInt(256);
      newValue = (char)index2;
    }
    // Set mutated character by concatenating the String with it
    // ---------------------------------------------------------------
    if (s == null) {
      s = ""+newValue;
    }
    else {
      s = s.substring(0, index) + newValue + s.substring(index + 1);
    }
    setAllele(s);
  }

}
