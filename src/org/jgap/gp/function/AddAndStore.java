/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.function;

import org.jgap.*;
import org.jgap.gp.*;

/**
 * The add operation that stores the result in internal memory afterwards.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class AddAndStore
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  /**
   * Symbolic name of the storage. Must correspond with a chosen name for
   * ReadTerminalCommand.
   */
  private String m_storageName;

  private Class m_type;

  public AddAndStore(final Configuration a_conf, Class a_type,
                     String a_storageName)
      throws InvalidConfigurationException {
    super(a_conf, 2, CommandGene.VoidClass);
    m_type = a_type;
    m_storageName = a_storageName;
  }

  protected Gene newGeneInternal() {
    try {
      Gene gene = new AddAndStore(getConfiguration(), m_type, m_storageName);
      return gene;
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public String toString() {
    return "Store(" + m_storageName + ", &1 + &2)";
  }

  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    Object value = null;
    if (m_type == CommandGene.IntegerClass) {
      value = new Integer(c.execute_int(n, 0, args) + c.execute_int(n, 1, args));
    }
    else if (m_type == CommandGene.LongClass) {
      value = new Long(c.execute_long(n, 0, args) + c.execute_long(n, 1, args));
    }
    else if (m_type == CommandGene.DoubleClass) {
      value = new Double(c.execute_double(n, 0, args) +
                         c.execute_double(n, 1, args));
    }
    else if (m_type == CommandGene.FloatClass) {
      value = new Float(c.execute_float(n, 0, args) +
                        c.execute_float(n, 1, args));
    }
    else {
      throw new RuntimeException("Type " + m_type +
                                 " not supported by AddAndStore");
    }
    ( (GPConfiguration) getConfiguration()).storeInMemory(m_storageName, value);
  }

  public Class getChildType(int i) {
    return m_type;
  }
}