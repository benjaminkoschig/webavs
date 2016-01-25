package globaz.corvus.itext;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class REListeImpotSourceTest extends TestCase {

    private REListeImpotSource testInstance;

    @Override
    @Before
    public void setUp() throws Exception {
        testInstance = new REListeImpotSource();
    }

    @Test
    public void testFormatDateIfNecessary() {

        String falseDate = "012.32.15";
        Assert.assertNull(testInstance.formatDateIfNecessary(falseDate));

        String monthDate = "03.1956";
        Assert.assertEquals("01.03.1956", testInstance.formatDateIfNecessary(monthDate));

        String completeDate = "13.04.2043";
        Assert.assertEquals(completeDate, testInstance.formatDateIfNecessary(completeDate));
    }

    @Test
    public void testValiderDates() throws Exception {

        // teste des limites pour moisDébut
        // date valide pour moisFin
        testInstance.setMoisFin("03.2011");

        // 12.1999 : date invalide
        testInstance.setMoisDebut("12.1999");
        Assert.assertFalse("Erreur : la plage de date [12.1999 - 03.2011] a été considéré comme valide",
                testInstance.validerDates());

        // 01.2000 : date valide
        testInstance.setMoisDebut("01.2000");
        Assert.assertTrue("Erreur : la plage de date [01.2000 - 03.2011] a été considéré comme invalide",
                testInstance.validerDates());

        // teste des limites pour moisFin

        // 31.2099 : date valide
        testInstance.setMoisFin("12.2099");
        Assert.assertTrue("Erreur : la plage de date [01.2000 - 12.2099] a été considéré comme invalide",
                testInstance.validerDates());

        // 01.2100 : date invalide
        testInstance.setMoisFin("01.2100");
        Assert.assertFalse("Erreur : la plage de date [01.2000 - 01.2100] a été considéré comme valide",
                testInstance.validerDates());

        // teste écart entre début et fin
        testInstance.setMoisDebut("04.2011");
        testInstance.setMoisFin("03.2011");
        Assert.assertFalse("Erreur : la plage de date [04.2011 - 03.2011] a été considéré comme valide",
                testInstance.validerDates());

    }

    @Test
    public void testValiderPlageDate() {
        // 12.1999 : date invalide
        Assert.assertFalse("Erreur : le mois 12.1999 a été considéré comme valide",
                testInstance.validerPlageDate("12.1999"));

        // 01.2000 : date valide
        Assert.assertTrue("Erreur : le mois 01.2000 a été considéré comme invalide",
                testInstance.validerPlageDate("01.2000"));

        // 31.2099 : date valide
        Assert.assertTrue("Erreur : le mois 12.2099 a été considéré comme invalide",
                testInstance.validerPlageDate("12.2099"));

        // 01.2100 : date invalide
        Assert.assertFalse("Erreur : le mois 01.2100 a été considéré comme valide",
                testInstance.validerPlageDate("01.2100"));

    }
}
