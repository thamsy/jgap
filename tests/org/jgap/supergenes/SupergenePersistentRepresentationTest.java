/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.supergenes;

import org.jgap.*;
import org.jgap.impl.*;

import junit.framework.*;

/**
 * Test persistent representation of the abstractSupergene.
 *
 * @author Meskauskas Audrius
 * @since 2.0
 */
public class SupergenePersistentRepresentationTest extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";


    public static class instantiableSupergene extends abstractSupergene
     {
         public boolean isValid(Gene [] a) { return true; };
     }

    public static boolean testRepresentation()
     {
        try {
            instantiableSupergene gene = new instantiableSupergene ();

            Gene i1 = new IntegerGene (1, 12);
            Gene i2 = new DoubleGene  (3, 4);

            i1.setAllele(new Integer(7));
            i2.setAllele(new Double(3.2));

            gene.addGene (i1);
            gene.addGene (i2);

            instantiableSupergene nested = new instantiableSupergene();

            Gene n1 = new IntegerGene (1, 12);
            Gene n2 = new DoubleGene  (3, 4);

            n1.setAllele(new Integer(5));
            n2.setAllele(new Double(3.6));

            nested.addGene(n1);
            nested.addGene(n2);

            gene.addGene(nested);

            instantiableSupergene nested2 = new instantiableSupergene();
            nested2.setValidator(new Validator()
             {
                 public boolean isValid(Gene [] g, Supergene s)
                  {
                      return true;
                  }
             });

            Gene nn1 = new IntegerGene (1, 1000);
            Gene nn2 = new DoubleGene  (0, 1000);

            nn1.setAllele(new Integer(22));
            nn2.setAllele(new Double(44));

            nested2.addGene(nn1);
            nested2.addGene(nn2);

            gene.addGene(nested2);

            instantiableSupergene nested3 = new instantiableSupergene();
            nested3.setValidator(null);

            Gene nnn1 = new IntegerGene (1, 1000);
            Gene nnn2 = new DoubleGene  (0, 1000);

            nnn1.setAllele(new Integer(555));
            nnn2.setAllele(new Double(777));

            nested3.addGene(nnn1);
            nested3.addGene(nnn2);

            nested2.addGene(nested3);

            String representation =
                gene.getPersistentRepresentation ();

//            System.out.println ("Old representation: " + representation);

            instantiableSupergene restored = new instantiableSupergene ();
            restored.setValueFromPersistentRepresentation (representation);

//            System.out.println ("New representation: " +
//                                restored.getPersistentRepresentation ());

//            System.out.println("Old gene "+gene);
//            System.out.println("New gene "+restored);

            return gene.equals (restored);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
     };

     public static Test suite() {
       TestSuite suite =
        new TestSuite(SupergenePersistentRepresentationTest.class);
       return suite;
     }

     protected void setUp() throws Exception {
         super.setUp();
     }

     protected void tearDown() throws Exception {
         super.tearDown();
     }

     public void testRunTest() {
         boolean expectedReturn = true;
         boolean actualReturn = testRepresentation();
         assertEquals("return value", expectedReturn, actualReturn);
     }

}
