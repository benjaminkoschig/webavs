package ch.globaz.pegasus.business.domaine.donneeFinanciere.pensionAlimentaire;

import static org.junit.Assert.*;
import org.junit.Test;

public class PensionAlimentaireTypeTest {

    @Test
    public void testIsDueTrue() throws Exception {
        assertTrue(PensionAlimentaireType.DUE.isDue());
    }

    @Test
    public void testIsVersee() throws Exception {
        assertTrue(PensionAlimentaireType.VERSEE.isVersee());
    }

    @Test
    public void testIsDueFalse() throws Exception {
        assertFalse(PensionAlimentaireType.VERSEE.isDue());
    }

    @Test
    public void testIsVerseeFalse() throws Exception {
        assertFalse(PensionAlimentaireType.DUE.isVersee());
    }

}
