package ch.globaz.perseus.businessimpl.tests.services.calcul;

import junit.framework.Test;
import junit.framework.TestSuite;
import ch.globaz.perseus.tests.util.End;

public class CalculTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for ch.globaz.perseus.businessimpl.services.models.situationfamille");
        // $JUnit-BEGIN$
        // suite.addTestSuite(Init.class);

        // suite.addTestSuite(CalculTestCase.class);

        suite.addTestSuite(End.class);
        // $JUnit-END$
        return suite;
    }

}
