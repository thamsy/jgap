package org.jgap.gp;

import java.util.*;
import org.jgap.*;
import org.jgap.gp.*;

public class AddCommand
    extends MathCommand {
  public AddCommand(final Configuration a_conf, Class type)
      throws InvalidConfigurationException {
    super(a_conf, 2, type);
  }

  protected Gene newGeneInternal() {
    try {
      Gene gene = new AddCommand(getConfiguration(), getReturnType());
      return gene;
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public void applyMutation(int index, double a_percentage) {
    // Here, we could mutate the parameter of the command.
    // This is not applicable for this command, just do nothing
    System.err.println("appliedMutation");
  }

  public CommandGene mutateCommand() {
    /**@todo muss nicht hier gemacht werden, denn Mutationsergebnis ist (immer)
     * eine Funktion mit selbser Arit�t! Aber: Wertebereich kann unterschiedlich
     * sein! Also zu jeder Funktion angeben, welche anderen als �quivalent
     * f�r die Aufgabe angesehen werden (Wurzel ginge nicht f�r EXP, wenn auch
     * negative x vorkommen k�nnten).
     * @todo muss aber irgendwo durchgef�hrt werden, ist noch nicht der Fall
     */
    return null; //new MultiplyCommand();
  }

  public void evaluate(Configuration config, List parameters) {
    MathConfiguration mConfig = (MathConfiguration) config;
    double newResult = ( (Double) mConfig.popTerminal()).doubleValue();
    Double d1 = (Double) mConfig.popTerminal();
    newResult = newResult + d1.doubleValue();
    mConfig.pushTerminal(new Double(newResult));
  }

  public String toString() {
    return "+";
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    return c.execute_int(n, 0, args) + c.execute_int(n, 1, args);
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    return c.execute_long(n, 0, args) + c.execute_long(n, 1, args);
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    return c.execute_float(n, 0, args) + c.execute_float(n, 1, args);
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    return c.execute_double(n, 0, args) + c.execute_double(n, 1, args);
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    return ( (Compatible) c.execute_object(n, 0, args)).execute_add(c.
        execute_object(n, 1, args));
  }

  public static interface Compatible {
    public Object execute_add(Object o);
  }
}
