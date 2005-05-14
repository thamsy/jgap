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
import java.io.*;

/**
 * This is a Singleton Helper class to read a JGAP config file and provide a
 * simple interface to the config properties.
 *
 * @author Siddhartha Azad
 * @since 2.3
 */
public class ConfigFileReader {
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  // Name of the config file to read
  private String m_fileName;

  // Properties read from the config file
  private Properties m_props;

  // namespace of the property
  private String m_ns;

  /**
   * Singleton Instance of ConfigFileReader
   */
  private static ConfigFileReader cfReader;

  /**
   * Method to create and access the Singleton ConfigFileReader instance.
   * @return instance of the ConfigFileReader
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public static ConfigFileReader instance() {
    if (cfReader == null) {
      cfReader = new ConfigFileReader();
    }
    return cfReader;
  }

  /**
   * Private Constructor.@param _fileName Name of the config file.
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  private ConfigFileReader() {
    m_props = new Properties();
  }

  /**
   * Retrieve the value for the property with the name as in param name.
   * @author Siddhartha Azad.
   * @param a_name Name of the property of which the value is required.
   * @return value for the property with the name as in param name, null if
   * property not found.
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public String getValue(String a_name) {
    String tmpName = m_ns + "." + a_name;
    String val = m_props.getProperty(tmpName);
    return val;
  }

  /**
   * Retrieve the values for the property with the name as in param name.
   * @author Siddhartha Azad.
   * @param name Name of the property of which the value is required.
   * @return ArrayList of Strings with values for the property with the name as
   * in param name, null if property not found.
   * */
  public ArrayList getValues(String name) {
    String val = "";
    boolean done = false;
    String tmpName = "";
    int idx = 0;
    ArrayList values = new ArrayList();
    while (!done) {
      tmpName = m_ns + "." + name + "[" + idx + "]";
      val = m_props.getProperty(tmpName);
      if (val == null) {
        done = true;
      }
      else {
        values.add(val);
        idx++;
      }
    }
    if (idx == 0) {
      return null;
    }
    else {
      return values;
    }
  }

  /**
   * Set the namespace for the properties that are being read from the
   * config file at this point.
   * @author Siddhartha Azad.
   * @param a_ns namespace for the properties being read.
   * */
  public void setNS(String a_ns) {
    m_ns = a_ns;
  }

  /**
   * Set the config file to load from. Everytime this method is called,
   * properties are reloaded from the config file.
   * @author Siddhartha Azad.
   * @param a_fileName Name of the config file.
   * @throws ConfigException
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public void setFileName(String a_fileName)
      throws ConfigException {
    m_fileName = a_fileName;
    load();
  }

  /**
   * Load the config properties file into a Properties instance.
   * @throws ConfigException
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  private void load()
      throws ConfigException {
    try {
      m_props.load(new FileInputStream(m_fileName));
    }
    catch (Exception ex) {
      String dir = new File(".").getAbsolutePath();
      throw new ConfigException("Error reading Config file " + m_fileName
                                + " in directory " + dir);
    }
  }
}
