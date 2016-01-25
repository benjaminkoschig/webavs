package ch.globaz.pegasus.business.domaine.parametre.variableMetier;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariableMetierTypeDeDonnee;

public class VariableMetierTypeDeDonneeTest {

    @Test
    public void testIsMontantTrue() throws Exception {
        assertTrue(VariableMetierTypeDeDonnee.MONTANT.isMontant());
    }

    @Test
    public void testIsTauxTrue() throws Exception {
        assertTrue(VariableMetierTypeDeDonnee.TAUX.isTaux());
    }

    @Test
    public void testIsFractionTrue() throws Exception {
        assertTrue(VariableMetierTypeDeDonnee.FRACTION.isFraction());
    }

    @Test
    public void testIsMontantFalse() throws Exception {
        assertFalse(VariableMetierTypeDeDonnee.TAUX.isMontant());
    }

    @Test
    public void testIsTauxFalse() throws Exception {
        assertFalse(VariableMetierTypeDeDonnee.MONTANT.isTaux());
    }

    @Test
    public void testIsFractionFalse() throws Exception {
        assertFalse(VariableMetierTypeDeDonnee.MONTANT.isFraction());
    }

}
