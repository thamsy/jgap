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

import java.io.*;

/**
 * Interface for Chromosomes.
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public interface IChromosome
    extends Comparable, Cloneable, Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.6 $";

  /**
   * Constants for toString()
   */
  public final static String S_FITNESS_VALUE = "Fitness value";

  public final static String S_ALLELES = "Alleles";

  public final static String S_APPLICATION_DATA = "Application data";

  public final static String S_SIZE = "Size";

  Gene getGene(int a_desiredLocus);

  Gene[] getGenes();

  void setGenes(Gene[] a_genes)
      throws InvalidConfigurationException;

  int size();

  void setFitnessValue(double a_newFitnessValue);

  void setFitnessValueDirectly(double a_newFitnessValue);

  double getFitnessValue();

  double getFitnessValueDirectly();

  void setIsSelectedForNextGeneration(boolean a_isSelected);

  boolean isSelectedForNextGeneration();

  Object clone();

  void setConstraintChecker(IGeneConstraintChecker a_constraintChecker)
      throws InvalidConfigurationException;

  void setApplicationData(Object a_newData);

  Object getApplicationData();

  void cleanup();
}