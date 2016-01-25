package ch.globaz.pegasus.testjade;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import junit.framework.Assert;
import ch.globaz.hera.business.vo.famille.MembreFamilleVO;

public class JadeTestCase {

    private boolean isAlive(MembreFamilleVO membreFamille, String dateToCompare) {
        String dateNaiss = membreFamille.getDateNaissance();
        String dateDeces = membreFamille.getDateDeces();

        return ((JadeDateUtil.isDateBefore(dateNaiss, dateToCompare) || JadeDateUtil.areDatesEquals(dateNaiss,
                dateToCompare)) && (JadeStringUtil.isEmpty(dateDeces) || JadeDateUtil.isDateAfter(dateDeces,
                dateToCompare)));

    }

    private boolean isAlive(String dateNaiss, String dateDeces, String dateToCompare) {

        return ((JadeDateUtil.isDateBefore(dateNaiss, dateToCompare) || JadeDateUtil.areDatesEquals(dateNaiss,
                dateToCompare)) && (JadeStringUtil.isEmpty(dateDeces) || JadeDateUtil.isDateAfter(dateDeces,
                dateToCompare)));
    }

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
    // JadeThread.rollbackSession();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // super.tearDown();
    // }

    public final void testDateAfter() throws Exception {
        Assert.assertEquals(false, JadeDateUtil.isDateAfter("11.11.1990", "12.12.2000"));
        Assert.assertEquals(false, JadeDateUtil.isDateAfter("11.11.1990", "12.12.1990"));
        Assert.assertEquals(true, JadeDateUtil.isDateAfter("11.11.1990", "01.01.1990"));

    }

    public final void testDateBefore() throws Exception {
        Assert.assertEquals(true, JadeDateUtil.isDateBefore("10.10.1990", "10.10.2000"));
        Assert.assertEquals(false, JadeDateUtil.isDateBefore("10.10.1990", "10.10.1990"));
    }

    public final void testIsAlive() throws Exception {
        Assert.assertEquals(true, this.isAlive("01.01.1982", "", "01.12.2010"));
        Assert.assertEquals(true, this.isAlive("01.01.1982", null, "01.12.2010"));
        Assert.assertEquals(true, this.isAlive("01.12.2010", "", "01.12.2010"));

        Assert.assertEquals(true, this.isAlive("01.01.1952", "05.03.2011", "01.12.2010"));

        Assert.assertEquals(false, this.isAlive("01.01.1952", "22.12.2000", "01.12.2010"));
        Assert.assertEquals(false, this.isAlive("01.01.1952", "01.12.2010", "01.12.2010"));

    }

}