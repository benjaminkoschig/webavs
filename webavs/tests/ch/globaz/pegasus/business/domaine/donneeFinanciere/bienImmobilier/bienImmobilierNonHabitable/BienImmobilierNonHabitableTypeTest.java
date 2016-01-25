package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonHabitable;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonHabitable.BienImmobilierNonHabitableType;

public class BienImmobilierNonHabitableTypeTest {

    @Test
    public void testIsZonneABatirTrue() throws Exception {
        assertTrue(BienImmobilierNonHabitableType.ZONE_A_BATIR.isZonneABatir());
    }

    @Test
    public void testIsTerrainAgricoleTrue() throws Exception {
        assertTrue(BienImmobilierNonHabitableType.TERRAIN_AGRICOLE.isTerrainAgricole());
    }

    @Test
    public void testIsForetTrueTrue() throws Exception {
        assertTrue(BienImmobilierNonHabitableType.FORET.isForet());
    }

    @Test
    public void testIsCheminTrue() throws Exception {
        assertTrue(BienImmobilierNonHabitableType.CHEMIN.isChemin());
    }

    @Test
    public void testIsAisanceTrue() throws Exception {
        assertTrue(BienImmobilierNonHabitableType.AISANCE.isAisance());
    }

    @Test
    public void testIsPiscineTrue() throws Exception {
        assertTrue(BienImmobilierNonHabitableType.PISCINE.isPiscine());
    }

    @Test
    public void testIsVigneTrue() throws Exception {
        assertTrue(BienImmobilierNonHabitableType.VIGNE.isVigne());
    }

    @Test
    public void testIsMineTrue() throws Exception {
        assertTrue(BienImmobilierNonHabitableType.MINE.isMine());
    }

    @Test
    public void testIsAutreTrue() throws Exception {
        assertTrue(BienImmobilierNonHabitableType.AUTRES.isAutre());
    }

    @Test
    public void testIsZonneABatirFalse() throws Exception {
        assertFalse(BienImmobilierNonHabitableType.TERRAIN_AGRICOLE.isZonneABatir());
    }

    @Test
    public void testIsTerrainAgricoleFalse() throws Exception {
        assertFalse(BienImmobilierNonHabitableType.FORET.isTerrainAgricole());
    }

    @Test
    public void testIsForetTrueFalse() throws Exception {
        assertFalse(BienImmobilierNonHabitableType.TERRAIN_AGRICOLE.isForet());
    }

    @Test
    public void testIsCheminFalse() throws Exception {
        assertFalse(BienImmobilierNonHabitableType.TERRAIN_AGRICOLE.isChemin());
    }

    @Test
    public void testIsAisanceFalse() throws Exception {
        assertFalse(BienImmobilierNonHabitableType.TERRAIN_AGRICOLE.isAisance());
    }

    @Test
    public void testIsPiscineFalse() throws Exception {
        assertFalse(BienImmobilierNonHabitableType.TERRAIN_AGRICOLE.isPiscine());
    }

    @Test
    public void testIsVigneFalse() throws Exception {
        assertFalse(BienImmobilierNonHabitableType.TERRAIN_AGRICOLE.isVigne());
    }

    @Test
    public void testIsMineFalse() throws Exception {
        assertFalse(BienImmobilierNonHabitableType.TERRAIN_AGRICOLE.isMine());
    }

    @Test
    public void testIsAutreFalse() throws Exception {
        assertFalse(BienImmobilierNonHabitableType.TERRAIN_AGRICOLE.isAutre());
    }

}
