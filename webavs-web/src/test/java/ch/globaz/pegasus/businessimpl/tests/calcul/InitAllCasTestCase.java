package ch.globaz.pegasus.businessimpl.tests.calcul;

import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.pegasus.tests.util.CasTest;
import ch.globaz.pegasus.tests.util.TestBaseData;

import static org.junit.Assert.assertTrue;

public class InitAllCasTestCase {

    // @Override
    // public void setUp() throws Exception {
    // super.setUp();
    // }

    public void testInitCasTests() throws Exception {
        for (CasTest casTest : TestBaseData.casForTest.values()) {
            casTest.getDescription();
            assertTrue("Test cas test non null", casTest != null);
            assertTrue("Test dossier non null avec id",
                    (casTest.getDossier() != null) && !JadeStringUtil.isBlank(casTest.getDossier().getId()));
            assertTrue("Test demande non null avec id",
                    (casTest.getDemande() != null) && !JadeStringUtil.isBlank(casTest.getDemande().getId()));
            assertTrue("Test droit non null avec id",
                    (casTest.getDroit() != null) && !JadeStringUtil.isBlank(casTest.getDroit().getId()));
        }

    }
}
