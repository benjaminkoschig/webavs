package ch.globaz.pegasus.businessimpl.services.models.decomptePca;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.pegasus.business.exceptions.models.decompte.DecompteException;
import ch.globaz.pegasus.business.services.decompte.DecompteService;
import ch.globaz.pegasus.business.vo.pcaccordee.PcaDecompte;
import ch.globaz.pegasus.businessimpl.services.decompte.DecompteServiceImpl;
import ch.globaz.pegasus.businessimpl.services.pca.DecomptePca;
import ch.globaz.pegasus.businessimpl.tests.factory.PegasusTestFactory;

public class DecomptePcaTestCase {

    protected String dateDernierPaiement = "05.2013";

    @Test
    @Ignore
    public void dateFinFerme() throws DecompteException {
        PcaDecompte pca = PegasusTestFactory.createPcaRequerantAvs("01.2013", "03.2013", "450");
        List<PcaDecompte> pcas = new ArrayList<PcaDecompte>();
        pcas.add(pca);
        DecomptePca decomptePca = this.genearateDecompte(pcas);
        Assert.assertEquals("Test date de début", "01.2013", decomptePca.getDateDebut());
        Assert.assertEquals("Test date de fin", "03.2013", decomptePca.getDateFin());
    }

    @Test(expected = DecompteException.class)
    public void exceptionDateProchainPmtNull() throws DecompteException {
        PcaDecompte pca1 = PegasusTestFactory.createPcaRequerantAvs("01.2013", "05.2013", "450");
        List<PcaDecompte> pcas = new ArrayList<PcaDecompte>();
        pcas.add(pca1);
        DecompteService decomteService = new DecompteServiceImpl();
        decomteService.generateDecomptePca(pcas, null, null, null, null);
    }

    @Test(expected = DecompteException.class)
    public void exceptionNewPcaNull() throws DecompteException {
        DecompteService decomteService = new DecompteServiceImpl();
        decomteService.generateDecomptePca(null, null, "05.2013", null, null);
    }

    @Test(expected = DecompteException.class)
    public void exceptionNewPcaVide() throws DecompteException {
        DecompteService decomteService = new DecompteServiceImpl();
        decomteService.generateDecomptePca(new ArrayList<PcaDecompte>(), null, "05.2013", null, null);
    }

    @Test(expected = DecompteException.class)
    @Ignore
    public void exceptionSameKey1() throws DecompteException {
        PcaDecompte pca1 = PegasusTestFactory.createPcaRequerantAvs("01.2013", "04.2013", "450");
        PcaDecompte pca2 = PegasusTestFactory.createPcaRequerantAvs("01.2013", null, "1450");
        List<PcaDecompte> pcas = new ArrayList<PcaDecompte>();
        pcas.add(pca1);
        pcas.add(pca2);
        DecompteService decomteService = new DecompteServiceImpl();
        decomteService.generateDecomptePca(pcas, null, dateDernierPaiement, null, null);
    }

    @Test(expected = DecompteException.class)
    @Ignore
    public void exceptionSameKey2() throws DecompteException {
        PcaDecompte pca1 = PegasusTestFactory.createPcaRequerantAvs("01.2013", "04.2013", "450");
        PcaDecompte pca2 = PegasusTestFactory.createPcaRequerantAvs("04.2013", null, "1450");
        List<PcaDecompte> pcas = new ArrayList<PcaDecompte>();
        pcas.add(pca1);
        pcas.add(pca2);
        DecompteService decomteService = new DecompteServiceImpl();
        decomteService.generateDecomptePca(pcas, null, dateDernierPaiement, null, null);
    }

    public DecomptePca genearateDecompte(List<PcaDecompte> pcasNew) throws DecompteException {
        DecompteService decomteService = new DecompteServiceImpl();
        DecomptePca decomptePca = decomteService.generateDecomptePca(pcasNew, null, dateDernierPaiement, null, null);
        return decomptePca;
    }

    public DecomptePca genearateDecompte(List<PcaDecompte> pcasNew, List<PcaDecompte> pcasReplaced)
            throws DecompteException {
        DecompteService decomteService = new DecompteServiceImpl();
        DecomptePca decomptePca = decomteService.generateDecomptePca(pcasNew, pcasReplaced, dateDernierPaiement, null,
                null);
        return decomptePca;
    }

    @Test
    @Ignore
    public void testComputedMontant() throws DecompteException {
        PcaDecompte pca1 = PegasusTestFactory.createPcaRequerantAvs("01.2013", "04.2013", "450");
        PcaDecompte pca2 = PegasusTestFactory.createPcaRequerantAvs("05.2013", null, "650");
        List<PcaDecompte> pcas = new ArrayList<PcaDecompte>();
        pcas.add(pca1);
        pcas.add(pca2);

        List<PcaDecompte> pcasRepleaced = new ArrayList<PcaDecompte>();
        pcasRepleaced.add(PegasusTestFactory.createPcaRequerantAvs("01.2013", "04.2013", "350"));
        pcasRepleaced.add(PegasusTestFactory.createPcaRequerantAvs("05.2013", null, "750"));

        DecompteService decomteService = new DecompteServiceImpl();
        DecomptePca decompte = decomteService.generateDecomptePca(pcas, pcasRepleaced, dateDernierPaiement, null, null);
        Assert.assertEquals(new BigDecimal(300), decompte.getMontant());
    }

    @Test
    @Ignore
    public void testComputedMontantInitial() throws DecompteException {
        PcaDecompte pca1 = PegasusTestFactory.createPcaRequerantAvs("01.2013", "04.2013", "450");
        PcaDecompte pca2 = PegasusTestFactory.createPcaRequerantAvs("05.2013", null, "650");
        List<PcaDecompte> pcas = new ArrayList<PcaDecompte>();
        pcas.add(pca1);
        pcas.add(pca2);

        DecompteService decomteService = new DecompteServiceImpl();
        DecomptePca decompte = decomteService.generateDecomptePca(pcas, null, dateDernierPaiement, null, null);
        Assert.assertEquals(new BigDecimal(2450), decompte.getMontant());
    }
}
