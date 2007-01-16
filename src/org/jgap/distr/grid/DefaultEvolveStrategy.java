/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid;

import org.jgap.Genotype;

/**
 * Default and simple implementation of IEvolveStrategy.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class DefaultEvolveStrategy implements IEvolveStrategy {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public void evolve(Genotype a_genotype) {
    a_genotype.evolve();
  }
}
