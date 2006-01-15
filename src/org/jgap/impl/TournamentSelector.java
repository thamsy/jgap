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
import org.jgap.data.config.*;

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
  private final static String CVS_REVISION = "$Revision: 1.15 $";

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
   * Default Constructor, needed to create Configurable via Class class.
   * @author Siddhartha Azad.
   */
  public TournamentSelector() {
    super();
    m_chromosomes = new Vector();
    m_fitnessValueComparator = new FitnessValueComparator();
  }

  /**
   * Constructor
   * @param a_tournament_size the size of each tournament to play
   * @param a_probability probability for selecting the best individuals
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public TournamentSelector(final int a_tournament_size,
                            final double a_probability) {
    this();
    if (a_tournament_size < 1) {
      throw new IllegalArgumentException("Tournament size must be at least 1!");
    }
    if (a_probability <= 0.0d || a_probability > 1.0d) {
      throw new IllegalArgumentException("Probability must be greater 0.0 and"
                                         + " less or equal than 1.0!");
    }
    m_tournament_size = a_tournament_size;
    m_probability = a_probability;
  }

  public void setTournamentSize(final int a_tournament_size) {
    if (a_tournament_size < 1) {
      throw new IllegalArgumentException("Tournament size must be at least 1!");
    }
    m_tournament_size = a_tournament_size;
  }

  public int getTournamentSize() {
    return m_tournament_size;
  }

  public double getProbability() {
    return m_probability;
  }

  public void setProbability(final double a_probability) {
    if (a_probability <= 0.0d || a_probability > 1.0d) {
      throw new IllegalArgumentException("Probability must be greater 0.0 and"
                                         + " less or equal than 1.0!");
    }
    m_probability = a_probability;
  }

  /**
   * Select a given number of Chromosomes from the pool that will move on
   * to the next generation population. This selection will be guided by the
   * fitness values. The chromosomes with the best fitness value win.
   *
   * @param a_howManyToSelect int
   * @param a_from_pop the population the Chromosomes will be selected from
   * @param a_to_pop the population the Chromosomes will be added to
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void select(final int a_howManyToSelect, final Population a_from_pop,
                     Population a_to_pop) {
    if (a_from_pop != null) {
      for (int i = 0; i < a_from_pop.size(); i++) {
        add(a_from_pop.getChromosome(i));
      }
    }
    List tournament = new Vector();
    RandomGenerator rn = Genotype.getConfiguration().getRandomGenerator();
    int size = m_chromosomes.size();
    if (size == 0) {
      return;
    }
    int k;
    for (int i = 0; i < a_howManyToSelect; i++) {
      // choose [tournament size] individuals from the population at random
      tournament.clear();
      for (int j = 0; j < m_tournament_size; j++) {
        k = rn.nextInt(size);
        tournament.add(m_chromosomes.get(k));
      }
      Collections.sort(tournament, m_fitnessValueComparator);
      double prob = rn.nextDouble();
      double probAccumulated = m_probability;
      int index = 0;
      //play the tournament
      if (m_tournament_size > 1) {
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
      }
      a_to_pop.addChromosome( (IChromosome) tournament.get(index));
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
  protected void add(final IChromosome a_chromosomeToAdd) {
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
    public int compare(final Object a_first, final Object a_second) {
      IChromosome chrom1 = (IChromosome) a_first;
      IChromosome chrom2 = (IChromosome) a_second;
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
  /**
   * Get the ConfigurationHandler for this class.
   *
   * @author Siddhartha Azad
   * @since 2.0
   * */
  public ConfigurationHandler getConfigurationHandler()
      throws ConfigException {
    TournamentSelectorConHandler conHandler = new TournamentSelectorConHandler();
    conHandler.setConfigurable(this);
    return conHandler;
  }

  /**
   * Pass the name and value of a property to be set.
   * @param a_name the name of the property
   * @param a_value the value of the property
   *
   * @author Siddhartha Azad.
   * @since 2.0
   * */
  public void setConfigProperty(String a_name, String a_value)
      throws ConfigException, InvalidConfigurationException {
    if (a_name.equals("m_probability")) {
      m_probability = Double.parseDouble(a_name);
    }
    else {
      System.err.println("TournamentSelector:Unknown property " + a_name);
    }
  }

  /**
   * Pass the name and values of a property to be set.
   * @param name The name of the property.
   * @param values The different values of the property.
   *
   * @author Siddhartha Azad.
   * @since 2.0
   * */
  public void setConfigMultiProperty(String name, ArrayList values)
      throws ConfigException, InvalidConfigurationException {
    // currently no multi-properties defined for TournamentSelectors
  }
}
