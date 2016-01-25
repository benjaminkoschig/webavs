package ch.globaz.pegasus.businessimpl.services.models.decomptePca;

import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.pegasus.business.exceptions.models.decompte.DecompteException;
import ch.globaz.pegasus.business.vo.pcaccordee.PcaDecompte;
import ch.globaz.pegasus.businessimpl.services.pca.DecomptePca;
import ch.globaz.pegasus.businessimpl.services.pca.PeriodePca;
import ch.globaz.pegasus.businessimpl.tests.factory.PegasusTestFactory;

public class DecomptePcaCoupleSepareeTest extends DecomptePcaTestCase {
    @Test
    @Ignore
    public void coupleSepareeCorrectionDroit() throws DecompteException {

        List<PcaDecompte> pcas = new ArrayList<PcaDecompte>();
        pcas.add(PegasusTestFactory.createPcaRequerantAvs("01.2011", "03.2011"));
        pcas.add(PegasusTestFactory.createPcaConjointAvs("01.2011", "03.2011"));
        pcas.add(PegasusTestFactory.createPcaRequerantAvs("04.2011", "08.2011"));
        pcas.add(PegasusTestFactory.createPcaConjointAvs("04.2011", "08.2011"));
        pcas.add(PegasusTestFactory.createPcaRequerantAvs("09.2011", "12.2011"));
        pcas.add(PegasusTestFactory.createPcaConjointAvs("09.2011", "12.2011"));
        pcas.add(PegasusTestFactory.createPcaRequerantAvs("01.2012", "12.2012"));
        pcas.add(PegasusTestFactory.createPcaConjointAvs("01.2012", "12.2012"));
        pcas.add(PegasusTestFactory.createPcaRequerantAvs("01.2013", null));
        pcas.add(PegasusTestFactory.createPcaConjointAvs("01.2013", null));

        List<PcaDecompte> pcasReplaced = new ArrayList<PcaDecompte>();
        pcasReplaced.add(PegasusTestFactory.createPcaRequerantAvs("01.2011", "12.2011"));
        pcasReplaced.add(PegasusTestFactory.createPcaConjointAvs("01.2011", "12.2011"));
        pcasReplaced.add(PegasusTestFactory.createPcaRequerantAvs("01.2012", "12.2012"));
        pcasReplaced.add(PegasusTestFactory.createPcaConjointAvs("01.2012", "12.2012"));
        pcasReplaced.add(PegasusTestFactory.createPcaRequerantAvs("01.2013", "01.2013"));
        pcasReplaced.add(PegasusTestFactory.createPcaConjointAvs("01.2013", "01.2013"));
        pcasReplaced.add(PegasusTestFactory.createPcaRequerantAvs("02.2013", "03.2013"));
        pcasReplaced.add(PegasusTestFactory.createPcaConjointAvs("02.2013", "03.2013"));
        pcasReplaced.add(PegasusTestFactory.createPcaRequerantAvs("04.2013", null));
        pcasReplaced.add(PegasusTestFactory.createPcaConjointAvs("04.2013", null));

        DecomptePca decomptePca = this.genearateDecompte(pcas, pcasReplaced);

        // Assert.assertEquals("Test date de début", "01.2013", decomptePca.getDateDebut());
        // Assert.assertEquals("Test date de fin", this.dateDernierPaiement, decomptePca.getDateFin());

        PeriodePca periode = decomptePca.getPeriodesPca().get(0);

        Assert.assertEquals("Test de le nombre de période", 7, decomptePca.getPeriodesPca().size());

        Assert.assertEquals("Nombre de mois dans la 1er période  (01.2011 - 03.2011)", 3, periode.getNbMont());
        Assert.assertEquals("1er periode requerant", pcas.get(0), periode.getPcaRequerantNew());
        Assert.assertEquals("1er periode conjoint", pcas.get(1), periode.getPcaConjointNew());
        Assert.assertEquals("1er periode(old) requerant", pcasReplaced.get(0), periode.getPcaRequeranReplaced());
        Assert.assertEquals("1er periode(old) conjoint", pcasReplaced.get(1), periode.getPcaConjointReplaced());

        PeriodePca periode2 = decomptePca.getPeriodesPca().get(1);
        Assert.assertEquals("Nombre de mois dans la 2eme période  (04.2011 - 08.2011)", 5, periode2.getNbMont());
        Assert.assertEquals("1er periode requerant", pcas.get(2), periode2.getPcaRequerantNew());
        Assert.assertEquals("1er periode conjoint", pcas.get(3), periode2.getPcaConjointNew());
        Assert.assertEquals("1er periode(old) requerant", pcasReplaced.get(0), periode2.getPcaRequeranReplaced());
        Assert.assertEquals("1er periode(old) conjoint", pcasReplaced.get(1), periode2.getPcaConjointReplaced());

        PeriodePca periode3 = decomptePca.getPeriodesPca().get(2);
        Assert.assertEquals("Nombre de mois dans la 3eme période  (09.2011 - 12.2011)", 4, periode3.getNbMont());
        Assert.assertEquals("1er periode requerant", pcas.get(4), periode3.getPcaRequerantNew());
        Assert.assertEquals("1er periode conjoint", pcas.get(5), periode3.getPcaConjointNew());
        Assert.assertEquals("1er periode(old) requerant", pcasReplaced.get(0), periode3.getPcaRequeranReplaced());
        Assert.assertEquals("1er periode(old) conjoint", pcasReplaced.get(1), periode3.getPcaConjointReplaced());

        PeriodePca periode4 = decomptePca.getPeriodesPca().get(3);
        Assert.assertEquals("Nombre de mois dans la 4eme période  (01.2012 - 12.2012)", 12, periode4.getNbMont());
        Assert.assertEquals("1er periode requerant", pcas.get(6), periode4.getPcaRequerantNew());
        Assert.assertEquals("1er periode conjoint", pcas.get(7), periode4.getPcaConjointNew());
        Assert.assertEquals("1er periode(old) requerant", pcasReplaced.get(2), periode4.getPcaRequeranReplaced());
        Assert.assertEquals("1er periode(old) conjoint", pcasReplaced.get(3), periode4.getPcaConjointReplaced());

        PeriodePca periode5 = decomptePca.getPeriodesPca().get(4);
        Assert.assertEquals("Nombre de mois dans la 5eme période  (01.2013 - 01.2013)", 1, periode5.getNbMont());
        Assert.assertEquals("1er periode requerant", pcas.get(8), periode5.getPcaRequerantNew());
        Assert.assertEquals("1er periode conjoint", pcas.get(9), periode5.getPcaConjointNew());
        Assert.assertEquals("1er periode(old) requerant", pcasReplaced.get(4), periode5.getPcaRequeranReplaced());
        Assert.assertEquals("1er periode(old) conjoint", pcasReplaced.get(5), periode5.getPcaConjointReplaced());

        PeriodePca periode6 = decomptePca.getPeriodesPca().get(5);
        Assert.assertEquals("Nombre de mois dans la 6eme période  (02.2013 - 03.2013)", 2, periode6.getNbMont());
        Assert.assertEquals("1er periode requerant", pcas.get(8), periode6.getPcaRequerantNew());
        Assert.assertEquals("1er periode conjoint", pcas.get(9), periode6.getPcaConjointNew());
        Assert.assertEquals("1er periode(old) requerant", pcasReplaced.get(6), periode6.getPcaRequeranReplaced());
        Assert.assertEquals("1er periode(old) conjoint", pcasReplaced.get(7), periode6.getPcaConjointReplaced());

        PeriodePca periode7 = decomptePca.getPeriodesPca().get(6);
        Assert.assertEquals("Nombre de mois dans la 7eme période  (04.2013 - 05.2013)", 2, periode7.getNbMont());
        Assert.assertEquals("1er periode requerant", pcas.get(8), periode7.getPcaRequerantNew());
        Assert.assertEquals("1er periode conjoint", pcas.get(9), periode7.getPcaConjointNew());
        Assert.assertEquals("1er periode(old) requerant", pcasReplaced.get(8), periode7.getPcaRequeranReplaced());
        Assert.assertEquals("1er periode(old) conjoint", pcasReplaced.get(9), periode7.getPcaConjointReplaced());
    }

