/*
 * This file is part of JGAP.
 *
 * JGAP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * JGAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with JGAP; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.jgap.xml;

/**
 * An ImproperXMLException will be thrown when an XML document or element is
 * parsed but is found to be structured improperly or missing required data.
 * The error message should be consulted for the exact reason the exception
 * is being thrown.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class ImproperXMLException
    extends Exception {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  /**
   * Constructs a new ImproperXMLException instance with the given error
   * message.
   *
   * @param a_message An error message describing the reason this exception
   *                  is being thrown.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public ImproperXMLException(String a_message) {
    super(a_message);
  }
}
