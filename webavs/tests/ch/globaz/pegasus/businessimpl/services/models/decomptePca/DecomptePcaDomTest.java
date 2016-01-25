package ch.globaz.pegasus.businessimpl.services.models.decomptePca;

import java.util.ArrayList;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.pegasus.business.exceptions.models.decompte.DecompteException;
import ch.globaz.pegasus.business.vo.pcaccordee.PcaDecompte;
import ch.globaz.pegasus.businessimpl.tests.factory.PegasusTestFactory;

public class DecomptePcaDomTest {

    @Ignore
    @Test
    public void domCorrectionDroitAvecUnePeriode() throws DecompteException {
        PcaDecompte pca = PegasusTestFactory.createPcaRequerantAvs("03.2013", null, "450");
        List<PcaDecompte> pcas = new ArrayList<PcaDecompte>();
        pcas.add(pca);

        PcaDecompte pcaR = PegasusTestFactory.createPcaRequerantAvs("01.2013", null, "200");
        List<PcaDecompte> pcaReplaceds = new ArrayList<PcaDecompte>();
        pcaReplaceds.add(pcaR);

        // DecomptePca decomptePca = this.genearateDecompte(pcas, pcaReplaceds);

        // Assert.assertEquals("Test date de début", "03.2013", decomptePca.getDateDebut());
        // Assert.assertNull("Test date de fin", decomptePca.getDateFin());
        //
        // PeriodePca periode = decomptePca.getPeriodesPca().get(0);
        //
        // Assert.assertEquals("Test de le nombre de période", 1, decomptePca.getPeriodesPca().size());
        // Assert.assertEquals("Nombre de mois dans la 1er période", 3, periode.getNbMont());
        //
        // Assert.assertEquals("La premièer periode doit seulement avoir le requerant", pca,
        // periode.getPcaRequerantNew());
        // Assert.assertEquals("La premièer periode(old) du le requerant", pcaR, periode.getPcaRequeranReplaced());
        //
        // Assert.assertNull(periode.getPcaConjointNew());
        // Assert.assertNull(periode.getPcaConjointReplaced());
        // Assert.assertEquals("750", decomptePca.getMontant().toString());
    }

