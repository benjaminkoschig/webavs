package ch.globaz.pegasus.business.domaine.membreFamille;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.pegasus.business.constantes.IPCDroits;

public class RoleMembreFamilleTest {

    @Test
    public void testIsConjoint() throws Exception {
        Assert.assertTrue(RoleMembreFamille.fromValue(IPCDroits.CS_ROLE_FAMILLE_CONJOINT).isConjoint());
        Assert.assertFalse(RoleMembreFamille.fromValue(IPCDroits.CS_ROLE_FAMILLE_ENFANT).isConjoint());
    }

    @Test
    public void testIsRequerant() throws Exception {
        Assert.assertTrue(RoleMembreFamille.fromValue(IPCDroits.CS_ROLE_FAMILLE_REQUERANT).isRequerant());
        Assert.assertFalse(RoleMembreFamille.fromValue(IPCDroits.CS_ROLE_FAMILLE_ENFANT).isConjoint());
    }

    @Test
    public void testIsEnfant() throws Exception {
        Assert.assertTrue(RoleMembreFamille.fromValue(IPCDroits.CS_ROLE_FAMILLE_ENFANT).isEnfant());
        Assert.assertFalse(RoleMembreFamille.fromValue(IPCDroits.CS_ROLE_FAMILLE_REQUERANT).isConjoint());
    }

    @Test
    public void testIsInTrue() throws Exception {
        List<RoleMembreFamille> list = new ArrayList<RoleMembreFamille>();
        list.add(RoleMembreFamille.CONJOINT);
        list.add(RoleMembreFamille.ENFANT);

        Assert.assertTrue(RoleMembreFamille.ENFANT.isIn(list));
    }

    @Test
    public void testIsInFalse() throws Exception {
        List<RoleMembreFamille> list = new ArrayList<RoleMembreFamille>();
        list.add(RoleMembreFamille.CONJOINT);
        list.add(RoleMembreFamille.REQUERANT);

        Assert.assertFalse(RoleMembreFamille.ENFANT.isIn(list));
    }

    @Test
    public void testIsInElipseTrue() throws Exception {
        Assert.assertTrue(RoleMembreFamille.REQUERANT.isIn(RoleMembreFamille.CONJOINT, RoleMembreFamille.REQUERANT));
    }

    @Test
    public void testIsInElipseFalse() throws Exception {
        Assert.assertFalse(RoleMembreFamille.ENFANT.isIn(RoleMembreFamille.CONJOINT, RoleMembreFamille.REQUERANT));
    }
}
