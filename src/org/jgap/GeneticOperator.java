/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import java.util.List;

/**
 * A GeneticOperator represents an operation that takes place on
 * a population of Chromosomes during the evolution process. Examples
 * of genetic operators include reproduction, crossover, and mutation.
 * This interface contains only one method--operate()--which is responsible
 * for performing the genetic operation on the current population of
 * Chromosomes.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public interface GeneticOperator
    extends java.io.Serializable {

  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.9 $";

  /**
   * The operate method will be invoked on each of the genetic operators
   * referenced by the current Configuration object during the evolution
   * phase. Operators are given an opportunity to run in the order that
   * they are added to the Configuration. Implementations of this method
   * may reference the population of Chromosomes as it was at the beginning
   * of the evolutionary phase and/or they may instead reference the
   * candidate Chromosomes, which are the results of prior genetic operators.
   * In either case, only Chromosomes added to the list of candidate
   * chromosomes will be considered for natural selection. Implementations
   * should never modify the original population, but should first make copies
   * of the Chromosomes selected for modification and operate upon the copies.
   *
   * @param a_population The population of chromosomes from the current
   *                     evolution prior to exposure to any genetic operators.
   *                     Chromosomes in this array should never be modified.
   * @param a_candidateChromosomes The pool of chromosomes that have been selected
   *                               for the next evolved population.
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.0 (earlier versions referenced the Configuration object)
   */
  public void operate(final Population a_population,
                      final List a_candidateChromosomes);
}
