/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.document;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Suite de tests pour les documents, table MAENVDOC, et services associ�s
 * 
 * @author DHI
 * 
 */
public class SuiteTestsDocument {

    /**
     * Initialise la suite de test
     * 
     * @return la suite initialis�e
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for ch.globaz.amal.businessimpl.services.models.document");
        // $JUnit-BEGIN$
        // suite.addTestSuite(DocumentServiceImplTest.class);
        // $JUnit-END$
        return suite;
    }

}
