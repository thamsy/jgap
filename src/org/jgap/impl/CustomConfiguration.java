/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import org.jgap.*;

/**
 * The CustomConfiguration is the configuration instance that can read a JGAP
 * configuration file and load itself.
 */
public class CustomConfiguration
    extends Configuration {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private String m_conFile;

  /**
   * Constructor.
   * @param _conFile The config file from which to load the configuration.
   * @throws ConfigException
   * @throws InvalidConfigurationException
   *
   * @author Siddhartha Azad.
   * @since 2.3
   */
  public CustomConfiguration(String _conFile) throws ConfigException,
  	InvalidConfigurationException {
    super();
    m_conFile = _conFile;
    ConfigFileReader.instance().setFileName(_conFile);
    getConfigurationHandler().readConfig();
  }
}
