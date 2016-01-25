package ch.globaz.pegasus.business.domaine.pca;

import static org.junit.Assert.*;
import org.junit.Test;

public class PcaSitutationTest {

    @Test(expected = NullPointerException.class)
    public void testResolveFail() throws Exception {
        PcaSitutation.resolve(null, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResolveFailBothDomicile() throws Exception {
        PcaSitutation.resolve(PcaGenre.DOMICILE, PcaGenre.DOMICILE, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResolveCoupleSeparerWihtDom2RFaile() throws Exception {
        PcaSitutation.resolve(PcaGenre.DOMICILE, PcaGenre.HOME, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResolveRequeranHomeDom2RFaile() throws Exception {
        PcaSitutation.resolve(PcaGenre.HOME, PcaGenre.DOMICILE, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResolveRequeranEtConjoinHomeDom2RFaile() throws Exception {
        PcaSitutation.resolve(PcaGenre.HOME, PcaGenre.HOME, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResolveHomeDom2RFaile() throws Exception {
        PcaSitutation.resolve(PcaGenre.HOME, null, true);
    }

    @Test
    public void testResolveDomicile() throws Exception {
        assertEquals(PcaSitutation.DOMICILE, PcaSitutation.resolve(PcaGenre.DOMICILE, null, false));
    }

    @Test
    public void testResolveHome() throws Exception {
        assertEquals(PcaSitutation.HOME, PcaSitutation.resolve(PcaGenre.HOME, null, false));
    }

    @Test
    public void testResolveDom2R() throws Exception {
        assertEquals(PcaSitutation.DOM2R, PcaSitutation.resolve(PcaGenre.DOMICILE, null, true));
    }

    @Test
    public void testResolveCoupleSeparConjointEnHome() throws Exception {
        assertEquals(PcaSitutation.COUPLE_SEPARE_CONJOINT_HOME, PcaSitutation.resolve(PcaGenre.DOMICILE, PcaGenre.HOME, false));
    }

    @Test
    public void testResolveCoupleSeparRequerantEnHome() throws Exception {
        assertEquals(PcaSitutation.COUPLE_SEPARE_REQUERANT_HOME, PcaSitutation.resolve(PcaGenre.HOME, PcaGenre.DOMICILE, false));
    }

    @Test
    public void testResolveCoupleSeparRequerantEtConjoinEnHome() throws Exception {
        assertEquals(PcaSitutation.COUPLE_SEPARE_DEUX_EN_HOME, PcaSitutation.resolve(PcaGenre.HOME, PcaGenre.HOME, false));
    }

    @Test
    public void testIsDom2True() throws Exception {
        assertTrue(PcaSitutation.DOM2R.isDom2());
    }

    @Test
    public void testIsDomicileTrue() throws Exception {
        assertTrue(PcaSitutation.DOMICILE.isDomicile());
    }

    @Test
    public void testIsCoupleSepareConjoinEnHome() throws Exception {
        assertTrue(PcaSitutation.COUPLE_SEPARE_CONJOINT_HOME.isCoupleSepareConjoinEnHome());
    }

    @Test
    public void testIsCoupleSepareRequerantEnHome() throws Exception {
        assertTrue(PcaSitutation.COUPLE_SEPARE_REQUERANT_HOME.isCoupleSepareRequerantEnHome());
    }

    @Test
    public void testIsCoupleSepareLesDeuxHome() throws Exception {
        assertTrue(PcaSitutation.COUPLE_SEPARE_DEUX_EN_HOME.isCoupleSepareLesDeuxHome());
    }

    @Test
    public void testIsCoupleSepareLesDeuxEnHomeTrue() throws Exception {
        assertTrue(PcaSitutation.COUPLE_SEPARE_DEUX_EN_HOME.isCoupleSepare());
    }

    @Test
    public void testIsCoupleSepareConjointTrue() throws Exception {
        assertTrue(PcaSitutation.COUPLE_SEPARE_CONJOINT_HOME.isCoupleSepare());
    }

    @Test
    public void testIsCoupleSepareRequerantTrue() throws Exception {
        assertTrue(PcaSitutation.COUPLE_SEPARE_REQUERANT_HOME.isCoupleSepare());
    }

    @Test
    public void testIsCoupleSepareFalse() throws Exception {
        assertFalse(PcaSitutation.DOM2R.isCoupleSepare());
        assertFalse(PcaSitutation.DOMICILE.isCoupleSepare());
    }

    @Test
    public void testIsHome() throws Exception {
        assertTrue(PcaSitutation.HOME.isHome());
    }
}
