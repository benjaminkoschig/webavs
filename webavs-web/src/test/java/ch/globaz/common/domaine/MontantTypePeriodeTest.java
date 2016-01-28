package ch.globaz.common.domaine;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.MontantTypePeriode;

public class MontantTypePeriodeTest {

    @Test
    public void testIsMensuelTrue() throws Exception {
        assertTrue(MontantTypePeriode.MENSUEL.isMensuel());
    }

    @Test
    public void testIsAnnuelTrue() throws Exception {
        assertTrue(MontantTypePeriode.ANNUEL.isAnnuel());
    }

    @Test
    public void testIsSansPeriodeTrue() throws Exception {
        assertTrue(MontantTypePeriode.SANS_PERIODE.isSansPeriode());
    }

    @Test
    public void testIsJournalierTrue() throws Exception {
        assertTrue(MontantTypePeriode.JOURNALIER.isJournalier());
    }

    @Test
    public void testIsMensuelFalse() throws Exception {
        assertFalse(MontantTypePeriode.ANNUEL.isMensuel());
    }

    @Test
    public void testIsAnnuelFalse() throws Exception {
        assertFalse(MontantTypePeriode.MENSUEL.isAnnuel());
    }

    @Test
    public void testIsSansPeriodeFalse() throws Exception {
        assertFalse(MontantTypePeriode.ANNUEL.isSansPeriode());
    }

    @Test
    public void testIsJournalierFalse() throws Exception {
        assertFalse(MontantTypePeriode.JOURNALIER.isSansPeriode());
    }

}
