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
package org.jgap.perf;

import org.jgap.*;

/**
 * Sample fitness function for the MakeChange example.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class TestOverallPerformanceFitnessFunc
    extends FitnessFunction {
  private final int m_targetAmount;
  public TestOverallPerformanceFitnessFunc(int a_targetAmount) {
    if (a_targetAmount < 1 || a_targetAmount > 999) {
      throw new IllegalArgumentException(
          "Change amount must be between 1 and 999 cents.");
    }
    m_targetAmount = a_targetAmount;
  }

  /**
   * Determine the fitness of the given Chromosome instance. The higher the
   * return value, the more fit the instance. This method should always
   * return the same fitness value for two equivalent Chromosome instances.
   *
   * @param a_subject The Chromosome instance to evaluate.
   *
   * @return A positive integer reflecting the fitness rating of the given
   *         Chromosome.
   */
  public double evaluate(Chromosome a_subject) {
    // The fitness value measures both how close the value is to the
    // target amount supplied by the user and the total number of coins
    // represented by the solution. We do this in two steps: first,
    // we consider only the represented amount of change vs. the target
    // amount of change and return higher fitness values for amounts
    // closer to the target, and lower fitness values for amounts further
    // away from the target. If the amount equals the target, then we go
    // to step 2, which returns a higher fitness value for solutions
    // representing fewer total coins, and lower fitness values for
    // solutions representing more total coins.
    // ------------------------------------------------------------------
    int changeAmount = amountOfChange(a_subject);
    int totalCoins = getTotalNumberOfCoins(a_subject);
    int changeDifference = Math.abs(m_targetAmount - changeAmount);
    // Step 1: Determine distance of amount represented by solution from
    // the target amount. Since we know  the maximum amount of change is
    // 99 cents, we'll subtract the difference in change between the
    // solution amount and the target amount from 99. That will give
    // the desired effect of returning higher values for amounts
    // closer to the target amount and lower values for amounts
    // further away from the target amount.
    // -----------------------------------------------------------------
    int fitness = (99 - changeDifference);
    // Step 2: If the solution amount equals the target amount, then
    // we add additional fitness points for solutions representing fewer
    // total coins.
    // -----------------------------------------------------------------
    if (changeAmount == m_targetAmount) {
      fitness += 100 - (10 * totalCoins);
    }
    // Make sure fitness value is always positive.
    // -------------------------------------------
    return Math.max(1, fitness);
  }

  /**
   * Calculates the total amount of change (in cents) represented by
   * the given potential solution and returns that amount.
   * Here we use "fantasy" coins just to have more genes and bloat the time
   * consumed for test performance test
   *
   * @param a_potentialSolution The pontential solution to evaluate.
   * @return The total amount of change (in cents) represented by the
   *         given solution.
   */
  public static int amountOfChange(Chromosome a_potentialSolution) {
    int numQuarters = getNumberOfCoinsAtGene(a_potentialSolution, 0);
    int numDimes = getNumberOfCoinsAtGene(a_potentialSolution, 1);
    int numNickels = getNumberOfCoinsAtGene(a_potentialSolution, 2);
    int numPennies = getNumberOfCoinsAtGene(a_potentialSolution, 3);
    int A = getNumberOfCoinsAtGene(a_potentialSolution, 4);
    int B = getNumberOfCoinsAtGene(a_potentialSolution, 5);
    int C = getNumberOfCoinsAtGene(a_potentialSolution, 6);
    int D = getNumberOfCoinsAtGene(a_potentialSolution, 7);
    int E = getNumberOfCoinsAtGene(a_potentialSolution, 8);
    int F = getNumberOfCoinsAtGene(a_potentialSolution, 9);
    return (numQuarters * 25) + (numDimes * 10) + (numNickels * 5) +
        numPennies + (A * 29) + (B * 31) + (C * 37) + (D * 41) + (E * 43)
        + (F * 47);
  }

  /**
   * Retrieves the number of coins represented by the given potential
   * solution at the given gene position.
   *
   * @param a_potentialSolution The potential solution to evaluate.
   * @param a_position The gene position to evaluate.
   * @return the number of coins represented by the potential solution
   *         at the given gene position.
   */
  public static int getNumberOfCoinsAtGene(Chromosome a_potentialSolution,
                                           int a_position) {
    Integer numCoins =
        (Integer) a_potentialSolution.getGene(a_position).getAllele();
    return numCoins.intValue();
  }

  /**
   * Returns the total number of coins represented by all of the genes in
   * the given potential solution.
   *
   * @param a_potentialsolution The potential solution to evaluate.
   * @return The total number of coins represented by the given Chromosome.
   */
  public static int getTotalNumberOfCoins(Chromosome a_potentialsolution) {
    int totalCoins = 0;
    int numberOfGenes = a_potentialsolution.size();
    for (int i = 0; i < numberOfGenes; i++) {
      totalCoins += getNumberOfCoinsAtGene(a_potentialsolution, i);
    }
    return totalCoins;
  }
}
