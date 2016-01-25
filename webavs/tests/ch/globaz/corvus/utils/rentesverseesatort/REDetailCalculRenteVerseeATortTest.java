package ch.globaz.corvus.utils.rentesverseesatort;

import java.math.BigDecimal;
import java.util.Arrays;
import junit.framework.Assert;
import org.junit.Test;
import ch.globaz.common.domaine.Periode;
import ch.globaz.corvus.domaine.constantes.TypeRenteVerseeATort;
import ch.globaz.pyxis.domaine.NumeroSecuriteSociale;

public class REDetailCalculRenteVerseeATortTest {

    @Test
    public void testCreationAvecValeursInvalides() {
        try {
            new REDetailCalculRenteVerseeATort(null, null, null, null, null, null, null, null, null, null, null, null,
                    null, null);
            Assert.fail();
        } catch (Exception e) {
            // ok
        }

        try {
            // l'id rente versée à tort peut être null lors du 1er calcul (pas encore sauvée en base)
            new REDetailCalculRenteVerseeATort(null, 0l, 0l, 0l, Arrays.asList(new RELigneDetailCalculRenteVerseeATort(
                    BigDecimal.ONE, new Periode("01.2013", "01.2013"))), 0l, new NumeroSecuriteSociale(
                    "756.1234.5678.97"), "", "", "", "", "", "", TypeRenteVerseeATort.PRESTATION_TOUCHEE_INDUMENT);
        } catch (Exception e) {
            Assert.fail();
        }

        try {
            // l'id rente nouveau droit peut être null dans le cas d'un trou dans le nouveau droit (couvert par
            // l'ancien)
            new REDetailCalculRenteVerseeATort(0l, null, 0l, 0l, Arrays.asList(new RELigneDetailCalculRenteVerseeATort(
                    BigDecimal.ONE, new Periode("01.2013", "01.2013"))), 0l, new NumeroSecuriteSociale(
                    "756.1234.5678.97"), "", "", "", "", "", "", TypeRenteVerseeATort.PRESTATION_TOUCHEE_INDUMENT);
        } catch (Exception e) {
            Assert.fail();
        }

        try {
            new REDetailCalculRenteVerseeATort(0l, 0l, null, 0l, Arrays.asList(new RELigneDetailCalculRenteVerseeATort(
                    BigDecimal.ONE, new Periode("01.2013", "01.2013"))), 0l, new NumeroSecuriteSociale(
                    "756.1234.5678.97"), "", "", "", "", "", "", TypeRenteVerseeATort.PRESTATION_TOUCHEE_INDUMENT);
        } catch (Exception e) {
            Assert.fail();
        }

        try {
            new REDetailCalculRenteVerseeATort(0l, 0l, 0l, null, Arrays.asList(new RELigneDetailCalculRenteVerseeATort(
                    BigDecimal.ONE, new Periode("01.2013", "01.2013"))), 0l, new NumeroSecuriteSociale(
                    "756.1234.5678.97"), "", "", "", "", "", "", TypeRenteVerseeATort.PRESTATION_TOUCHEE_INDUMENT);
            Assert.fail();
        } catch (Exception e) {
            // ok
        }

        try {
            new REDetailCalculRenteVerseeATort(0l, 0l, 0l, 0l, null, 0l, new NumeroSecuriteSociale("756.1234.5678.97"),
                    "", "", "", "", "", "", TypeRenteVerseeATort.PRESTATION_TOUCHEE_INDUMENT);
            Assert.fail();
        } catch (Exception e) {
            // ok
        }

        try {
            new REDetailCalculRenteVerseeATort(0l, 0l, 0l, 0l, Arrays.asList(new RELigneDetailCalculRenteVerseeATort(
                    BigDecimal.ONE, new Periode("01.2013", "01.2013"))), null, new NumeroSecuriteSociale(
                    "756.1234.5678.97"), "", "", "", "", "", "", TypeRenteVerseeATort.PRESTATION_TOUCHEE_INDUMENT);
            Assert.fail();
        } catch (Exception e) {
            // ok
        }

        try {
            new REDetailCalculRenteVerseeATort(0l, 0l, 0l, 0l, Arrays.asList(new RELigneDetailCalculRenteVerseeATort(
                    BigDecimal.ONE, new Periode("01.2013", "01.2013"))), 0l, null, "", "", "", "", "", "",
                    TypeRenteVerseeATort.PRESTATION_TOUCHEE_INDUMENT);
            Assert.fail();
        } catch (Exception e) {
            // ok
        }

        try {
            new REDetailCalculRenteVerseeATort(0l, 0l, 0l, 0l, Arrays.asList(new RELigneDetailCalculRenteVerseeATort(
                    BigDecimal.ONE, new Periode("01.2013", "01.2013"))), 0l, new NumeroSecuriteSociale(
                    "756.1234.5678.97"), null, "", "", "", "", "", TypeRenteVerseeATort.PRESTATION_TOUCHEE_INDUMENT);
            Assert.fail();
        } catch (Exception e) {
            // ok
        }

        try {
            new REDetailCalculRenteVerseeATort(0l, 0l, 0l, 0l, Arrays.asList(new RELigneDetailCalculRenteVerseeATort(
                    BigDecimal.ONE, new Periode("01.2013", "01.2013"))), 0l, new NumeroSecuriteSociale(
                    "756.1234.5678.97"), "", null, "", "", "", "", TypeRenteVerseeATort.PRESTATION_TOUCHEE_INDUMENT);
            Assert.fail();
        } catch (Exception e) {
            // ok
        }

        try {
            new REDetailCalculRenteVerseeATort(0l, 0l, 0l, 0l, Arrays.asList(new RELigneDetailCalculRenteVerseeATort(
                    BigDecimal.ONE, new Periode("01.2013", "01.2013"))), 0l, new NumeroSecuriteSociale(
                    "756.1234.5678.97"), "", "", null, "", "", "", TypeRenteVerseeATort.PRESTATION_TOUCHEE_INDUMENT);
            Assert.fail();
        } catch (Exception e) {
            // ok
        }

        try {
            new REDetailCalculRenteVerseeATort(0l, 0l, 0l, 0l, Arrays.asList(new RELigneDetailCalculRenteVerseeATort(
                    BigDecimal.ONE, new Periode("01.2013", "01.2013"))), 0l, new NumeroSecuriteSociale(
                    "756.1234.5678.97"), "", "", "", null, "", "", TypeRenteVerseeATort.PRESTATION_TOUCHEE_INDUMENT);
            Assert.fail();
        } catch (Exception e) {
            // ok
        }

        try {
            new REDetailCalculRenteVerseeATort(0l, 0l, 0l, 0l, Arrays.asList(new RELigneDetailCalculRenteVerseeATort(
                    BigDecimal.ONE, new Periode("01.2013", "01.2013"))), 0l, new NumeroSecuriteSociale(
                    "756.1234.5678.97"), "", "", "", "", null, "", TypeRenteVerseeATort.PRESTATION_TOUCHEE_INDUMENT);
            Assert.fail();
        } catch (Exception e) {
            // ok
        }

        try {
            new REDetailCalculRenteVerseeATort(0l, 0l, 0l, 0l, Arrays.asList(new RELigneDetailCalculRenteVerseeATort(
                    BigDecimal.ONE, new Periode("01.2013", "01.2013"))), 0l, new NumeroSecuriteSociale(
                    "756.1234.5678.97"), "", "", "", "", "", null, TypeRenteVerseeATort.PRESTATION_TOUCHEE_INDUMENT);
            Assert.fail();
        } catch (Exception e) {
            // ok
        }

        try {
            new REDetailCalculRenteVerseeATort(0l, 0l, 0l, 0l, Arrays.asList(new RELigneDetailCalculRenteVerseeATort(
                    BigDecimal.ONE, new Periode("01.2013", "01.2013"))), 0l, new NumeroSecuriteSociale(
                    "756.1234.5678.97"), "", "", "", "", "", "", null);
            Assert.fail();
        } catch (Exception e) {
            // ok
        }
    }

