package ch.globaz.pegasus.business.domaine.pca;

import static org.junit.Assert.*;
import org.junit.Test;

public class PcaRequerantConjointTest {

    @Test
    public void resolveCasPcaWihtOnlyRequerant() throws Exception {
        PcaRequerantConjoint pcas = new PcaRequerantConjoint();
        Pca pcaRequerant = new Pca();
        pcaRequerant.setGenre(PcaGenre.HOME);
        pcaRequerant.setId("1");
        pcas.setRequerant(pcaRequerant);
        pcas.setConjoint(null);

        PcaSitutation pcaCas = pcas.resolveCasPca();
        assertNotNull(pcaCas);
        assertEquals(PcaSitutation.HOME, pcaCas);
    }

    @Test
    public void resolveCasPcaWihtNoThingNull() throws Exception {
        PcaRequerantConjoint pcas = new PcaRequerantConjoint();
        pcas.setRequerant(null);
        pcas.setConjoint(null);
        PcaSitutation pcaCas = pcas.resolveCasPca();
        assertNotNull(pcaCas);
        assertEquals(PcaSitutation.INDEFINIT, pcaCas);
    }

    @Test
    public void resolveCasPcaWihtNoThing() throws Exception {
        PcaRequerantConjoint pcas = new PcaRequerantConjoint();
        pcas.setRequerant(new Pca());
        pcas.setConjoint(new Pca());
        PcaSitutation pcaCas = pcas.resolveCasPca();
        assertNotNull(pcaCas);
        assertEquals(PcaSitutation.INDEFINIT, pcaCas);
    }

    @Test
    public void resolveCasPcaWihtConjoint() throws Exception {
        PcaRequerantConjoint pcas = new PcaRequerantConjoint();
        Pca pcaRequerant = new Pca();
        Pca pcaConjoint = new Pca();
        pcaRequerant.setGenre(PcaGenre.HOME);
        pcaConjoint.setGenre(PcaGenre.HOME);
        pcas.setRequerant(pcaRequerant);
        pcaRequerant.setId("1");
        pcas.setConjoint(pcaConjoint);
        pcaConjoint.setId("2");

        PcaSitutation pcaCas = pcas.resolveCasPca();
        assertNotNull(pcaCas);
        assertEquals(PcaSitutation.COUPLE_SEPARE_DEUX_EN_HOME, pcaCas);
    }

    @Test
    public void resolveCasPcaWihtDom2R() throws Exception {

        PcaRequerantConjoint pcas = new PcaRequerantConjoint();
        Pca pcaRequerant = new Pca();
        Pca pcaConjoint = new Pca();
        pcaRequerant.setGenre(PcaGenre.DOMICILE);
        pcaRequerant.getBeneficiaireConjointDom2R().setId(new Long(1));
        pcaConjoint.setGenre(null);
        pcas.setRequerant(pcaRequerant);
        pcaRequerant.setId("1");
        pcas.setConjoint(pcaConjoint);

        PcaSitutation pcaCas = pcas.resolveCasPca();
        assertNotNull(pcaCas);
        assertEquals(PcaSitutation.DOM2R, pcaCas);
    }

    @Test
    public void testHasConjoint() throws Exception {
        PcaRequerantConjoint pcas = new PcaRequerantConjoint();
        assertFalse(pcas.hasConjoint());
        pcas.setConjoint(null);
        assertFalse(pcas.hasConjoint());
        pcas.setConjoint(new Pca());
        pcas.getConjoint().setId("   ");
        assertFalse(pcas.hasConjoint());
        pcas.getConjoint().setId("0");
        assertFalse(pcas.hasConjoint());
        pcas.getConjoint().setId("null");
        assertFalse(pcas.hasConjoint());
    }
}
