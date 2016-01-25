package ch.globaz.perseus.businessimpl.tests.services.parametres;

import ch.globaz.perseus.business.models.parametres.SimpleZone;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class ParametresTestCase {
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

    public final void test01CreateZone() throws Exception {
        SimpleZone simpleZone = new SimpleZone();
        simpleZone.setDesignation("Zone 1");

        simpleZone = PerseusServiceLocator.getSimpleZoneService().create(simpleZone);

        System.out.println(simpleZone.getId());
    }

}