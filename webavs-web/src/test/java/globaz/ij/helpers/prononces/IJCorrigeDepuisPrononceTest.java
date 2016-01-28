package globaz.ij.helpers.prononces;

import java.util.ArrayList;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

public class IJCorrigeDepuisPrononceTest {

    @Parameters
    public static ArrayList<String> getDateOk() {

        ArrayList<String> params = new ArrayList<String>();
        params.add("28.02.2013");
        params.add("01.03.2013");
        params.add("11.02.2013");
        params.add("23.02.2013");
        params.add("01.02.2013");
        params.add("12.02.2013");
        params.add("01.12.2013");
        params.add("01.02.2013");
        params.add("01.12.2013");
        params.add("01.12.5999");

        return params;
    }

    /**
     * Test CheckArgument avec un mauvais format
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckArgumentWithBadFormat() {
        String dateBad = "2323";
        new IJCorrigerDepuisPrononce().checkArgument(dateBad);
        Assert.fail("Exception non généré");
    }

    /**
     * Test CheckArgument avec un mauvais format et comparaison du message d'erreur
     */
    @Test
    public void testCheckArgumentWithBadFormatErrorMessage() {
        String dateBad = "2323";

        String messageToTest = "La date n'est pas dans le bon format . [" + dateBad
                + "] Le format est dd.MM.yyyy. Voir class [IJCorrigerDepuisPrononce]";

        try {
            new IJCorrigerDepuisPrononce().checkArgument(dateBad);
            Assert.fail("Exception non généré");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(messageToTest, e.getMessage());
        }
    }

    /**
     * Test CheckArgument avec le bon format
     */
    @Test
    public void testCheckArgumentWithDateOk() {
        String dateOK = "01.01.2012";
        new IJCorrigerDepuisPrononce().checkArgument(dateOK);
        Assert.assertTrue(true);
    }

    /**
     * Test CheckArgument avec une liste contenant de bons format de date
     */
    @Test
    public void testCheckArgumentWithListDateOk() {

        for (String dateOk : IJCorrigeDepuisPrononceTest.getDateOk()) {
            new IJCorrigerDepuisPrononce().checkArgument(dateOk);
            Assert.assertTrue(true);
        }
    }

    /**
     * Test CheckArgument avec une valeur null
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckArgumentWithNull() {
        String dateNull = null;
        new IJCorrigerDepuisPrononce().checkArgument(dateNull);
        Assert.fail("Exception non généré");
    }

    /**
     * Test CheckArgument avec un mauvais format
     */
    @Test
    public void testCheckArgumentWithNullErrorMessage() {
        String dateNull = null;
        String messageToTest = "La date est à null. Voir class [IJCorrigerDepuisPrononce]";

        try {
            new IJCorrigerDepuisPrononce().checkArgument(dateNull);
            Assert.fail("Exception non généré");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(messageToTest, e.getMessage());
        }
    }

    /**
     * Test Composer Message Erreur
     * 
     * @throws Exception
     */
    @Test
    public void testComposerMessageErreur() throws Exception {
        String messageToTest = "La date de correction ({0}) ne correspond pas à la période de validité du prononcé ({1} - {2}).";

        IJCorrigerDepuisPrononce corrigerDepuisPrononce = new IJCorrigerDepuisPrononce();

        if (corrigerDepuisPrononce.verifierDateCorrectionDansPrononce("01.05.2013", "01.06.2013", "01.12.2013")) {
            Assert.fail("La date est valide, donc le message n'est pas généré");
        } else {
            // FIXME sce pour que cela compile
            // Assert.assertEquals(
            // corrigerDepuisPrononce.composerMessageErreur(corrigerDepuisPrononce.getTypeErreur(), messageToTest),
            // "La date de correction (01.05.2013) ne correspond pas à la période de validité du prononcé (01.06.2013 - 01.12.2013).");
        }
    }

    /**
     * Test Date avant le début du prononcé
     */
    @Test
    public void testVerifierDateCorrectionDansPrononceCasDateCorrectionApresDebut() throws IllegalArgumentException,
            Exception {

        String dateCorrection = "10.08.2013";
        String dateDebutPrononce = "01.07.2013";
        String dateFinPrononce = "31.07.2013";

        boolean resultat = new IJCorrigerDepuisPrononce().verifierDateCorrectionDansPrononce(dateCorrection,
                dateDebutPrononce, dateFinPrononce);

        Assert.assertFalse(resultat);
    }

    /**
     * Test Date avant le début du prononcé
     */
    @Test
    public void testVerifierDateCorrectionDansPrononceCasDateCorrectionAvantDebut() throws IllegalArgumentException,
            Exception {

        String dateCorrection = "10.06.2013";
        String dateDebutPrononce = "01.07.2013";
        String dateFinPrononce = "31.07.2013";

        boolean resultat = new IJCorrigerDepuisPrononce().verifierDateCorrectionDansPrononce(dateCorrection,
                dateDebutPrononce, dateFinPrononce);

        Assert.assertFalse(resultat);
    }

    /**
     * Test Date dans prononcé
     */
    @Test
    public void testVerifierDateCorrectionDansPrononceCasStandard() throws IllegalArgumentException, Exception {

        String dateCorrection = "10.07.2013";
        String dateDebutPrononce = "01.07.2013";
        String dateFinPrononce = "31.07.2013";

        boolean resultat = new IJCorrigerDepuisPrononce().verifierDateCorrectionDansPrononce(dateCorrection,
                dateDebutPrononce, dateFinPrononce);

        Assert.assertTrue(resultat);
    }
}
