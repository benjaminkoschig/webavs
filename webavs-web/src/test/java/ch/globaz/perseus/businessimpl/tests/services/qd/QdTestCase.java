package ch.globaz.perseus.businessimpl.tests.services.qd;

import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.models.qd.QDAnnuelle;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class QdTestCase {
    //
    // @Override
    // public void setUp() throws Exception {
    // super.setUp();
    // }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.tests.util.BaseTestCase#tearDown()
     */
    // @Override
    // public void tearDown() {
    // // genere erreur pour provoquer un rollback de la db
    // try {
    // // JadeThread.rollbackSession();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // super.tearDown();
    // }

    public final void test01createQDs() throws Exception {
        PCFAccordee pcfa = PerseusServiceLocator.getPCFAccordeeService().read("18");
        QDAnnuelle qdAnnuelle = PerseusServiceLocator.getQDAnnuelleService().createOrRead(pcfa, "2011");

    }

}