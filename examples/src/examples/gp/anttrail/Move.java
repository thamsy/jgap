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
 * Move the ant.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class Move
    extends AntCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public Move(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf);
  }

  public void execute_void(ProgramChromosome a_ind, int a_n, Object[] a_args) {
    AntMap map = getMap();
    int x = map.getPosX();
    int y = map.getPosX();
    int orient = map.getOrientation();
    switch (orient) {
      case AntMap.O_DOWN:
        y++;
        if (y >= map.getHeight() ) {
          throw new IllegalStateException("y bigger than height");
        }
        map.setPosY(y);
        break;
      case AntMap.O_LEFT:
        x--;
        if (x < 0 ) {
          throw new IllegalStateException("x smaller zero");
        }
        map.setPosX(x);
        break;
      case AntMap.O_RIGHT:
        x++;
        if (x >= map.getWidth() ) {
          throw new IllegalStateException("x bigger than width");
        }
        map.setPosX(x);
        break;
      case AntMap.O_UP:
        y--;
        if (y < 0 ) {
          throw new IllegalStateException("y smaller zero");
        }
        map.setPosY(y);
        break;
    }
    map.IncrementMoveCounter();
  }

  public String toString() {
    return "move";
  }
}
