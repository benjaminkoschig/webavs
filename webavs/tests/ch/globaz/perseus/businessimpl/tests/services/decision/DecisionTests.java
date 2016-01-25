package ch.globaz.perseus.businessimpl.tests.services.decision;

import junit.framework.Test;
import junit.framework.TestSuite;
import ch.globaz.perseus.tests.util.End;

public class DecisionTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for ch.globaz.perseus.businessimpl.services.models.situationfamille");
        // $JUnit-BEGIN$
        // suite.addTestSuite(Init.class);

        // suite.addTestSuite(DecisionTestCase.class);

        suite.addTestSuite(End.class);
        // $JUnit-END$
        return suite;
    }

}
