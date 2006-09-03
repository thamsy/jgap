/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.anttrail;

import org.jgap.gp.impl.*;
import org.jgap.*;

/**
 * Turn the ant right.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class Right
    extends AntCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public Right(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf);
  }

  public void execute_void(ProgramChromosome a_chrom, int a_n, Object[] a_args) {
    AntMap map = getMap(a_chrom);
    int orient = map.getOrientation();
    switch (orient) {
      case AntMap.O_DOWN:
        orient = AntMap.O_LEFT;
        break;
      case AntMap.O_LEFT:
        orient = AntMap.O_UP;
        break;
      case AntMap.O_RIGHT:
        orient = AntMap.O_DOWN;
        break;
      case AntMap.O_UP:
        orient = AntMap.O_RIGHT;
        break;
    }
    map.setOrientation(orient);
    map.IncrementMoveCounter();
  }

  public String toString() {
    return "right";
  }
}
