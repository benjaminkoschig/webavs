package ch.globaz.pegasus.businessimpl.tests.util;

import java.util.Calendar;
import junit.framework.Assert;
import ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil;

public class PegasusUtilDateTestCase {

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
    // JadeThread.rollbackSession();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // super.tearDown();
    // }

    public final void testDateDebutDateFinWithBooleanParameters() throws Exception {
        // Test date du jour de debut
        Calendar cal = Calendar.getInstance();
        cal.setTime(PegasusDateUtil.getPegasusMonthYearDate("01.2011", true));
        Assert.assertEquals("Test début période", 1, cal.get(Calendar.DAY_OF_MONTH));
        // Test date fin
        cal.setTime(PegasusDateUtil.getPegasusMonthYearDate("01.2011", false));
        Assert.assertEquals("Test fin période", 31, cal.get(Calendar.DAY_OF_MONTH));
        // Test false
        Assert.assertFalse("Test avec valeur fausse, 12 au lieu de 31", cal.get(Calendar.DAY_OF_MONTH) == 12);
    }

    public final void testGetPegasusDateMonthYearNow() throws Exception {

        // !!!!!!!!!!!Attention définir ici la date du jour pour que le test passe
        String dateToTest = "06.2011";
        String dateFromMethod = PegasusDateUtil.getPegasusMonthYearDateNow();
        Assert.assertTrue("Test date now", dateToTest.equals(dateFromMethod));
    }

    public final void testGlobal() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(PegasusDateUtil.getPegasusMonthYearDate("04.2011", true));
        // test jour ok
        Assert.assertEquals("Jour du mois, fomat passé mm.yyyy", 1, cal.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals("mois, fomat passé mm.yyyy", 3, cal.get(Calendar.MONTH));
        Assert.assertEquals("année, fomat passé mm.yy", 2011, cal.get(Calendar.YEAR));

        cal.setTime(PegasusDateUtil.getPegasusMonthYearDate("06.2011", false));
        Assert.assertEquals("Jour du mois, fomat passé mm.yyyy", 30, cal.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals("mois, fomat passé mm.yyyy", 5, cal.get(Calendar.MONTH));
        Assert.assertEquals("année, fomat passé mm.yy", 2011, cal.get(Calendar.YEAR));

    }

    public final void testWithDifferentGlobazDateFormat() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(PegasusDateUtil.getPegasusMonthYearDate("01.11", true));
        // test jour ok
        Assert.assertEquals("Jour du mois, fomat passé mm.yy", 1, cal.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals("mois, fomat passé mm.yy", 0, cal.get(Calendar.MONTH));
        Assert.assertEquals("année, fomat passé mm.yy", 2011, cal.get(Calendar.YEAR));
        // test false
        Assert.assertFalse("Jour du mois, fomat passé mm.yy", cal.get(Calendar.DAY_OF_MONTH) == 3);
        Assert.assertFalse("mois, fomat passé mm.yy", cal.get(Calendar.MONTH) == 4);
        Assert.assertFalse("année, fomat passé mm.yy", cal.get(Calendar.YEAR) == 2001);
    }
}
