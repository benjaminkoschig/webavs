package ch.globaz.amal.businessimpl.services.models.famille;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author CBU
 * 
 */
public class SuiteTestsFamille {
    /**
     * Initialise la suite de test
     * 
     * @return la suite initialisée
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for ch.globaz.amal.businessimpl.services.models.famille");
        // $JUnit-BEGIN$
        // suite.addTestSuite(SimpleFamilleServiceImplTest.class);
        // suite.addTestSuite(FamilleServiceImplTest.class);
        // $JUnit-END$
        return suite;
    }
}
