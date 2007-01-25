/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.util;

/**
 * System-related utility functions.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class SystemKit {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  /**
   * @return total memory available by the VM in megabytes.
   *
   * @author Klaus Meffert
   * @since 3.2 (since 3.0 in GPGenotype)
   */
  public static double getTotalMemoryMB() {
    return (Runtime.getRuntime().totalMemory() / 1024 / 1024);
  }

  /**
   * @return free memory available in the VM in megabytes.
   *
   * @author Klaus Meffert
   * @since 3.0 (since 3.0 in GPGenotype)
   */
  public static double getFreeMemoryMB() {
    return (Runtime.getRuntime().freeMemory() / 1024 / 1024);
  }
}