    public void testGetMontantTotalVerseeATortAvecPlusieursPeriodes() throws Exception {
        // 10.- * 3 mois : 30.-
        // 7.- * 3 mois : 21.-
        // 9.- * 2 mois : 18.-
        // total : 69.-
        REDetailCalculRenteVerseeATort detail = new REDetailCalculRenteVerseeATort(0l, 0l, 0l, 0l, Arrays.asList(
                new RELigneDetailCalculRenteVerseeATort(new BigDecimal(10.0), new Periode("01.2013", "03.2013")),
                new RELigneDetailCalculRenteVerseeATort(new BigDecimal(7.0), new Periode("04.2013", "06.2013")),
                new RELigneDetailCalculRenteVerseeATort(new BigDecimal(9.0), new Periode("07.2013", "08.2013"))), 0l,
                new NumeroSecuriteSociale("756.1234.5678.97"), "", "", "", "", null, null,
                TypeRenteVerseeATort.PRESTATION_TOUCHEE_INDUMENT);

        Assert.assertEquals(new BigDecimal(69.0), detail.getMontantTotalVerseeATort());
    }

    @Test
    public void testGetMontantTotalVerseeATortAvecUneSeulePeriode() throws Exception {
        REDetailCalculRenteVerseeATort detail = new REDetailCalculRenteVerseeATort(0l, 0l, 0l, 0l,
                Arrays.asList(new RELigneDetailCalculRenteVerseeATort(new BigDecimal(50.0), new Periode("01.2013",
                        "06.2013"))), 0l, new NumeroSecuriteSociale("756.1234.5678.97"), "", "", "", "", "", "",
                TypeRenteVerseeATort.PRESTATION_TOUCHEE_INDUMENT);
        Assert.assertEquals(new BigDecimal("300.00"), detail.getMontantTotalVerseeATort());
    }

}
