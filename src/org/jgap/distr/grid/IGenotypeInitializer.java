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

import org.jgap.*;

/**
 * Interface specifying how to initialize a Genotype on behalf of the worker.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public interface IGenotypeInitializer {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.1 $";

  Genotype setupGenotype(JGAPRequest a_req, Population a_initialPop);
}
