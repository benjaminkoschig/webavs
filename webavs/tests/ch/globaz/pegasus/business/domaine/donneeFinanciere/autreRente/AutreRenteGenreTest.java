package ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente;

import static org.junit.Assert.*;
import org.junit.Test;

public class AutreRenteGenreTest {

    @Test
    public void testIsLaa() throws Exception {
        assertTrue(AutreRenteGenre.LAA.isLaa());
    }

    @Test
    public void testIsLpp() throws Exception {
        assertTrue(AutreRenteGenre.LPP.isLpp());
    }

    @Test
    public void testIsRenteEtrangere() throws Exception {
        assertTrue(AutreRenteGenre.RENTE_ETRANGERE.isRenteEtrangere());
    }

    @Test
    public void testIsAssurancePrivee() throws Exception {
        assertTrue(AutreRenteGenre.ASSURANCE_PRIVEE.isAssurancePrivee());
    }

    @Test
    public void testIsTroisiemePilier() throws Exception {
        assertTrue(AutreRenteGenre.TROISIEME_PILIER.isTroisiemePilier());
    }

    @Test
    public void testIsLam() throws Exception {
        assertTrue(AutreRenteGenre.LAM.isLam());
    }

    @Test
    public void testIsAutres() throws Exception {
        assertTrue(AutreRenteGenre.AUTRES.isAutres());
    }

    @Test
    public void testIsLaaFalse() throws Exception {
        assertFalse(AutreRenteGenre.LPP.isLaa());
    }

    @Test
    public void testIsLppFalse() throws Exception {
        assertFalse(AutreRenteGenre.LAA.isLpp());
    }

    @Test
    public void testIsRenteEtrangereFalse() throws Exception {
        assertFalse(AutreRenteGenre.LAA.isRenteEtrangere());
    }

    @Test
    public void testIsAssurancePriveeFalse() throws Exception {
        assertFalse(AutreRenteGenre.LAA.isAssurancePrivee());
    }

    @Test
    public void testIsTroisiemePilierFalse() throws Exception {
        assertFalse(AutreRenteGenre.LAA.isTroisiemePilier());
    }

    @Test
    public void testIsLamFalse() throws Exception {
        assertFalse(AutreRenteGenre.LAA.isLam());
    }

    @Test
    public void testIsAutresFalse() throws Exception {
        assertFalse(AutreRenteGenre.LAA.isAutres());
    }

}