    @Ignore
    @Test
    public void domCorrectionDroitPlusieursPeriode() throws DecompteException {

        List<PcaDecompte> pcas = new ArrayList<PcaDecompte>();
        pcas.add(PegasusTestFactory.createPcaRequerantAvs("05.2010", "12.2010", "400"));
        pcas.add(PegasusTestFactory.createPcaRequerantAvs("01.2011", "12.2011", "450"));
        pcas.add(PegasusTestFactory.createPcaRequerantAvs("01.2012", "12.2012", "460"));
        pcas.add(PegasusTestFactory.createPcaRequerantAvs("01.2013", "04.2013", "480"));
        pcas.add(PegasusTestFactory.createPcaRequerantAvs("05.2013", null, "500"));

        List<PcaDecompte> pcaReplaceds = new ArrayList<PcaDecompte>();
        pcaReplaceds.add(PegasusTestFactory.createPcaRequerantAvs("01.2010", "05.2010", "200"));
        pcaReplaceds.add(PegasusTestFactory.createPcaRequerantAvs("06.2010", "12.2010", "200"));
        pcaReplaceds.add(PegasusTestFactory.createPcaRequerantAvs("01.2011", "12.2011", "200"));
        pcaReplaceds.add(PegasusTestFactory.createPcaRequerantAvs("01.2012", "12.2012", "200"));
        pcaReplaceds.add(PegasusTestFactory.createPcaRequerantAvs("01.2013", null, "200"));

        // DecomptePca decomptePca = this.genearateDecompte(pcas, pcaReplaceds);
        //
        // Assert.assertEquals("Test date de début", "05.2010", decomptePca.getDateDebut());
        // Assert.assertNull("Test date de fin", decomptePca.getDateFin());
        // Assert.assertEquals("Test de le nombre de période", 6, decomptePca.getPeriodesPca().size());
        //
        // PeriodePca periode = decomptePca.getPeriodesPca().get(0);
        // Assert.assertEquals("Nombre de mois dans la 1er période (05.2010 - 05.2010)", 1, periode.getNbMont());
        // Assert.assertEquals("1er periode requerant", pcas.get(0), periode.getPcaRequerantNew());
        // Assert.assertEquals("1er periode(old) requerant", pcaReplaceds.get(0), periode.getPcaRequeranReplaced());
        //
        // PeriodePca periode2 = decomptePca.getPeriodesPca().get(1);
        // Assert.assertEquals("Nombre de mois dans la 2eme période (06.2010 - 12.2010)", 7, periode2.getNbMont());
        // Assert.assertEquals("2eme periode requerant", pcas.get(0), periode2.getPcaRequerantNew());
        // Assert.assertEquals("2eme periode(old) requerant", pcaReplaceds.get(1), periode2.getPcaRequeranReplaced());
        //
        // PeriodePca periode3 = decomptePca.getPeriodesPca().get(2);
        // Assert.assertEquals("Nombre de mois dans la 3eme période (01.2011 - 12.2011)", 12, periode3.getNbMont());
        // Assert.assertEquals("3eme periode requerant", pcas.get(1), periode3.getPcaRequerantNew());
        // Assert.assertEquals("3eme periode(old) requerant", pcaReplaceds.get(2), periode3.getPcaRequeranReplaced());
        //
        // PeriodePca periode4 = decomptePca.getPeriodesPca().get(3);
        // Assert.assertEquals("Nombre de mois dans la 4eme période (01.2012 - 12.2012)", 12, periode4.getNbMont());
        // Assert.assertEquals("4eme periode requerant", pcas.get(2), periode4.getPcaRequerantNew());
        // Assert.assertEquals("4eme periode(old) requerant", pcaReplaceds.get(3), periode4.getPcaRequeranReplaced());
        //
        // PeriodePca periode5 = decomptePca.getPeriodesPca().get(4);
        // Assert.assertEquals("Nombre de mois dans la 5eme période (01.2013 - 04.2013)", 4, periode5.getNbMont());
        // Assert.assertEquals("5eme periode requerant", pcas.get(3), periode5.getPcaRequerantNew());
        // Assert.assertEquals("5eme periode(old) requerant", pcaReplaceds.get(4), periode5.getPcaRequeranReplaced());
        //
        // PeriodePca periode6 = decomptePca.getPeriodesPca().get(5);
        // Assert.assertEquals("Nombre de mois dans la 6eme période (05.2013 - )", 1, periode6.getNbMont());
        // Assert.assertEquals("6eme periode requerant", pcas.get(4), periode6.getPcaRequerantNew());
        // Assert.assertEquals("6eme periode(old) requerant", pcaReplaceds.get(4), periode6.getPcaRequeranReplaced());
        //
        // Assert.assertNull(periode.getPcaConjointNew());
        // Assert.assertNull(periode.getPcaConjointReplaced());
        //
        // Assert.assertEquals("9140", decomptePca.getMontant().toString());
    }

