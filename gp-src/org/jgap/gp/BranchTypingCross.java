package org.jgap.gp;

import org.jgap.*;

public class BranchTypingCross
    extends CrossMethod {

  public BranchTypingCross(GPConfiguration a_config) {
    super(a_config);
  }

  /**
   * Crosses two individuals. A random chromosome is chosen for crossing based
   * probabilistically on the proportion of nodes in each chromosome in the
   * first individual.
   * @param i1 the first individual to cross
   * @param i2 the second individual to cross
   * @return an array of the two resulting individuals
   */
  public ProgramChromosome[] cross(ProgramChromosome i1, ProgramChromosome i2) {
    try {
      // Determine which chromosome we'll cross, probabilistically determined
      // by the sizes of the chromosomes of the first individual --
      // equivalent to Koza's branch typing.
      int numChroms = 1;//i1.size();
      int[] sizes = new int[numChroms];
      int totalSize = 0;

//      CommandGene[] commands = i1.getFunctions();
//      for (int i = 0; i < i1.size(); i++) {
//        if (commands == null || commands[i] == null) {
//          throw new RuntimeException("1, i=" + i);
//        }
//        sizes[i] = commands[i].size(); //i1.size(); // chromosomes[i].getSize(0);
//        totalSize += sizes[i];
//      }

       sizes[0] = i1.getSize(0);
       totalSize += sizes[0];

      if (totalSize == 0) {
        totalSize = 0;
      }
      int nodeNum = getConfiguration().getRandomGenerator().nextInt(
          totalSize);
      int chromosomeNum;

      for (chromosomeNum = 0; chromosomeNum < numChroms; chromosomeNum++) {
        nodeNum -= sizes[chromosomeNum];
        if (nodeNum < 0)
          break;
      }

      // Cross the selected chromosomes
      ProgramChromosome[] newChromosomes = doCross(getConfiguration(),
          i1, //.chromosomes[chromosomeNum],
          i2); //.chromosomes[chromosomeNum]);

      // Create the new individuals by copying the uncrossed chromosomes
      // and setting the crossed chromosome. There's no need to deep-copy
      // the uncrossed chromosomes because they don't change. That is,
      // even if two individuals' chromosomes point to the same chromosome,
      // the only change in a chromosome is crossing, which generates
      // deep-copied chromosomes anyway.
      if (i1.getGenes() == null || i2.getGenes() == null || newChromosomes[0].getGenes() == null
          || newChromosomes[1].getGenes() == null ||newChromosomes[0].getGene(0)==null
          ||newChromosomes[1].getGene(0)==null) {
        throw new RuntimeException("NIX");
      }

      return newChromosomes;
    }
    catch (InvalidConfigurationException iex) {
      return null;
    }
  }

  /**
   * Crosses two chromsomes using branch-typing.
   * <p>
   * A random point in the first chromosome is chosen,
   * with 90% probability it will be a function and 10% probability it will be
   * a terminal. A random point in the second chromosome
   * is chosen using the same
   * probability distribution, but the node chosen must be of the same type as the chosen node
   * in the first chromosome.
   * <p>
   * If a suitable point in the second chromosome couldn't be found then the
   * chromosomes are not crossed.
   * <p>
   * If a resulting chromosome's depth is larger than the World's maximum crossover depth
       * then that chromosome is simply copied from the original rather than crossed.
   * <p>
   * @param a_config the configuration to use
   * @param c0 the first chromosome to cross
   * @param c1 the second chromosome to cross
   * @return an array of the two resulting chromosomes
   * @throws InvalidConfigurationException
   *
   * @since 1.0
   */
  protected static ProgramChromosome[] doCross(GPConfiguration a_config,
                                               ProgramChromosome c0,
                                               ProgramChromosome c1)
  throws InvalidConfigurationException {
    /**@todo here we need to work with details of ProgramChromosome (i.e.
     * CommandGenes in the context of JGAP. But this does not work as a
     * CommandGene does not have a collection of function and therefor not a
     * method "int numFunctions()".
     */
    ProgramChromosome[] c = {
        c0, c1};

    // Choose a point in c1
    int p0;

    if (a_config.getRandomGenerator().nextFloat() < 0.9f) {
      // choose a function
      int nf = c0.numFunctions();
      if (nf == 0) {
        // no functions
        return c;
      }
      p0 = c0.getFunction(a_config.getRandomGenerator().
                          nextInt(nf));
    }
    else {
      //040206:folgendes auskommentiert, da es gehen MUSSS!
      // choose a terminal
      if (c0.numTerminals() < 1) { //KM
//        return new ProgramChromosome[]{c0,c1};//KM
        int oo= 2;
      }
//      else { //KM
        p0 = c0.getTerminal(a_config.getRandomGenerator().
                            nextInt(c0.numTerminals()));
//      }
    }

    // Choose a point in c2 matching the type
    int p1;
    Class t = c0.getNode(p0).getReturnType();

    if (a_config.getRandomGenerator().nextFloat() < 0.9f) {
      // choose a function
      int nf = c1.numFunctions(t);
      if (nf == 0) {
        // no functions of that type
        return c;
      }
      p1 = c1.getFunction(a_config.getRandomGenerator().
                          nextInt(nf), t);
    }
    else {
      // choose a terminal
      int nt = c1.numTerminals(t);
      if (nt == 0) {
        // no terminals of that type
        return c;
      }
      p1 = c1.getTerminal(a_config.getRandomGenerator().
                          nextInt(c1.numTerminals(t)), t);
    }

    int s0 = c0.getSize(p0);
    int s1 = c1.getSize(p1);
    int d0 = c0.getDepth(p0);
    int d1 = c1.getDepth(p1);
    int c0s = c0.getSize(0);
    int c1s = c1.getSize(0);

//    System.out.println("Cross p0 " + p0 + " s0 " + s0 + " d0 " + d0 +
//      " p1 " + p1 + " s1 " + s1 + " d1 " + d1 + " c0s " + c0s + " c1s " + c1s);

    // Check for depth constraint for p1 inserted into c0
    if (d0 - 1 + s1 > a_config.getMaxCrossoverDepth()) {
      // choose the other parent
      c[0] = c1;
    }
    else {
//      c[0] = new ProgramChromosome(c0s - s0 + s1, c[0].functionSet,
      c[0] = new ProgramChromosome(a_config, c0s - s0 + s1, c[0].getFunctionSet(),c[0].getFunctions(),
                                   c[0].getArgTypes());
      System.arraycopy(c0.getFunctions(), 0, c[0].getFunctions(), 0, p0);
      System.arraycopy(c1.getFunctions(), p1, c[0].getFunctions(), p0, s1);
      if (c0s - p0 - s0 < 1 && false) {
        int i=1;
      }
      else { //KM: if-else eingef�hrt
        System.arraycopy(c0.getFunctions(), p0 + s0, c[0].getFunctions(),
                         p0 + s1,
                         c0s - p0 - s0);
        c[0].redepth();
      }
    }

    // Check for depth constraint for p0 inserted into c1
    if (d1 - 1 + s0 > a_config.getMaxCrossoverDepth()) {
      // choose the other parent
      c[1] = c0;
    }
    else {
//      c[1] = new ProgramChromosome(c1s - s1 + s0, c[1].functionSet,
      c[1] = new ProgramChromosome(a_config, c1s - s1 + s0, c[1].getFunctionSet(),c[0].getFunctions(),
                                   c[1].getArgTypes());
      System.arraycopy(c1.getFunctions(), 0, c[1].getFunctions(), 0, p1);
      System.arraycopy(c0.getFunctions(), p0, c[1].getFunctions(), p1, s0);
      if (c1s - p1 - s1 < 1 && false) {
        int i=1;
      }
      else { //KM: if-else eingef�hrt
        System.arraycopy(c1.getFunctions(), p1 + s1, c[1].getFunctions(),
                         p1 + s0,
                         c1s - p1 - s1);
        c[1].redepth();
      }
    }

    return c;
  }
}