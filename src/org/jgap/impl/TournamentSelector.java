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
public class TournamentSelector
    extends NaturalSelector {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  /**
   * The probability for selecting the best chromosome in a tournament.
   * For the second-best chromosome it would be p * (1 - p).
   * For the third-best chromosome it would be p * (p * (1 - p)) and so forth.
   */
  private double m_probability;

  /**
   * Size of a tournament to be played, i.e. number of chromosomes taken into
   * account for one selection round
   */
  private int m_tournament_size;

  private List m_chromosomes;

  /**
   * Comparator that is only concerned about fitness values
   */
  private FitnessValueComparator m_fitnessValueComparator;

  /**
   *
   * @param a_tournament_size int
   * @param a_probability double
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public TournamentSelector(int a_tournament_size, double a_probability) {
    super();
    m_tournament_size = a_tournament_size;
    m_probability = a_probability;
    m_chromosomes = new Vector();
    m_fitnessValueComparator = new FitnessValueComparator();
  }

  /**
   *
   * @param a_howManyToSelect int
   * @return Population
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public Population select(int a_howManyToSelect) {

    Population pop = new Population(a_howManyToSelect);
    List tournament = new Vector();
    RandomGenerator rn = Genotype.getConfiguration().getRandomGenerator();
    int size = m_chromosomes.size();
    int k;
    for (int i = 0; i < a_howManyToSelect; i++) {
      // choose [tournament size] individuals from the population at random
      tournament.clear();
      for (int j = 0; j < m_tournament_size; j++) {
        k = rn.nextInt(size - 1);
        tournament.add(m_chromosomes.get(k));
      }
      Collections.sort(tournament, m_fitnessValueComparator);
      double prob = rn.nextDouble();
      double probAccumulated = m_probability;
      int index = 0;
      do {
        if (prob <= probAccumulated) {
          break;
        }
        else {
          probAccumulated += probAccumulated * (1 - m_probability);
          index++;
        }
      }
      while (index < m_tournament_size - 1);
      pop.addChromosome( (Chromosome) tournament.get(index));
    }
    return pop;
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
  public void add(Chromosome a_chromosomeToAdd) {
    m_chromosomes.add(a_chromosomeToAdd);
  }

  /**
   * Comparator regarding only the fitness value. Best fitness value will
   * be on first position of resulted sorted list
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
