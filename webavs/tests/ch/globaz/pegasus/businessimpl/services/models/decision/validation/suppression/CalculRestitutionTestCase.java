package ch.globaz.pegasus.businessimpl.services.models.decision.validation.suppression;

import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

public class CalculRestitutionTestCase {

    private String dateDernierPmt = "09.2013";
    private String dateSuppression = "06.2013";

    @Test
    public void dateIndentiqueSansDateDeFin() {
        CalculRestitution calculRestitution = new CalculRestitution("12.2013", "12.2013");
        calculRestitution.calculeMontantRestitution("12.2013", null, "1000", "0");
        Assert.assertEquals(new BigDecimal(0), calculRestitution.getTotal());
    }

    @Test
    public void periodeSur2ans() {
        CalculRestitution calculRestitution = new CalculRestitution("11.2013", "01.2014");
        calculRestitution.calculeMontantRestitution("12.2013", null, "100", "0");
        Assert.assertEquals(new BigDecimal(200), calculRestitution.getTotal());
    }

    @Test
    public void periodeSur2ansAvecDateDeDebutEnDebutDanne() {
        CalculRestitution calculRestitution = new CalculRestitution("11.2013", "01.2014");
        calculRestitution.calculeMontantRestitution("01.2013", null, "100", "0");
        Assert.assertEquals(new BigDecimal(200), calculRestitution.getTotal());
    }

    @Test
    public void suppressionPourlAnneeDavant() {
        CalculRestitution calculRestitution = new CalculRestitution("12.2012", "12.2013");
        calculRestitution.calculeMontantRestitution("01.2013", "12.2013", "1000", "0");
        Assert.assertEquals(new BigDecimal(12000), calculRestitution.getTotal());
    }

    @Test
    public void supressionPourLafinDunePeriode() {
        CalculRestitution calculRestitution = new CalculRestitution("12.2013", "01.2014");
        calculRestitution.calculeMontantRestitution("01.2013", "12.2013", "1000", "0");
        Assert.assertEquals(new BigDecimal(0), calculRestitution.getTotal());
    }

    @Test
    public void test112() {
        CalculRestitution calculRestitution = new CalculRestitution("12.2013", "01.2014");
        calculRestitution.calculeMontantRestitution("01.2014", null, "1000", "0");
        Assert.assertEquals(new BigDecimal(1000), calculRestitution.getTotal());
    }

    @Test
    public void test1122() {
        CalculRestitution calculRestitution = new CalculRestitution("10.2013", "01.2014");
        calculRestitution.calculeMontantRestitution("01.2013", "12.2013", "1000", "0");
        calculRestitution.calculeMontantRestitution("01.2014", null, "1000", "0");
        Assert.assertEquals(new BigDecimal(3000), calculRestitution.getTotal());
    }

    @Test
    public void testMontantAnnePrecedante() {
        CalculRestitution calculRestitution = new CalculRestitution(dateSuppression, dateDernierPmt);
        BigDecimal montant = calculRestitution.calculeMontantRestitution("01.2012", "10.2012", "100", "0");
        Assert.assertEquals(new BigDecimal(0), montant);
        Assert.assertEquals(new BigDecimal(0), calculRestitution.getTotal());
    }

    @Test
    public void testMontantAvecDateDefin() {
        CalculRestitution calculRestitution = new CalculRestitution(dateSuppression, dateDernierPmt);
        BigDecimal montant = calculRestitution.calculeMontantRestitution("01.2013", "08.2013", "300", "0");
        Assert.assertEquals(new BigDecimal(600), montant);
        Assert.assertEquals(new BigDecimal(600), calculRestitution.getTotal());
    }

    @Test
    public void testMontantAvecDom2R() {
        CalculRestitution calculRestitution = new CalculRestitution(dateSuppression, dateDernierPmt);
        BigDecimal montant = calculRestitution.calculeMontantRestitution("01.2013", null, "300", "300");
        Assert.assertEquals(new BigDecimal(1800), montant);
        Assert.assertEquals(new BigDecimal(1800), calculRestitution.getTotal());
    }

    @Test
    public void testMontantDateDebutPlusGrandQueDernierPmtSansDateDeFin() {
        CalculRestitution calculRestitution = new CalculRestitution(dateSuppression, dateDernierPmt);
        BigDecimal montant = calculRestitution.calculeMontantRestitution("12.2013", null, "300", "0");
        Assert.assertEquals(new BigDecimal(0), montant);
        Assert.assertEquals(new BigDecimal(0), calculRestitution.getTotal());
    }

