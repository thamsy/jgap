/*
 * Copyright 2001, Neil Rotstan
 *
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

package org.jgap;

import java.util.*;


/**
 * Genotypes represent fixed-length collections or "populations" of
 * Chromosomes. As an instance of a genotype is evolved, all of its
 * Chromosomes are also evolved.
 *
 * @author Neil Rotstan (neil at bluesock.org)
 */
public class Genotype implements java.io.Serializable {
  protected static Random generator = new Random();

  protected Configuration gaConf;
  protected Chromosome[] chromosomes;
  protected List workingPool;

  /**
   * Constructs a new Genotype instance with the given array
   * of Chromosomes and the given configuration settings. Note
   * that the Configuration instance must be in a valid state
   * when this method is invoked, or a InvalidconfigurationException
   * will be thrown.
   *
   * @param configuration: An instance of a Configuration object
   *                       that will control the behavior of this
   *                       GA execution.
   * @param initialChromosomes: The Chromosome population to be
   *                            managed by this Genotype instance.
   *
   * @throws InvalidConfigurationException if the given Configuration
   *         is null or in an invalid state.
   */
  public Genotype(Configuration configuration,
                  Chromosome[] initialChromosomes)
         throws InvalidConfigurationException {
    if (configuration == null) {
      throw new InvalidConfigurationException(
        "The Configuration instance must not be null.");
    }

    configuration.lockSettings();
    chromosomes = initialChromosomes;
    gaConf = configuration;

    workingPool = new ArrayList();
  }


  /**
   * Retrieve the array of Chromosomes that make up this
   * Genotype instance.
   *
   * @return The chromosomes that make up this Genotype.
   */
  public Chromosome[] getChromosomes()
  {
    return chromosomes;
  }


  /**
   * Retrieve the Chromosome in the population with the highest
   * fitness value.
   *
   * @return The Chromosome with the highest fitness value.
   */
  public Chromosome getFittestChromosome() {
    if (chromosomes.length == 0) {
      return null;
    }

    Chromosome fittestChromosome = chromosomes[0];
    int fittestValue = 
      gaConf.getFitnessFunction().evaluate(fittestChromosome); 

    for( int i = 1; i < chromosomes.length; i++) {
      int fitnessValue = gaConf.getFitnessFunction().evaluate(chromosomes[i]);
      if (fitnessValue > fittestValue)
      {
        fittestChromosome = chromosomes[i];
        fittestValue = fitnessValue;
      }
    }

    return fittestChromosome;
  }


  /**
   * Evolve the collection of Chromosomes within this
   * Genotype. This is implemented via the following steps:
   *
   * (1) Reproduce each Chromosome instance.
   * (2) Randomly select two Chromosome instances and have
   *     them crossover. Repeat this step a number of times 
   *     equal to the original number of Chromosomes managed
   *     by this Genotype instance.
   * (3) Give each Chromosome instance an opportunity to mutate.
   * (4) Evaluate the fitness of each Chromosome instance and
   *     hand the Chromosome and its fitness value to a natural
   *     selector.
   * (5) Have the natural selector choose a number of Chromosome
   *     instances equal to the size of the original population
   *     managed by this Genotype. These instances become the new
   *     population managed by this Genotype instance.
   */
  public void evolve() {
    workingPool.removeAll(workingPool);

    for(int i = 0; i < chromosomes.length; i++) {
      // Step 1.
      workingPool.add(chromosomes[i].reproduce());

      // Step 2.
      Chromosome firstMate =
        chromosomes[generator.nextInt(chromosomes.length)];
        
      Chromosome secondMate =   
        chromosomes[generator.nextInt(chromosomes.length)];

      firstMate.crossover(secondMate);

      workingPool.add(firstMate);
      workingPool.add(secondMate);
    }


    Iterator iterator = workingPool.iterator();

    while(iterator.hasNext()) {
      Chromosome currentChromosome = (Chromosome) iterator.next();

      // Step 3.
      currentChromosome.mutate();

      // Step 4.
      gaConf.getNaturalSelector().add(
        currentChromosome,
        gaConf.getFitnessFunction().evaluate(currentChromosome));
    }

    // Step 5.
    chromosomes = gaConf.getNaturalSelector().select(chromosomes.length);
  }


  /**
   * Return a string representation of this Genotype instance,
   * useful for debugging purposes.
   *
   * @return A string representation of this Genotype instance.
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();

    for(int i = 0; i < chromosomes.length; i++) {
      buffer.append(chromosomes[i].toString());
      buffer.append(" [" );
      buffer.append(gaConf.getFitnessFunction().evaluate(chromosomes[i]));
      buffer.append("]");
      buffer.append('\n');
    }

    return buffer.toString();
  }

  /**
   * Convenience method that returns a newly constructed Genotype
   * instance configured according to the given Configuration instance.
   * The population of Chromosomes will be randomly generated.
   *
   * Note that the given Configuration instance must be in a valid state
   * at the time this method is invoked, or an InvalidConfigurationException
   * will be thrown.
   *
   * @return A newly constructed Genotype instance.
   *
   * @throws InvalidConfigurationException if the given Configuration
   *         instance is null or invalid.
   */
   public static Genotype randomInitialGenotype(Configuration gaConf)
                          throws InvalidConfigurationException {
     if (gaConf == null) {
       throw new InvalidConfigurationException(
         "The Configuration instance must not be null.");
     }

     gaConf.lockSettings();

     int populationSize = gaConf.getPopulationSize();
     int chromosomeSize = gaConf.getChromosomeSize();
     Chromosome[] chromosomes = new Chromosome[populationSize];

     for(int i = 0; i < populationSize; i++) {
       chromosomes[i] = Chromosome.randomInitialChromosome(gaConf);
     }

     return new Genotype(gaConf, chromosomes);
   }
}