    @Test
    @Ignore
    public void coupleSepareeInitialAvecPlusieursPeriode() throws DecompteException {
        PcaDecompte pca = PegasusTestFactory.createPcaRequerantAvs("01.2013", null, "450");
        PcaDecompte pcaConjoint = PegasusTestFactory.createPcaConjointAvs("01.2013", null, "1450");
        List<PcaDecompte> pcas = new ArrayList<PcaDecompte>();
        pcas.add(pca);
        pcas.add(pcaConjoint);
        DecomptePca decomptePca = this.genearateDecompte(pcas);

        Assert.assertEquals("Test date de début", "01.2013", decomptePca.getDateDebut());
        Assert.assertNull("Test date de fin", decomptePca.getDateFin());

        PeriodePca periode = decomptePca.getPeriodesPca().get(0);

        Assert.assertEquals("Test de le nombre de période", 1, decomptePca.getPeriodesPca().size());
        Assert.assertEquals("La première periode doit avoir le requerant", pca, periode.getPcaRequerantNew());
        Assert.assertEquals("La première periode doit avoir le conjoint", pcaConjoint, periode.getPcaConjointNew());
        Assert.assertEquals("Nombre de mois dans la 1er période", 5, periode.getNbMont());
        Assert.assertNull(periode.getPcaConjointReplaced());
        Assert.assertNull(periode.getPcaRequeranReplaced());
    }

