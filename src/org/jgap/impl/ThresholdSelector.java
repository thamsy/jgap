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

import java.util.*;
import org.jgap.*;

/**
 * Implementation of a NaturalSelector that plays tournaments to determine
 * the chromosomes to be taken to the next generation.
 * <p>
 * The tournament size can be adjusted as well as the probability for selecting
 * an individual.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class ThresholdSelector
    extends NaturalSelector {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * Stores the chromosomes to be taken into account for selection
   */
  private List m_chromosomes;

  /**
   * Indicated whether the list of added chromosomes needs sorting
   */
  private boolean m_needsSorting;

  /**
   * This percentage indicates the number of best chromosomes from the
   * population to be selected for granted. All other chromosomes will
   * be selected in a random fashion.
   */
  private double m_bestChroms_Percentage;

  /**
   * Standard constructor
   *
   * @param a_bestChromosomes_Percentage indicates the number of best
   * chromosomes from the population to be selected for granted. All other
   * chromosomes will be selected in a random fashion.

   * @author Klaus Meffert
   * @since 2.0
   */
  public ThresholdSelector(double a_bestChromosomes_Percentage) {
    super();
    m_bestChroms_Percentage = a_bestChromosomes_Percentage;
    m_chromosomes = new Vector();
  }

  /**
   * Comparator that is only concerned about fitness values
   */
  private FitnessValueComparator m_fitnessValueComparator;

  /**
   * Select a given number of Chromosomes from the pool that will move on
   * to the next generation population. This selection will be guided by the
   * fitness values. The chromosomes with the best fitness value win.

   * @param a_howManyToSelect The number of Chromosomes to select.
   * @param a_from_pop the population the Chromosomes will be selected from.
   * @param a_to_pop the population the Chromosomes will be added to.
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void select(int a_howManyToSelect, Population  a_from_pop, Population a_to_pop) {
    if (a_from_pop != null) {
      for (int i = 0; i < a_from_pop.size(); i++) {
        add(a_from_pop.getChromosome(i));
      }
    }
    // Sort the collection of chromosomes previously added for evaluation.
    // Only do this if necessary.
    // -------------------------------------------------------------------
    if (m_needsSorting) {
      Collections.sort(m_chromosomes, m_fitnessValueComparator);
      m_needsSorting = false;
    }

    // Select the best chromosomes for granted
    int bestToBeSelected = (int) Math.round(a_howManyToSelect *
                                            m_bestChroms_Percentage);
    for (int i = 0; i < bestToBeSelected; i++) {
      a_to_pop.addChromosome((Chromosome)m_chromosomes.get(i));
    }

    // Fill up the rest by randomly selecting chromosomes
    int missing = a_howManyToSelect - bestToBeSelected;
    RandomGenerator rn = Genotype.getConfiguration().getRandomGenerator();
    int index;
    int size = m_chromosomes.size();
    for (int i = 0; i < missing; i++) {
      index = rn.nextInt(size);
      a_to_pop.addChromosome((Chromosome)m_chromosomes.get(index));
    }
  }

  /**
   * @return false as we allow to return the same chromosome multiple times
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public boolean returnsUniqueChromosomes() {
    return false;
  }

  public void empty() {
    m_chromosomes.clear();
  }

  /**
   *
   * @param a_chromosomeToAdd Chromosome
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  protected void add(Chromosome a_chromosomeToAdd) {
    m_chromosomes.add(a_chromosomeToAdd);
  }

  /**
   * Comparator regarding only the fitness value. Best fitness value will
   * be on first position of resulting sorted list
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  private class FitnessValueComparator
      implements Comparator {
    public int compare(Object first, Object second) {
      Chromosome chrom1 = (Chromosome) first;
      Chromosome chrom2 = (Chromosome) second;

      if (Genotype.getConfiguration().getFitnessEvaluator().isFitter(chrom2.
          getFitnessValue(), chrom1.getFitnessValue())) {
        return 1;
      }
      else if (Genotype.getConfiguration().getFitnessEvaluator().isFitter(
          chrom1.getFitnessValue(), chrom2.getFitnessValue())) {
        return -1;
      }
      else {
        return 0;
      }
    }
  }
}
