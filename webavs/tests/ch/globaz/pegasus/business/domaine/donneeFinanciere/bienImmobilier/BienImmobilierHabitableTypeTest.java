package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.BienImmobilierHabitableType;

public class BienImmobilierHabitableTypeTest {

    @Test
    public void testIsMaisonIndividuelleTrue() throws Exception {
        assertTrue(BienImmobilierHabitableType.MAISON_INDIVIDUELLE.isMaisonIndividuelle());
    }

    @Test
    public void testIsImmeubleLocatifTrue() throws Exception {
        assertTrue(BienImmobilierHabitableType.IMMEUBLE_LOCATIF.isImmeubleLocatif());
    }

    @Test
    public void testIsAppartementTrue() throws Exception {
        assertTrue(BienImmobilierHabitableType.APPARTEMENT.isAppartement());
    }

    @Test
    public void testIsRuralTrue() throws Exception {
        assertTrue(BienImmobilierHabitableType.RURAL.isRural());
    }

    @Test
    public void testIsAutreTrue() throws Exception {
        assertTrue(BienImmobilierHabitableType.AUTRE.isAutre());
    }

    @Test
    public void testIsMaisonIndividuelleFalse() throws Exception {
        assertFalse(BienImmobilierHabitableType.APPARTEMENT.isMaisonIndividuelle());
    }

    @Test
    public void testIsImmeubleLocatifFalse() throws Exception {
        assertFalse(BienImmobilierHabitableType.APPARTEMENT.isImmeubleLocatif());
    }

    @Test
    public void testIsAppartementFalse() throws Exception {
        assertFalse(BienImmobilierHabitableType.IMMEUBLE_LOCATIF.isAppartement());
    }

    @Test
    public void testIsRuralFalse() throws Exception {
        assertFalse(BienImmobilierHabitableType.APPARTEMENT.isRural());
    }

    @Test
    public void testIsAutreFalse() throws Exception {
        assertFalse(BienImmobilierHabitableType.APPARTEMENT.isAutre());
    }

}