    @Ignore
    @Test
    public void coupleSepareeToDom() throws DecompteException {

        List<PcaDecompte> pcas = new ArrayList<PcaDecompte>();

        pcas.add(PegasusTestFactory.createPcaRequerantAvs("01.2013", "03.2013"));
        pcas.add(PegasusTestFactory.createPcaRequerantAvs("04.2013", "04.2013"));
        pcas.add(PegasusTestFactory.createPcaRequerantAvs("05.2013", null));

        List<PcaDecompte> pcasReplaced = new ArrayList<PcaDecompte>();

        pcasReplaced.add(PegasusTestFactory.createPcaRequerantAvs("01.2013", "01.2013"));
        pcasReplaced.add(PegasusTestFactory.createPcaConjointAvs("01.2013", "01.2013"));
        pcasReplaced.add(PegasusTestFactory.createPcaRequerantAvs("02.2013", "03.2013"));
        pcasReplaced.add(PegasusTestFactory.createPcaConjointAvs("02.2013", "03.2013"));
        pcasReplaced.add(PegasusTestFactory.createPcaRequerantAvs("04.2013", null));
        pcasReplaced.add(PegasusTestFactory.createPcaConjointAvs("04.2013", null));

        DecomptePca decomptePca = this.genearateDecompte(pcas, pcasReplaced);

        Assert.assertEquals("Test date de début", "01.2013", decomptePca.getDateDebut());
        Assert.assertNull("Test date de fin", decomptePca.getDateFin());

        Assert.assertEquals("Test de le nombre de période", 4, decomptePca.getPeriodesPca().size());

        PeriodePca periode = decomptePca.getPeriodesPca().get(0);
        Assert.assertEquals("Nombre de mois dans la 1er période  (01.2013 - 01.2013)", 1, periode.getNbMont());
        Assert.assertEquals("1er periode requerant", pcas.get(0), periode.getPcaRequerantNew());
        Assert.assertNull("1er periode conjoint null", periode.getPcaConjointNew());
        Assert.assertEquals("1er periode(old) requerant", pcasReplaced.get(0), periode.getPcaRequeranReplaced());
        Assert.assertEquals("1er periode(old) conjoint", pcasReplaced.get(1), periode.getPcaConjointReplaced());

        PeriodePca periode2 = decomptePca.getPeriodesPca().get(1);
        Assert.assertEquals("Nombre de mois dans la 1er période  (02.2013 - 03.2013)", 2, periode2.getNbMont());
        Assert.assertEquals("1er periode requerant", pcas.get(0), periode2.getPcaRequerantNew());
        Assert.assertNull("1er periode conjoint null", periode2.getPcaConjointNew());
        Assert.assertEquals("1er periode(old) requerant", pcasReplaced.get(2), periode2.getPcaRequeranReplaced());
        Assert.assertEquals("1er periode(old) conjoint", pcasReplaced.get(3), periode2.getPcaConjointReplaced());

        PeriodePca periode3 = decomptePca.getPeriodesPca().get(2);
        Assert.assertEquals("Nombre de mois dans la 1er période  (04.2013 - 04.2013)", 1, periode3.getNbMont());
        Assert.assertEquals("1er periode requerant", pcas.get(1), periode3.getPcaRequerantNew());
        Assert.assertNull("1er periode conjoint null", periode3.getPcaConjointNew());
        Assert.assertEquals("1er periode(old) requerant", pcasReplaced.get(4), periode3.getPcaRequeranReplaced());
        Assert.assertEquals("1er periode(old) conjoint", pcasReplaced.get(5), periode3.getPcaConjointReplaced());

        PeriodePca periode4 = decomptePca.getPeriodesPca().get(3);
        Assert.assertEquals("Nombre de mois dans la 1er période  (05.2013 - 05.2013)", 1, periode4.getNbMont());
        Assert.assertEquals("1er periode requerant", pcas.get(2), periode4.getPcaRequerantNew());
        Assert.assertNull("1er periode conjoint null", periode4.getPcaConjointNew());
        Assert.assertEquals("1er periode(old) requerant", pcasReplaced.get(4), periode4.getPcaRequeranReplaced());
        Assert.assertEquals("1er periode(old) conjoint", pcasReplaced.get(5), periode4.getPcaConjointReplaced());
    }

}
