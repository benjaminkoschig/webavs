package ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere;

import static org.junit.Assert.*;
import org.junit.Test;

public class MonnaieEtrangereTypeTest {

    @Test
    public void testIsFrancSuisseTrue() throws Exception {
        assertTrue(MonnaieEtrangereType.FRANC_SUISSE.isFrancSuisse());
    }

    @Test
    public void testIsFrancSuisseFalse() throws Exception {
        assertFalse(MonnaieEtrangereType.fromValue("sjssjs").isFrancSuisse());
    }

}
