package ch.globaz.corvus.utils.rentesverseesatort;

import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.common.domaine.Periode;

public class RELigneDetailCalculRenteVerseeATortTest {

    @Test
    public void testCreationAvecValeursInvalides() throws Exception {
        try {
            new RELigneDetailCalculRenteVerseeATort(BigDecimal.ZERO, new Periode("01.01.2010", "01.02.2010"));
            Assert.fail();
        } catch (Exception e) {
            // ok
        }
        try {
            new RELigneDetailCalculRenteVerseeATort(new BigDecimal(-10.0), new Periode("01.2010", "02.2010"));
            Assert.fail();
        } catch (Exception e) {
            // ok
        }
    }

    @Test
    public void testCreationAvecValeursNull() throws Exception {
        try {
            new RELigneDetailCalculRenteVerseeATort(null, null);
            Assert.fail();
        } catch (Exception e) {
            // ok
        }
        try {
            new RELigneDetailCalculRenteVerseeATort(null, new Periode("01.2010", "02.2010"));
            Assert.fail();
        } catch (Exception e) {
            // ok
        }
        try {
            new RELigneDetailCalculRenteVerseeATort(new BigDecimal("10.00"), null);
            Assert.fail();
        } catch (Exception e) {
            // ok
        }
    }

    @Test
    public void testGetMontantTotalPourLaPeriode() throws Exception {

        RELigneDetailCalculRenteVerseeATort ligneDetail = new RELigneDetailCalculRenteVerseeATort(new BigDecimal(
                "10.00"), new Periode("01.2013", "05.2013"));
        Assert.assertEquals(new BigDecimal("50.00"), ligneDetail.getMontantTotalPourLaPeriode());
    }

    @Test
    public void testGetMontantTotalPourLaPeriodeAvecPeriodeSansDateDeFin() throws Exception {
        try {
            new RELigneDetailCalculRenteVerseeATort(new BigDecimal("33.00"), new Periode("01.2013", ""));
            Assert.fail("ne dois pas accepter de calculer le montant sans date de fin");
        } catch (Exception ex) {
            // ok
        }
    }
}
