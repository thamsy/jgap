/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp;

import org.jgap.*;
import org.jgap.gp.*;

/**
 * The for-loop loop from 0 to X-1.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class ForXCommand
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private Class m_type;

  public ForXCommand(final Configuration a_conf, Class a_type)
      throws InvalidConfigurationException {
    super(a_conf, 1, CommandGene.VoidClass);
    m_type = a_type;
  }

  protected Gene newGeneInternal() {
    return null; /**@todo implement if necessary*/
  }

  public String toString() {
    return "for(int i=0;i<X;i++) { &1 }";
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int index = c.getCommandOfClass(0, Variable.class);
    if (index < 0) {
      throw new IllegalStateException("Variable missing for forX");
    }
    Variable var = (Variable) c.getNode(index);
    int x = ( (Integer) var.getValue()).intValue();
    if (x > 15) {
      x = 15;
    }
    int value = 0;
    for (int i = 0; i < x; i++) {
      value = c.execute_int(n, 0, args);
    }
    return value;
  }

  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int index = c.getCommandOfClass(0, Variable.class);
    if (index < 0) {
      throw new IllegalStateException("Variable missing for forX");
    }
    Variable var = (Variable) c.getNode(index);
    int x = ( (Integer) var.getValue()).intValue();
    if (x > 15) {
      x = 15;
    }
    for (int i = 0; i < x; i++) {
      c.execute_void(n, 0, args);
    }
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int index = c.getCommandOfClass(0, Variable.class);
    if (index < 0) {
      throw new IllegalStateException("Variable missing for forX");
    }
    Variable var = (Variable) c.getNode(index);
    int x = ( (Integer) var.getValue()).intValue();
    if (x > 15) {
      x = 15;
    }
    Object value = null;
    for (int i = 0; i < x; i++) {
      value = c.execute(n, 0, args);
    }
    return value;
  }

  public static interface Compatible {
    public Object execute_forX(Object o);
  }
  public boolean isValid(ProgramChromosome a_program) {
    return true;
//    /**@todo check if elements deferring the state are available in the sub
//     * branch. If not, the sub branch needs only be executed once.
//     * Appropriate elements are, for example: PushCommand and PopCommand
//     */
//    return a_program.getCommandOfClass(0,Variable.class) >= 0;
  }

  public Class getChildType(int i) {
    return m_type;
  }
}
