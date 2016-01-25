package ch.globaz.draco.business.domaine;

import static org.junit.Assert.*;
import org.junit.Test;

public class DeclarationSalaireTypeTest {

    @Test(expected = IllegalArgumentException.class)
    public void testFromValueString() throws Exception {
        DeclarationSalaireType.fromCodeSystem("");
    }

    @Test
    public void testIsPrincipale() throws Exception {
        assertTrue(DeclarationSalaireType.PRINCIPALE.isPrincipale());
    }

    @Test
    public void testIsComplementaire() throws Exception {
        assertTrue(DeclarationSalaireType.COMPLEMENTAIRE.isComplementaire());
    }

    @Test
    public void testIsBouclementAcompte() throws Exception {
        assertTrue(DeclarationSalaireType.BOUCLEMENT_ACOMPTE.isBouclementAcompte());
    }

    @Test
    public void testIsControleEmployeur() throws Exception {
        assertTrue(DeclarationSalaireType.CONTROLE_EMPLOYEUR.isControleEmployeur());
    }

    @Test
    public void testIsLTN() throws Exception {
        assertTrue(DeclarationSalaireType.LTN.isLTN());
    }

    @Test
    public void testIsLTNComplementaire() throws Exception {
        assertTrue(DeclarationSalaireType.LTN_COMPLEMENTAIRE.isLTNComplementaire());
    }

    @Test
    public void testIsSalaireDifferes() throws Exception {
        assertTrue(DeclarationSalaireType.SALAIRE_DIFFERES.isSalaireDifferes());
    }

    @Test
    public void testIsIci() throws Exception {
        assertTrue(DeclarationSalaireType.ICI.isIci());
    }

    @Test
    public void testFromCodeSystem_122001() throws Exception {
        assertEquals(DeclarationSalaireType.PRINCIPALE, DeclarationSalaireType.fromCodeSystem("122001"));
    }

    @Test
    public void testFromCodeSystem_122002() throws Exception {
        assertEquals(DeclarationSalaireType.COMPLEMENTAIRE, DeclarationSalaireType.fromCodeSystem("122002"));
    }

    @Test
    public void testFromCodeSystem_122003() throws Exception {
        assertEquals(DeclarationSalaireType.BOUCLEMENT_ACOMPTE, DeclarationSalaireType.fromCodeSystem("122003"));
    }

    @Test
    public void testFromCodeSystem_122004() throws Exception {
        assertEquals(DeclarationSalaireType.CONTROLE_EMPLOYEUR, DeclarationSalaireType.fromCodeSystem("122004"));
    }

    @Test
    public void testFromCodeSystem_122005() throws Exception {
        assertEquals(DeclarationSalaireType.LTN, DeclarationSalaireType.fromCodeSystem("122005"));
    }

    @Test
    public void testFromCodeSystem_122006() throws Exception {
        assertEquals(DeclarationSalaireType.LTN_COMPLEMENTAIRE, DeclarationSalaireType.fromCodeSystem("122006"));
    }

    @Test
    public void testFromCodeSystem_122007() throws Exception {
        assertEquals(DeclarationSalaireType.SALAIRE_DIFFERES, DeclarationSalaireType.fromCodeSystem("122007"));
    }

    @Test
    public void testFromCodeSystem_122008() throws Exception {
        assertEquals(DeclarationSalaireType.ICI, DeclarationSalaireType.fromCodeSystem("122008"));
    }

}
