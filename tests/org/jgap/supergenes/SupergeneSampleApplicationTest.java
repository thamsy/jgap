package org.jgap.supergenes;

import junit.framework.*;

/**
* Test Supergene sample application, verifying the "make change"
* supergene and non-supergene versions. Both must return a zero error.
* Performance is not verified.
* @return true if the Supergene tests succeded.
*/

public class SupergeneSampleApplicationTest extends TestCase {

  public void testSupergeneTotal()
  {
    int E_s = Integer.MAX_VALUE;
    Test:
    for (int i = 0; i < 2; i++) {
      p_abstractSupergeneTest.EXISTING_SOLUTIONS_ONLY = true;
      p_abstractSupergeneTest.REPORT_ENABLED = false;
      p_Force.REPORT_ENABLED = false;

      p_abstractSupergeneTest.MAX_ALLOWED_EVOLUTIONS = 512;
      p_abstractSupergeneTest.POPULATION_SIZE = 256;

      abstractSupergene.reset ();
      E_s = new p_SupergeneTest().test ();

      if (E_s == 0) break Test;

      assertTrue("Correctness of solution: supergene "+E_s, E_s <3);

    }
    assertTrue("Correctness of solution: supergene "+E_s, E_s == 0);
  }

  protected void setUp() throws Exception {
      super.setUp();
  }

  protected void tearDown() throws Exception {
      super.tearDown();
  }

  public static Test suite() {
    TestSuite suite =
     new TestSuite(SupergeneSampleApplicationTest.class);
    return suite;
  }

}