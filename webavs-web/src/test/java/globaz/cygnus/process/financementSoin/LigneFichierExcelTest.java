package globaz.cygnus.process.financementSoin;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class LigneFichierExcelTest {

    @Test
    public void testDateDebutErreurFormat() {
        List<RFLigneFichierExcel> listeCasTest = TableauxSimulationFichierExcelTests.getListeAvecErreurDateDebut();
        RFLigneFichierExcel ligneAvecErreurFormatDateDebut = listeCasTest.get(0);

        Assert.assertTrue(ligneAvecErreurFormatDateDebut.getCellulesEnErreur().size() == 3);
        Assert.assertTrue(ligneAvecErreurFormatDateDebut.getCellulesEnErreur().contains(CellulesExcelEnum.DATE_DEBUT));
        Assert.assertTrue(ligneAvecErreurFormatDateDebut.getCellulesEnErreur().contains(CellulesExcelEnum.NB_JOURS));
        Assert.assertTrue(ligneAvecErreurFormatDateDebut.getCellulesEnErreur()
                .contains(CellulesExcelEnum.MONTANT_TOTAL));

    }

    @Test
    public void testDateDebutNull() {
        List<RFLigneFichierExcel> listeCasTest = TableauxSimulationFichierExcelTests.getListeAvecNullDateDebut();
        RFLigneFichierExcel ligneAvecDateDebutNull = listeCasTest.get(0);

        Assert.assertTrue(ligneAvecDateDebutNull.getCellulesEnErreur().size() == 1);
        Assert.assertTrue(ligneAvecDateDebutNull.getCellulesEnErreur().contains(CellulesExcelEnum.LIGNE_INCOMPLETE));

    }

    @Test
    public void testDateDecompteErreurFormat() {
        List<RFLigneFichierExcel> listeCasTest = TableauxSimulationFichierExcelTests.getListeAvecErreurDateDecompte();
        RFLigneFichierExcel ligneavecErreurFormatDateDecompte = listeCasTest.get(0);

        Assert.assertTrue(ligneavecErreurFormatDateDecompte.getCellulesEnErreur().size() == 1);
        Assert.assertTrue(ligneavecErreurFormatDateDecompte.getCellulesEnErreur().contains(
                CellulesExcelEnum.DATE_DECOMPTE));

    }

    @Test
    public void testDateDecompteNull() {
        List<RFLigneFichierExcel> listeCasTest = TableauxSimulationFichierExcelTests.getListeAvecNullDateDecompte();
        RFLigneFichierExcel ligneAvecDateDecompteNull = listeCasTest.get(0);

        Assert.assertTrue(ligneAvecDateDecompteNull.getCellulesEnErreur().size() == 1);
        Assert.assertTrue(ligneAvecDateDecompteNull.getCellulesEnErreur().contains(CellulesExcelEnum.LIGNE_INCOMPLETE));

    }

    @Test
    public void testDateFinErreurFormat() {
        List<RFLigneFichierExcel> listeCasTest = TableauxSimulationFichierExcelTests.getListeAvecErreurDateFin();
        RFLigneFichierExcel ligneAvecErreurFormatDateFin = listeCasTest.get(0);

        Assert.assertTrue(ligneAvecErreurFormatDateFin.getCellulesEnErreur().size() == 3);
        Assert.assertTrue(ligneAvecErreurFormatDateFin.getCellulesEnErreur().contains(CellulesExcelEnum.DATE_FIN));
        Assert.assertTrue(ligneAvecErreurFormatDateFin.getCellulesEnErreur().contains(CellulesExcelEnum.NB_JOURS));
        Assert.assertTrue(ligneAvecErreurFormatDateFin.getCellulesEnErreur().contains(CellulesExcelEnum.MONTANT_TOTAL));

    }

    @Test
    public void testDateFinNull() {
        List<RFLigneFichierExcel> listeCasTest = TableauxSimulationFichierExcelTests.getListeAvecNullDateFin();
        RFLigneFichierExcel ligneAvecDateFinNull = listeCasTest.get(0);

        Assert.assertTrue(ligneAvecDateFinNull.getCellulesEnErreur().size() == 1);
        Assert.assertTrue(ligneAvecDateFinNull.getCellulesEnErreur().contains(CellulesExcelEnum.LIGNE_INCOMPLETE));

    }

    @Test
    public void testFraisJournalierDepassementPlafondJournalier() {
        List<RFLigneFichierExcel> listeCasTest = TableauxSimulationFichierExcelTests
                .getListeAvecDepassementFraisJournalier();
        RFLigneFichierExcel ligneAvecErreurFormatFraisJournalier = listeCasTest.get(0);

        Assert.assertTrue(ligneAvecErreurFormatFraisJournalier.getCellulesEnErreur().size() == 2);
        Assert.assertTrue(ligneAvecErreurFormatFraisJournalier.getCellulesEnErreur().contains(
                CellulesExcelEnum.FRAIS_JOURNALIER));
        Assert.assertTrue(ligneAvecErreurFormatFraisJournalier.getCellulesEnErreur().contains(
                CellulesExcelEnum.MONTANT_TOTAL));
    }

    @Test
    public void testFraisJournalierErreurFormat() {
        List<RFLigneFichierExcel> listeCasTest = TableauxSimulationFichierExcelTests
                .getListeAvecErreurFraisJournalier();
        RFLigneFichierExcel ligneAvecErreurFormatFraisJournalier = listeCasTest.get(0);

        Assert.assertTrue(ligneAvecErreurFormatFraisJournalier.getCellulesEnErreur().size() == 2);
        Assert.assertTrue(ligneAvecErreurFormatFraisJournalier.getCellulesEnErreur().contains(
                CellulesExcelEnum.FRAIS_JOURNALIER));
        Assert.assertTrue(ligneAvecErreurFormatFraisJournalier.getCellulesEnErreur().contains(
                CellulesExcelEnum.MONTANT_TOTAL));
    }

    @Test
    public void testFraisJournalierNull() {
        List<RFLigneFichierExcel> listeCasTest = TableauxSimulationFichierExcelTests.getListeAvecNullFraisJournalier();
        RFLigneFichierExcel ligneAvecFraisJournalierNull = listeCasTest.get(0);

        Assert.assertTrue(ligneAvecFraisJournalierNull.getCellulesEnErreur().size() == 1);
        Assert.assertTrue(ligneAvecFraisJournalierNull.getCellulesEnErreur().contains(
                CellulesExcelEnum.LIGNE_INCOMPLETE));

    }

    @Test
    public void testLignesMultiplesAvecErreurs() {
        for (RFLigneFichierExcel ligne : TableauxSimulationFichierExcelTests.getListeMultipleAvecErreurs()) {
            Assert.assertNotNull(ligne);
        }
    }

    @Test
    public void testLignesMultiplesSansErreurs() {
        for (RFLigneFichierExcel ligne : TableauxSimulationFichierExcelTests.getListeMultipleSansErreurs()) {
            Assert.assertNotNull(ligne);
        }
    }

    @Test
    public void testMontantTotalErreurFormat() {
        List<RFLigneFichierExcel> listeCasTest = TableauxSimulationFichierExcelTests
                .getListeAvecErreurFormatMontantTotal();
        RFLigneFichierExcel ligneAvecErreurFormatMontantTotal = listeCasTest.get(0);

        Assert.assertTrue(ligneAvecErreurFormatMontantTotal.getCellulesEnErreur().size() == 1);
        Assert.assertTrue(ligneAvecErreurFormatMontantTotal.getCellulesEnErreur().contains(
                CellulesExcelEnum.MONTANT_TOTAL));
    }

    @Test
    public void testMontantTotalNull() {
        List<RFLigneFichierExcel> listeCasTest = TableauxSimulationFichierExcelTests.getListeAvecNullMontantTotal();
        RFLigneFichierExcel ligneAvecMontantTotalNull = listeCasTest.get(0);

        Assert.assertTrue(ligneAvecMontantTotalNull.getCellulesEnErreur().size() == 1);
        Assert.assertTrue(ligneAvecMontantTotalNull.getCellulesEnErreur().contains(CellulesExcelEnum.LIGNE_INCOMPLETE));
    }

    @Test
    public void testNbJoursErreurFormat() {
        List<RFLigneFichierExcel> listeCasTest = TableauxSimulationFichierExcelTests.getListeAvecErreurFormatNbJours();
        RFLigneFichierExcel ligneAvecErreurFormatNbJours = listeCasTest.get(0);

        Assert.assertTrue(ligneAvecErreurFormatNbJours.getCellulesEnErreur().size() == 2);
        Assert.assertTrue(ligneAvecErreurFormatNbJours.getCellulesEnErreur().contains(CellulesExcelEnum.NB_JOURS));
        Assert.assertTrue(ligneAvecErreurFormatNbJours.getCellulesEnErreur().contains(CellulesExcelEnum.MONTANT_TOTAL));
    }

    @Test
    public void testNbJoursNull() {
        List<RFLigneFichierExcel> listeCasTest = TableauxSimulationFichierExcelTests.getListeAvecNullNbJours();
        RFLigneFichierExcel ligneAvecNbJoursNull = listeCasTest.get(0);

        Assert.assertTrue(ligneAvecNbJoursNull.getCellulesEnErreur().size() == 1);
        Assert.assertTrue(ligneAvecNbJoursNull.getCellulesEnErreur().contains(CellulesExcelEnum.LIGNE_INCOMPLETE));

    }

    @Test
    public void testNssErreurFormat() {
        List<RFLigneFichierExcel> listeCasTest = TableauxSimulationFichierExcelTests.getListeAvecErreurFormatNss();
        RFLigneFichierExcel ligneAvecErreurFormatNss = listeCasTest.get(0);

        Assert.assertTrue(ligneAvecErreurFormatNss.getCellulesEnErreur().size() == 1);
        Assert.assertTrue(ligneAvecErreurFormatNss.getCellulesEnErreur().contains(CellulesExcelEnum.NUM_NSS));
    }

    @Test
    public void testNssNull() {
        List<RFLigneFichierExcel> listeCasTest = TableauxSimulationFichierExcelTests.getListeAvecNullFormatNss();
        RFLigneFichierExcel ligneAvecNssNull = listeCasTest.get(0);

        Assert.assertTrue(ligneAvecNssNull.getCellulesEnErreur().size() == 1);
        Assert.assertTrue(ligneAvecNssNull.getCellulesEnErreur().contains(CellulesExcelEnum.LIGNE_INCOMPLETE));
    }

}
