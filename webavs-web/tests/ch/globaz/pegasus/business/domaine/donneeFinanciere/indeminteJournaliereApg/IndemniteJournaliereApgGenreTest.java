package ch.globaz.pegasus.business.domaine.donneeFinanciere.indeminteJournaliereApg;

import static org.junit.Assert.*;
import org.junit.Test;

public class IndemniteJournaliereApgGenreTest {

    @Test
    public void testIsIjChomageTrue() throws Exception {
        assertTrue(IndemniteJournaliereApgGenre.IJ_CHOMAGE.isIjChomage());
    }

    @Test
    public void testIsIjAaTrue() throws Exception {
        assertTrue(IndemniteJournaliereApgGenre.IJ_AA.isIjAa());
    }

    @Test
    public void testIsIjAmTrue() throws Exception {
        assertTrue(IndemniteJournaliereApgGenre.IJ_AM.isIjAm());
    }

    @Test
    public void testIsIjLamalTrue() throws Exception {
        assertTrue(IndemniteJournaliereApgGenre.IJ_LAMAL.isIjLamal());
    }

    @Test
    public void testIsApgTrue() throws Exception {
        assertTrue(IndemniteJournaliereApgGenre.APG.isApg());
    }

    @Test
    public void testIsAutreTrue() throws Exception {
        assertTrue(IndemniteJournaliereApgGenre.AUTRE.isAutre());
    }

    @Test
    public void testIsIjChomageFalse() throws Exception {
        assertFalse(IndemniteJournaliereApgGenre.IJ_AA.isIjChomage());
    }

    @Test
    public void testIsIjAaFalse() throws Exception {
        assertFalse(IndemniteJournaliereApgGenre.IJ_CHOMAGE.isIjAa());
    }

    @Test
    public void testIsIjAmFalse() throws Exception {
        assertFalse(IndemniteJournaliereApgGenre.IJ_CHOMAGE.isIjAm());
    }

    @Test
    public void testIsIjLamalFalse() throws Exception {
        assertFalse(IndemniteJournaliereApgGenre.IJ_CHOMAGE.isIjLamal());
    }

    @Test
    public void testIsApgFalse() throws Exception {
        assertFalse(IndemniteJournaliereApgGenre.IJ_CHOMAGE.isApg());
    }

    @Test
    public void testIsAutreFalse() throws Exception {
        assertFalse(IndemniteJournaliereApgGenre.IJ_CHOMAGE.isAutre());
    }

}
