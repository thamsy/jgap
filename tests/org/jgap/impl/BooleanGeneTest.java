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

package org.jgap.impl;

import org.jgap.*;
import junit.framework.*;

/**
 * Test class for BooleanGene class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class BooleanGeneTest
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  public BooleanGeneTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(BooleanGeneTest.class);
    return suite;
  }

  public void testConstruct_0() {
    Gene gene = new BooleanGene();
    //following should be possible without exception
    gene.setAllele(new Boolean(true));
    gene.setAllele(new Boolean(false));
  }

  public void testToString_0() {
    Gene gene = new BooleanGene();
    gene.setAllele(new Boolean(true));
    assertEquals("true", gene.toString());
  }

  public void testToString_1() {
    Gene gene = new BooleanGene();
    gene.setAllele(new Boolean(false));
    assertEquals("false", gene.toString());
  }

  public void testGetAllele_0() {
    Gene gene = new BooleanGene();
    gene.setAllele(new Boolean(true));
    assertEquals(new Boolean(true), gene.getAllele());
  }

  public void testGetAllele_1() {
    Gene gene = new BooleanGene();
    gene.setAllele(new Boolean(false));
    assertEquals(new Boolean(false), gene.getAllele());
  }

  public void testGetAllele_2() {
    Gene gene = new BooleanGene();
    try {
      gene.setAllele(new Integer(100));
      fail();
    }
    catch (ClassCastException classex) {
      ; //this is OK
    }
  }

  public void testEquals_0() {
    Gene gene1 = new BooleanGene();
    Gene gene2 = new BooleanGene();
    assertTrue(gene1.equals(gene2));
  }

  public void testEquals_1() {
    Gene gene1 = new BooleanGene();
    assertFalse(gene1.equals(null));
  }

  public void testEquals_2() {
    Gene gene1 = new BooleanGene();
    gene1.setAllele(new Boolean(true));
    Gene gene2 = new BooleanGene();
    gene2.setAllele(new Boolean(false));
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  public void testEquals_3() {
    Gene gene1 = new BooleanGene();
    assertFalse(gene1.equals(new IntegerGene()));
  }

  public void testBooleanValue_0() {
    BooleanGene gene1 = new BooleanGene();
    gene1.setAllele(new Boolean(true));
    assertEquals(true, gene1.booleanValue());
  }

  public void testBooleanValue_1() {
    BooleanGene gene1 = new BooleanGene();
    gene1.setAllele(new Boolean(false));
    assertEquals(false, gene1.booleanValue());
  }

  public void testBooleanValue_2() {
    BooleanGene gene1 = new BooleanGene();
    gene1.setAllele(null);
    try {
      assertEquals(true, gene1.booleanValue());
      fail();
    }
    catch (NullPointerException nullex) {
      ; //this is OK
    }
  }

  /**
   * Set Allele to null, no exception should occur
   */
  public void testSetAllele_0() {
    Gene gene1 = new BooleanGene();
    gene1.setAllele(null);
  }

  public void testSetAllele_1() {
    Gene gene1 = new BooleanGene();
    try {
      gene1.setAllele("22");
      fail();
    }
    catch (ClassCastException classex) {
      ; //this is OK
    }
  }

  /**
   * Set Allele to boolean value, no exception should occur
   */
  public void testSetAllele_2() {
    Gene gene1 = new BooleanGene();
    gene1.setAllele(new Boolean(true));
  }

  /**
   * Set Allele to boolean value, no exception should occur
   */
  public void testSetAllele_3() {
    Gene gene1 = new BooleanGene();
    gene1.setAllele(new Boolean(false));
  }

  public void testCompareTo_0() {
    Gene gene1 = new BooleanGene();
    assertEquals(1, gene1.compareTo(null));
  }

  public void testCompareTo_1() {
    Gene gene1 = new BooleanGene();
    Gene gene2 = new BooleanGene();
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
  }

  public void testCompareTo_2() {
    Gene gene1 = new BooleanGene();
    gene1.setAllele(new Boolean(true));
    Gene gene2 = new BooleanGene();
    gene2.setAllele(new Boolean(true));
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
    gene1.setAllele(new Boolean(false));
    gene2.setAllele(new Boolean(false));
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
  }

  public void testCompareTo_3() {
    Gene gene1 = new BooleanGene();
    gene1.setAllele(new Boolean(true));
    Gene gene2 = new BooleanGene();
    gene2.setAllele(new Boolean(false));
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals( -1, gene2.compareTo(gene1));
  }

  public void testApplyMutation_0() {
    BooleanGene gene = new BooleanGene();
    gene.setAllele(new Boolean(true));
    gene.applyMutation(0, 0.0d);
    assertEquals(true, gene.booleanValue());
  }

  public void testApplyMutation_1() {
    BooleanGene gene = new BooleanGene();
    gene.setAllele(new Boolean(true));
    gene.applyMutation(1, 0.000001d); //index 1 should be ignored
    assertEquals(true, gene.booleanValue());
  }

  public void testApplyMutation_2() {
    BooleanGene gene = new BooleanGene();
    gene.setAllele(new Boolean(true));
    gene.applyMutation(333, -0.000001d); //index 333 should be ignored
    assertEquals(false, gene.booleanValue());
  }

  public void testApplyMutation_3() {
    BooleanGene gene = new BooleanGene();
    gene.setAllele(new Boolean(true));
    gene.applyMutation(0, -1.0d);
    assertEquals(false, gene.booleanValue());
  }

  public void testApplyMutation_4() {
    BooleanGene gene = new BooleanGene();
    gene.setAllele(new Boolean(true));
    gene.applyMutation(0, -2.0d);
    assertEquals(false, gene.booleanValue());
  }

  public void testApplyMutation_5() {
    BooleanGene gene = new BooleanGene();
    gene.setAllele(new Boolean(true));
    gene.applyMutation(0, 2.0d);
    assertEquals(true, gene.booleanValue());
  }

  public void testApplyMutation_6() {
    BooleanGene gene = new BooleanGene();
    gene.setAllele(new Boolean(false));
    gene.applyMutation(0, 2.0d);
    assertEquals(true, gene.booleanValue());
  }

  public void testApplyMutation_7() {
    BooleanGene gene = new BooleanGene();
    gene.setAllele(new Boolean(false));
    gene.applyMutation(0, -1.0d);
    assertEquals(false, gene.booleanValue());
  }

  public void testApplyMutation_8() {
    BooleanGene gene = new BooleanGene();
    gene.setAllele(new Boolean(false));
    gene.applyMutation(22, -0.5d);//22 should be ignored
    assertEquals(false, gene.booleanValue());
  }

  public void testApplyMutation_9() {
    BooleanGene gene = new BooleanGene();
    gene.setAllele(new Boolean(false));
    gene.applyMutation(22, 0.5d);//22 should be ignored
    assertEquals(true, gene.booleanValue());
  }

  /**
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_0() {
    BooleanGene gene = new BooleanGene();
    try {
      gene.setValueFromPersistentRepresentation(null);
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_1()
      throws Exception {
    BooleanGene gene = new BooleanGene();
    gene.setValueFromPersistentRepresentation("null");
    assertEquals(null, gene.getAllele());
  }

  /**
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_2()
      throws Exception {
    BooleanGene gene = new BooleanGene();
    gene.setValueFromPersistentRepresentation("true");
    assertEquals(Boolean.TRUE, gene.getAllele());
  }

  /**
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_3()
      throws Exception {
    BooleanGene gene = new BooleanGene();
    gene.setValueFromPersistentRepresentation("false");
    assertEquals(Boolean.FALSE, gene.getAllele());
  }

  /**
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_4() {
    BooleanGene gene = new BooleanGene();
    try {
      gene.setValueFromPersistentRepresentation("True");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_5() {
    BooleanGene gene = new BooleanGene();
    try {
      gene.setValueFromPersistentRepresentation("False");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_6() {
    BooleanGene gene = new BooleanGene();
    try {
      gene.setValueFromPersistentRepresentation("X");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  public void testGetPersistentRepresentation_0() {
     BooleanGene gene = new BooleanGene();
     gene.setAllele(new Boolean(true));
     String s = gene.getPersistentRepresentation();
     assertEquals("true",s);
  }

  public void testGetPersistentRepresentation_1() {
     BooleanGene gene = new BooleanGene();
     gene.setAllele(new Boolean(false));
     String s = gene.getPersistentRepresentation();
     assertEquals("false",s);
  }

  public void testGetPersistentRepresentation_2() {
     BooleanGene gene = new BooleanGene();
     String s = gene.getPersistentRepresentation();
     assertEquals("null",s);
  }

}
