package ch.globaz.pegasus.business.domaine.parametre.variableMetier;

import org.junit.Test;

public class VariableMetierTest {

    @Test(expected = RuntimeException.class)
    public void testGetPart() throws Exception {
        VariableMetier vm = new VariableMetier();
        vm.getPart();
    }

    @Test(expected = RuntimeException.class)
    public void testGetTaux() throws Exception {
        VariableMetier vm = new VariableMetier();
        vm.getTaux();
    }

    @Test(expected = RuntimeException.class)
    public void testGetMontant() throws Exception {
        VariableMetier vm = new VariableMetier();
        vm.getMontant();
    }

}
