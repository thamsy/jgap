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

import java.util.*;
import org.jgap.*;
import junit.framework.*;

/**
 * Test class for GaussianMutationOperator class
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class GaussianMutationOperatorTest
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.3 $";

  public GaussianMutationOperatorTest() {
  }

  public void setUp() {
    Genotype.setConfiguration(null);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(GaussianMutationOperatorTest.class);
    return suite;
  }

  public void testOperate_0()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    GeneticOperator op = new GaussianMutationOperator(0.15d);
    conf.addGeneticOperator(op);
    Genotype.setConfiguration(conf);
    RandomGeneratorForTest rand = new RandomGeneratorForTest();
    rand.setNextDouble(0.45d);
    conf.setRandomGenerator(rand);
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene sampleGene = new IntegerGene(1, 10);
    Chromosome chrom = new Chromosome(sampleGene, 3);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(6);
    Gene cgene1 = new IntegerGene(1, 10);
    cgene1.setAllele(new Integer(6));
    Gene[] genes1 = new Gene[] {
        cgene1};
    Chromosome chrom1 = new Chromosome(genes1);
    Gene cgene2 = new IntegerGene(1, 10);
    cgene2.setAllele(new Integer(9));
    Gene[] genes2 = new Gene[] {
        cgene2};
    Chromosome chrom2 = new Chromosome(genes2);
    Chromosome[] population = new Chromosome[] {
        chrom1, chrom2};
    List chroms = new Vector();
    Gene gene1 = new IntegerGene(1, 10);
    gene1.setAllele(new Integer(5));
    chroms.add(gene1);
    Gene gene2 = new IntegerGene(1, 10);
    gene2.setAllele(new Integer(7));
    chroms.add(gene2);
    Gene gene3 = new IntegerGene(1, 10);
    gene3.setAllele(new Integer(4));
    chroms.add(gene3);
    assertEquals(3, chroms.size());
    op.operate(new Population(population), chroms);
    assertEquals(3 + 2, chroms.size());
    Chromosome target = (Chromosome) chroms.get(4);
    assertEquals(Math.round( (10 - 1) * (0.45d * 0.15d) + 9),
                 ( (Integer) target.getGene(0).getAllele()).intValue());
    target = (Chromosome) chroms.get(3);
    assertEquals(Math.round( (10 - 1) * (0.45d * 0.15d) + 6),
                 ( (Integer) target.getGene(0).getAllele()).intValue());
  }

  /**
   * Tests if crossing over produces same results for two operate-runs
   * @throws Exception
   */
  public void testOperate_1()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    GeneticOperator op = new GaussianMutationOperator();
    Genotype.setConfiguration(conf);
    conf.addGeneticOperator(op);
    RandomGeneratorForTest rand = new RandomGeneratorForTest();
    rand.setNextIntSequence(new int[] {
                            0, 1, 0, 1, 2});
    conf.setRandomGenerator(rand);
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene sampleGene = new IntegerGene(1, 10);
    Chromosome chrom = new Chromosome(sampleGene, 3);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(6);
    Gene cgene1 = new IntegerGene(1, 10);
    cgene1.setAllele(new Integer(6));
    Gene[] genes1 = new Gene[] {
        cgene1};
    Chromosome chrom1 = new Chromosome(genes1);
    Gene cgene2 = new IntegerGene(1, 10);
    cgene2.setAllele(new Integer(8));
    Gene[] genes2 = new Gene[] {
        cgene2};
    Chromosome chrom2 = new Chromosome(genes2);
    Chromosome[] population = new Chromosome[] {
        chrom1, chrom2};
    List chroms = new Vector();
    Gene gene1 = new IntegerGene(1, 10);
    gene1.setAllele(new Integer(5));
    chroms.add(gene1);
    Gene gene2 = new IntegerGene(1, 10);
    gene2.setAllele(new Integer(7));
    chroms.add(gene2);
    Gene gene3 = new IntegerGene(1, 10);
    gene3.setAllele(new Integer(4));
    chroms.add(gene3);
    Chromosome[] population2 = (Chromosome[]) population.clone();
    op.operate(new Population(population), chroms);
    op.operate(new Population(population2), chroms);
    assertTrue(isChromosomesEqual(population, population2));
  }

  public static boolean isChromosomesEqual(Chromosome[] list1,
                                           Chromosome[] list2) {
    if (list1 == null) {
      if (list2 == null) {
        return true;
      }
      else {
        return false;
      }
    }
    else if (list2 == null) {
      return false;
    }
    else {
      if (list1.length != list2.length) {
        return false;
      }
      else {
        for (int i = 0; i < list1.length; i++) {
          Chromosome c1 = (Chromosome) list1[i];
          Chromosome c2 = (Chromosome) list2[i];
          if (!c1.equals(c2)) {
            return false;
          }
        }
        return true;
      }
    }
  }

  /**@todo test against CompositeGene*/
}
