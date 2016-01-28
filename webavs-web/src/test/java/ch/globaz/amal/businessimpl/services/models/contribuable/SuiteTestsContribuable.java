package ch.globaz.amal.businessimpl.services.models.contribuable;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Suite de tests pour les contribuables
 * 
 * @author CBU
 * 
 */
public class SuiteTestsContribuable {

    /**
     * Initialise la suite de test
     * 
     * @return la suite initialisée
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for ch.globaz.amal.businessimpl.services.models.contribuable");
        // $JUnit-BEGIN$
        // suite.addTestSuite(SimpleContribuableServiceImplTest.class);
        // suite.addTestSuite(SimpleContribuableInfosTest.class);
        // suite.addTestSuite(ContribuableServiceImplTest.class);
        // $JUnit-END$
        return suite;
    }

}
