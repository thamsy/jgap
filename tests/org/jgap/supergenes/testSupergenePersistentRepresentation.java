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
package org.jgap.supergenes;

import org.jgap.*;
import org.jgap.Gene;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.DoubleGene;
import org.jgap.impl.CompositeGene;

/** Test persistent representation of the abstractSupergene. */
public class testSupergenePersistentRepresentation {

    public static class instantiableSupergene extends abstractSupergene
     {
         public boolean isValid() { return true; };
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

            Gene nn1 = new IntegerGene (1, 1000);
            Gene nn2 = new DoubleGene  (0, 1000);

            nn1.setAllele(new Integer(22));
            nn2.setAllele(new Double(44));

            nested2.addGene(nn1);
            nested2.addGene(nn2);

            gene.addGene(nested2);

            CompositeGene nested3 = new CompositeGene();

            Gene nnn1 = new IntegerGene (1, 1000);
            Gene nnn2 = new DoubleGene  (0, 1000);

            nnn1.setAllele(new Integer(555));
            nnn2.setAllele(new Double(777));

            nested3.addGene(nnn1);
            nested3.addGene(nnn2);

            nested2.addGene(nested3);

            String representation =
                gene.getPersistentRepresentation ();

            System.out.println ("Old representation: " + representation);

            instantiableSupergene restored = new instantiableSupergene ();
            restored.setValueFromPersistentRepresentation (representation);

            System.out.println ("New representation: " +
                                restored.getPersistentRepresentation ());

            System.out.println("Old gene "+gene);
            System.out.println("New gene "+restored);

            return gene.equals (restored);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
     };

     public static void main(String[] args) {
         System.out.println(
          testRepresentation()
          );
     }

}
