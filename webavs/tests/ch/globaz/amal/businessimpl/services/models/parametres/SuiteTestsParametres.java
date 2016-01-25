/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.parametres;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author dhi
 * 
 */
public class SuiteTestsParametres {
    /**
     * Initialise la suite de test
     * 
     * @return la suite initialisée
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for ch.globaz.amal.businessimpl.services.parametres");
        // $JUnit-BEGIN$
        // suite.addTestSuite(ParamModelServiceImplTest.class);
        // suite.addTestSuite(SimpleParametreAnnuelServiceImplTest.class);
        // suite.addTestSuite(SimplePrimeMoyenneServiceImplTest.class);
        // suite.addTestSuite(SimpleSubsideAnneeServiceImplTest.class);
        // $JUnit-END$
        return suite;
    }

}
