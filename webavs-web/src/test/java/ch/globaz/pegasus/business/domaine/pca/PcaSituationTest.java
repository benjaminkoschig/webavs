package ch.globaz.pegasus.business.domaine.pca;

import static org.junit.Assert.*;
import org.junit.Test;

public class PcaSituationTest {

    @Test(expected = NullPointerException.class)
    public void testResolveFail() throws Exception {
        PcaSituation.resolve(null, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResolveFailBothDomicile() throws Exception {
        PcaSituation.resolve(PcaGenre.DOMICILE, PcaGenre.DOMICILE, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResolveCoupleSeparerWihtDom2RFaile() throws Exception {
        PcaSituation.resolve(PcaGenre.DOMICILE, PcaGenre.HOME, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResolveRequeranHomeDom2RFaile() throws Exception {
        PcaSituation.resolve(PcaGenre.HOME, PcaGenre.DOMICILE, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResolveRequeranEtConjoinHomeDom2RFaile() throws Exception {
        PcaSituation.resolve(PcaGenre.HOME, PcaGenre.HOME, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResolveHomeDom2RFaile() throws Exception {
        PcaSituation.resolve(PcaGenre.HOME, null, true);
    }

    @Test
    public void testResolveDomicile() throws Exception {
        assertEquals(PcaSituation.DOMICILE, PcaSituation.resolve(PcaGenre.DOMICILE, null, false));
    }

    @Test
    public void testResolveHome() throws Exception {
        assertEquals(PcaSituation.HOME, PcaSituation.resolve(PcaGenre.HOME, null, false));
    }

    @Test
    public void testResolveDom2R() throws Exception {
        assertEquals(PcaSituation.DOM2R, PcaSituation.resolve(PcaGenre.DOMICILE, null, true));
    }

    @Test
    public void testResolveCoupleSeparConjointEnHome() throws Exception {
        assertEquals(PcaSituation.COUPLE_SEPARE_CONJOINT_HOME, PcaSituation.resolve(PcaGenre.DOMICILE, PcaGenre.HOME, false));
    }

    @Test
    public void testResolveCoupleSeparRequerantEnHome() throws Exception {
        assertEquals(PcaSituation.COUPLE_SEPARE_REQUERANT_HOME, PcaSituation.resolve(PcaGenre.HOME, PcaGenre.DOMICILE, false));
    }

    @Test
    public void testResolveCoupleSeparRequerantEtConjoinEnHome() throws Exception {
        assertEquals(PcaSituation.COUPLE_SEPARE_DEUX_EN_HOME, PcaSituation.resolve(PcaGenre.HOME, PcaGenre.HOME, false));
    }

    @Test
    public void testIsDom2True() throws Exception {
        assertTrue(PcaSituation.DOM2R.isDom2());
    }

    @Test
    public void testIsDomicileTrue() throws Exception {
        assertTrue(PcaSituation.DOMICILE.isDomicile());
    }

    @Test
    public void testIsCoupleSepareConjoinEnHome() throws Exception {
        assertTrue(PcaSituation.COUPLE_SEPARE_CONJOINT_HOME.isCoupleSepareConjoinEnHome());
    }

    @Test
    public void testIsCoupleSepareRequerantEnHome() throws Exception {
        assertTrue(PcaSituation.COUPLE_SEPARE_REQUERANT_HOME.isCoupleSepareRequerantEnHome());
    }

    @Test
    public void testIsCoupleSepareLesDeuxHome() throws Exception {
        assertTrue(PcaSituation.COUPLE_SEPARE_DEUX_EN_HOME.isCoupleSepareLesDeuxHome());
    }

    @Test
    public void testIsCoupleSepareLesDeuxEnHomeTrue() throws Exception {
        assertTrue(PcaSituation.COUPLE_SEPARE_DEUX_EN_HOME.isCoupleSepare());
    }

    @Test
    public void testIsCoupleSepareConjointTrue() throws Exception {
        assertTrue(PcaSituation.COUPLE_SEPARE_CONJOINT_HOME.isCoupleSepare());
    }

    @Test
    public void testIsCoupleSepareRequerantTrue() throws Exception {
        assertTrue(PcaSituation.COUPLE_SEPARE_REQUERANT_HOME.isCoupleSepare());
    }

    @Test
    public void testIsCoupleSepareFalse() throws Exception {
        assertFalse(PcaSituation.DOM2R.isCoupleSepare());
        assertFalse(PcaSituation.DOMICILE.isCoupleSepare());
    }

    @Test
    public void testIsHome() throws Exception {
        assertTrue(PcaSituation.HOME.isHome());
    }
}
