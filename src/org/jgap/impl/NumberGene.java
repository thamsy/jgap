/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import org.jgap.*;

/**
 * Base class for all Genes based on numbers.<br>
 *
 * @author Klaus Meffert
 * @since 1.1 (most code moved and adapted from IntegerGene)
 */
public abstract class NumberGene extends BaseGene
    implements Gene {

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.10 $";

  /**
   * References the internal value (allele) of this Gene
   * E.g., for DoubleGene this is of type Double
   */
  protected Object m_value = null;

  /**
   * Optional helper class for checking if a given allele value to be set
   * is valid. If not the allele value may not be set for the gene!
   */
  private IGeneConstraintChecker m_geneAlleleChecker;

  /**
   * Compares this NumberGene with the specified object (which must also
   * be a NumberGene) for order, which is determined by the number
   * value of this Gene compared to the one provided for comparison.
   *
   * @param  other the NumberGene to be compared to this NumberGene.
   * @return a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the object provided for comparison.
   *
   * @throws ClassCastException if the specified object's type prevents it
   *         from being compared to this NumberGene.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public int compareTo(Object other) {
    NumberGene otherGene = (NumberGene) other;
    // First, if the other gene (or its value) is null, then this is
    // the greater allele. Otherwise, just use the overridden compareToNative
    // method to perform the comparison.
    // ---------------------------------------------------------------
    if (otherGene == null) {
      return 1;
    }
    else if (otherGene.m_value == null) {
      // check if type corresponds (because we could have a type not inherited
      // from NumberGene)
      if (!otherGene.getClass().equals(this.getClass())) {
        throw new ClassCastException(
            "Comparison not possible: different types!");
      }
      // If our value is also null, then we're the same. Otherwise,
      // this is the greater gene.
      // ----------------------------------------------------------
      return m_value == null ? 0 : 1;
    }
    else {
      try {
        return compareToNative(m_value, otherGene.m_value);
      }
      catch (ClassCastException e) {
        throw e;
      }
    }
  }

  /**
   * Compares to objects by first casting them into their expected type
   * (e.g. Integer for IntegerGene) and then calling the compareTo-method
   * of the casted type.
   * @param o1 first object to be compared, always is not null
   * @param o2 second object to be compared, always is not null
   * @return a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the object provided for comparison.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  protected abstract int compareToNative(Object o1, Object o2);

  /**
   * Sets the value (allele) of this Gene to the new given value. This class
   * expects the value to be an instance of current type (e.g. Integer).
   * If the value is above or below the upper or lower bounds, it will be
   * mappped to within the allowable range.
   *
   * @param a_newValue the new value of this Gene instance.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void setAllele(Object a_newValue) {
    if (m_geneAlleleChecker != null) {
      if (!m_geneAlleleChecker.verify(this, a_newValue)) {
        return;
      }
    }
    m_value = a_newValue;
    // If the value isn't between the upper and lower bounds of this
    // Gene, map it to a value within those bounds.
    // -------------------------------------------------------------
    mapValueToWithinBounds();
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
   * Maps the value of this NumberGene to within the bounds specified by
   * the m_upperBounds and m_lowerBounds instance variables. The value's
   * relative position within the integer range will be preserved within the
   * bounds range (in other words, if the value is about halfway between the
   * integer max and min, then the resulting value will be about halfway
   * between the upper bounds and lower bounds). If the value is null or
   * is already within the bounds, it will be left unchanged.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  protected abstract void mapValueToWithinBounds();

  protected Object getInternalValue() {
    return m_value;
  }
}
