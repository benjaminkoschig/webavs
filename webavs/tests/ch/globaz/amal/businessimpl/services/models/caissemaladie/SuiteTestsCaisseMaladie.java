package ch.globaz.amal.businessimpl.services.models.caissemaladie;

import junit.framework.Test;
import junit.framework.TestSuite;

public class SuiteTestsCaisseMaladie {
    /**
     * Initialise la suite de test
     * 
     * @return la suite initialisée
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for ch.globaz.amal.businessimpl.services.models.caissemalaide");
        // $JUnit-BEGIN$
        // suite.addTestSuite(CaisseMaladieServiceImplTest.class);
        // $JUnit-END$
        return suite;
    }
}
