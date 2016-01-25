package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import static org.junit.Assert.*;
import org.junit.Test;

public class ProprieteTypeTest {

    @Test
    public void testIsProprietaireTrue() throws Exception {
        assertTrue(ProprieteType.PROPRIETAIRE.isProprietaire());
    }

    @Test
    public void testIsCoProprietaireTrue() throws Exception {
        assertTrue(ProprieteType.CO_PROPRIETAIRE.isCoProprietaire());
    }

    @Test
    public void testIsUsufruitTrue() throws Exception {
        assertTrue(ProprieteType.USUFRUITIER.isUsufruit());
    }

    @Test
    public void testIsDroitHabitationTrue() throws Exception {
        assertTrue(ProprieteType.DROIT_HABITATION.isDroitHabitation());
    }

    @Test
    public void testIsNuProprietaireTrue() throws Exception {
        assertTrue(ProprieteType.NU_PROPRIETAIRE.isNuProprietaire());
    }

    @Test
    public void testIsProprietaireFalse() throws Exception {
        assertFalse(ProprieteType.CO_PROPRIETAIRE.isProprietaire());
    }

    @Test
    public void testIsCoProprietaireFalse() throws Exception {
        assertFalse(ProprieteType.USUFRUITIER.isCoProprietaire());
    }

    @Test
    public void testIsUsufruitFalse() throws Exception {
        assertFalse(ProprieteType.CO_PROPRIETAIRE.isUsufruit());
    }

    @Test
    public void testIsDroitHabitationFalse() throws Exception {
        assertFalse(ProprieteType.CO_PROPRIETAIRE.isDroitHabitation());
    }

    @Test
    public void testIsNuProprietaireFalse() throws Exception {
        assertFalse(ProprieteType.CO_PROPRIETAIRE.isNuProprietaire());
    }

}
