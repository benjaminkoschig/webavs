package ch.globaz.pegasus.businessimpl.tests.calcul;

import globaz.jade.client.util.JadeStringUtil;
import junit.framework.Assert;
import ch.globaz.pegasus.tests.util.CasTest;
import ch.globaz.pegasus.tests.util.TestBaseData;

public class InitAllCasTestCase {

    // @Override
    // public void setUp() throws Exception {
    // super.setUp();
    // }

    public void testInitCasTests() throws Exception {
        for (CasTest casTest : TestBaseData.casForTest.values()) {
            casTest.getDescription();
            Assert.assertTrue("Test cas test non null", casTest != null);
            Assert.assertTrue("Test dossier non null avec id",
                    (casTest.getDossier() != null) && !JadeStringUtil.isBlank(casTest.getDossier().getId()));
            Assert.assertTrue("Test demande non null avec id",
                    (casTest.getDemande() != null) && !JadeStringUtil.isBlank(casTest.getDemande().getId()));
            Assert.assertTrue("Test droit non null avec id",
                    (casTest.getDroit() != null) && !JadeStringUtil.isBlank(casTest.getDroit().getId()));
        }

    }
}
