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

import java.util.*;

/**
 * Interface for central factory, see JGAPFactory.
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public interface IJGAPFactory {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.4 $";

  void setParameters(Collection a_parameters);

  RandomGenerator createRandomGenerator();

  /**
   * Retrieves a clone handler capable of clone the given class
   * @param a_classToClone the class to clone an object of
   * @return ICloneHandler the clone handler found capable of clone the given
   * class, or null if none registered
   *
   * @author Klaus Meffert
   * @since 2.6
   */

  ICloneHandler getCloneHandlerFor(Object a_obj, Class a_classToClone);

  /**
   * Registers a clone handler that could be retrieved by
   * getCloneHandlerFor(Class)
   * @param a_cloneHandler the ICloneHandler to register
   * @return index of the added clone handler, needed when removeCloneHandler
   * will be called
   *
   * @author Klaus Meffert
   * @since 2.6
   */

  int registerCloneHandler(ICloneHandler a_cloneHandler);

  /**
   * Retrieves an initializer capable of initializing the Object of the given
   * class
   * @param a_objToInit the object class to init
   * @return  the initializer found capable of initializing an object of the
   * given class, or null if none registered
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  IInitializer getInitializerFor(Object a_obj, Class a_objToInit);

  /**
   * Registers an initializer that could be retrieved by getInitializerFor(Class)
   * @param a_chromIniter the IChromosomeInitializer to register
   * @return index of the added initializer, needed when
   * removeChromosomeInitializer will be called
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  int registerInitializer(IInitializer a_chromIniter);

  void setGeneticOperatorConstraint(IGeneticOperatorConstraint
                                    a_constraint);

  IGeneticOperatorConstraint getGeneticOperatorConstraint();
}