    @Ignore
    @Test
    public void domInitialAvecPlusieursPeriode() throws DecompteException {
        PcaDecompte pca1 = PegasusTestFactory.createPcaRequerantAvs("01.2012", "05.2012", "220");
        PcaDecompte pca2 = PegasusTestFactory.createPcaRequerantAvs("06.2012", "12.2012", "700");
        PcaDecompte pca3 = PegasusTestFactory.createPcaRequerantAvs("01.2013", null, "450");
        List<PcaDecompte> pcas = new ArrayList<PcaDecompte>();
        pcas.add(pca1);
        pcas.add(pca2);
        pcas.add(pca3);
        // DecomptePca decomptePca = this.genearateDecompte(pcas);
        //
        // Assert.assertEquals("Test date de début", "01.2012", decomptePca.getDateDebut());
        // Assert.assertNull("Test date de fin", decomptePca.getDateFin());
        // Assert.assertEquals("Test de le nombre de période", 3, decomptePca.getPeriodesPca().size());
        //
        // PeriodePca periode1 = decomptePca.getPeriodesPca().get(0);
        // Assert.assertEquals("Nombre de mois dans la 1er période", 5, periode1.getNbMont());
        // Assert.assertEquals("La premièer periode doit seulement avoir le requerant", pca1,
        // periode1.getPcaRequerantNew());
        // Assert.assertNull(periode1.getPcaConjointNew());
        // Assert.assertNull(periode1.getPcaConjointReplaced());
        // Assert.assertNull(periode1.getPcaRequeranReplaced());
        //
        // PeriodePca periode2 = decomptePca.getPeriodesPca().get(1);
        // Assert.assertEquals("Nombre de mois dans la 2eme période", 7, periode2.getNbMont());
        // Assert.assertEquals("La deuxième periode doit seulement avoir le requerant", pca2,
        // periode2.getPcaRequerantNew());
        // Assert.assertNull(periode1.getPcaConjointNew());
        // Assert.assertNull(periode1.getPcaConjointReplaced());
        // Assert.assertNull(periode1.getPcaRequeranReplaced());
        //
        // PeriodePca periode3 = decomptePca.getPeriodesPca().get(2);
        // Assert.assertEquals("Nombre de mois dans la 3eme période", 5, periode3.getNbMont());
        // Assert.assertEquals("La troisième periode doit seulement avoir le requerant", pca3,
        // periode3.getPcaRequerantNew());
        // Assert.assertNull(periode1.getPcaConjointNew());
        // Assert.assertNull(periode1.getPcaConjointReplaced());
        // Assert.assertNull(periode1.getPcaRequeranReplaced());
        //
        // Assert.assertEquals("8250", decomptePca.getMontant().toString());

    }

    @Ignore
    @Test
    public void domIntialAvecUnePeriode() throws DecompteException {
        PcaDecompte pca = PegasusTestFactory.createPcaRequerantAvs("01.2013", null, "450");
        List<PcaDecompte> pcas = new ArrayList<PcaDecompte>();
        pcas.add(pca);
        // DecomptePca decomptePca = this.genearateDecompte(pcas);
        //
        // Assert.assertEquals("Test date de début", "01.2013", decomptePca.getDateDebut());
        // Assert.assertNull("Test date de fin", decomptePca.getDateFin());
        // PeriodePca periode = decomptePca.getPeriodesPca().get(0);
        //
        // Assert.assertEquals("Test de le nombre de période", 1, decomptePca.getPeriodesPca().size());
        // Assert.assertEquals("La premièer periode doit seulement avoir le requerant", pca,
        // periode.getPcaRequerantNew());
        // Assert.assertNull(periode.getPcaConjointNew());
        // Assert.assertNull(periode.getPcaConjointReplaced());
        // Assert.assertNull(periode.getPcaRequeranReplaced());
        // Assert.assertEquals("2250", decomptePca.getMontant().toString());

    }

    @Ignore
    @Test
    public void domToSepMal() throws DecompteException {
        List<PcaDecompte> pcas = new ArrayList<PcaDecompte>();
        pcas.add(PegasusTestFactory.createPcaRequerantAvs("04.2012", "12.2012", "200"));
        pcas.add(PegasusTestFactory.createPcaConjointAvs("04.2012", "12.2012", "400"));

        List<PcaDecompte> pcaReplaceds = new ArrayList<PcaDecompte>();
        pcaReplaceds.add(PegasusTestFactory.createPcaRequerantAvs("01.2012", "12.2012", "200"));

        // DecomptePca decomptePca = this.genearateDecompte(pcas, pcaReplaceds);
        //
        // Assert.assertEquals("Test date de début", "04.2012", decomptePca.getDateDebut());
        // Assert.assertEquals("Test date de fin", "12.2012", decomptePca.getDateFin());
        //
        // PeriodePca periode = decomptePca.getPeriodesPca().get(0);
        //
        // Assert.assertEquals("Test de le nombre de période", 1, decomptePca.getPeriodesPca().size());
        // Assert.assertEquals("1er periode le requerant", pcas.get(0), periode.getPcaRequerantNew());
        // Assert.assertEquals("1er periode le conjoint", pcas.get(1), periode.getPcaConjointNew());
        // Assert.assertEquals("1er periode(old) le requerant", pcaReplaceds.get(0), periode.getPcaRequeranReplaced());
        // Assert.assertNull("1er periode(old) le conjoint ", periode.getPcaConjointReplaced());
        // Assert.assertEquals("3600", decomptePca.getMontant().toString());

    }
}
