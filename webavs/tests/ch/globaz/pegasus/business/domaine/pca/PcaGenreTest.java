package ch.globaz.pegasus.business.domaine.pca;

import static org.junit.Assert.*;
import org.junit.Test;

public class PcaGenreTest {

    @Test
    public void testIsDomicileTrue() throws Exception {
        assertTrue(PcaGenre.DOMICILE.isDomicile());
    }

    @Test
    public void testIsHomeTrue() throws Exception {
        assertTrue(PcaGenre.HOME.isHome());
    }

    @Test
    public void testIsDomicileFalse() throws Exception {
        assertFalse(PcaGenre.HOME.isDomicile());
    }

    @Test
    public void testIsHomeFalse() throws Exception {
        assertFalse(PcaGenre.DOMICILE.isHome());
    }
}