    @Test
    public void testMontantDateDebutSameHaseDateDernierPmtSansDateDeFin() {
        CalculRestitution calculRestitution = new CalculRestitution("08.2013", "09.2013");
        BigDecimal montant = calculRestitution.calculeMontantRestitution("09.2013", null, "300", "0");
        Assert.assertEquals(new BigDecimal(0), montant);
        Assert.assertEquals(new BigDecimal(0), calculRestitution.getTotal());
    }

    @Test
    public void testMontantDateDebutSameHaseDateSuppressionSansDateDeFin() {
        CalculRestitution calculRestitution = new CalculRestitution(dateSuppression, dateDernierPmt);
        BigDecimal montant = calculRestitution.calculeMontantRestitution(dateSuppression, null, "300", "0");
        Assert.assertEquals(new BigDecimal(900), montant);
        Assert.assertEquals(new BigDecimal(900), calculRestitution.getTotal());
    }

    @Test
    public void testMontantDateFinSameHaseDateProchainPmtSansDateDeFin() {
        CalculRestitution calculRestitution = new CalculRestitution(dateSuppression, dateDernierPmt);
        BigDecimal montant = calculRestitution.calculeMontantRestitution("01.2013", dateDernierPmt, "300", "0");
        Assert.assertEquals(new BigDecimal(900), montant);
        Assert.assertEquals(new BigDecimal(900), calculRestitution.getTotal());
    }

    @Test
    public void testMontantDateFinSameHaseDateSuppressionSansDateDeFin() {
        CalculRestitution calculRestitution = new CalculRestitution("06.2013", "09.2013");
        BigDecimal montant = calculRestitution.calculeMontantRestitution("01.2013", "06.2013", "300", "0");
        Assert.assertEquals(new BigDecimal(0), montant);
        Assert.assertEquals(new BigDecimal(0), calculRestitution.getTotal());
    }

    @Test
    public void testMontantSansDateDeFin() {
        CalculRestitution calculRestitution = new CalculRestitution(dateSuppression, dateDernierPmt);
        BigDecimal montant = calculRestitution.calculeMontantRestitution("01.2013", null, "300", "0");
        Assert.assertEquals(new BigDecimal(900), montant);
        Assert.assertEquals(new BigDecimal(900), calculRestitution.getTotal());
    }

    @Test
    public void testMontantSansDateDeFinAvecDateDeSuppressionPlusPetitQueDateDeDebut() {
        CalculRestitution calculRestitution = new CalculRestitution("09.2012", dateDernierPmt);
        BigDecimal montant = calculRestitution.calculeMontantRestitution("06.2013", null, "100", "");
        Assert.assertEquals(new BigDecimal(400), montant);
        Assert.assertEquals(new BigDecimal(400), calculRestitution.getTotal());
    }

    @Test
    public void testMontantTotal() {
        CalculRestitution calculRestitution = new CalculRestitution("06.2013", "09.2013");
        calculRestitution.calculeMontantRestitution("01.2012", "10.2012", "100", "0");
        calculRestitution.calculeMontantRestitution("11.2012", "12.2012", "200", "0");
        calculRestitution.calculeMontantRestitution("01.2013", null, "300", "0");
        Assert.assertEquals(new BigDecimal(900), calculRestitution.getTotal());
    }

    @Test
    public void testMontantTotal2() {
        CalculRestitution calculRestitution = new CalculRestitution("06.2012", "09.2013");
        calculRestitution.calculeMontantRestitution("01.2012", "10.2012", "100", "0"); // 400
        calculRestitution.calculeMontantRestitution("11.2012", "12.2012", "200", "0"); // 400
        calculRestitution.calculeMontantRestitution("01.2013", null, "300", "0"); // 2700
        Assert.assertEquals(new BigDecimal(3500), calculRestitution.getTotal());
    }

    @Test
    public void testSurUneAnneAvecSupressionEnFinDannee() {
        CalculRestitution calculRestitution = new CalculRestitution("12.2013", "12.2013");
        calculRestitution.calculeMontantRestitution("01.2013", "12.2013", "1000", "0");
        Assert.assertEquals(new BigDecimal(0), calculRestitution.getTotal());
    }

    @Test
    public void unMoisDeRestitution() {
        CalculRestitution calculRestitution = new CalculRestitution("11.2013", "12.2013");
        calculRestitution.calculeMontantRestitution("01.2013", null, "1000", "0");
        Assert.assertEquals(new BigDecimal(1000), calculRestitution.getTotal());
    }

}
