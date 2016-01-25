package ch.globaz.pegasus.business.domaine.membreFamille;

import static org.junit.Assert.*;
import org.junit.Test;

public class MembresFamillesTest {

    private MembresFamilles membresFamilles = new MembresFamilles();

    @Test
    public void testCountEmpty() throws Exception {
        assertEquals(0, membresFamilles.countNbEnfant());
    }

    @Test
    public void testCountNbEnfant3() throws Exception {
        membresFamilles.add(build(RoleMembreFamille.REQUERANT));
        membresFamilles.add(build(RoleMembreFamille.ENFANT));
        membresFamilles.add(build(RoleMembreFamille.ENFANT));
        membresFamilles.add(build(RoleMembreFamille.ENFANT));
        assertEquals(3, membresFamilles.countNbEnfant());
    }

    @Test
    public void testCountNbEnfant0() throws Exception {
        membresFamilles.add(build(RoleMembreFamille.REQUERANT));
        membresFamilles.add(build(RoleMembreFamille.CONJOINT));
        assertEquals(0, membresFamilles.countNbEnfant());
    }

    @Test
    public void testAdd() throws Exception {
        assertTrue(membresFamilles.add(build(RoleMembreFamille.REQUERANT)));
        assertEquals(1, membresFamilles.size());
    }


    private MembreFamille build(RoleMembreFamille role) {
        MembreFamille membreFamille = new MembreFamille();
        membreFamille.setRoleMembreFamille(role);
        return membreFamille;
    }

    @Test
    public void testHasEnfantTrue() throws Exception {
        membresFamilles.add(build(RoleMembreFamille.REQUERANT));
        membresFamilles.add(build(RoleMembreFamille.CONJOINT));
        membresFamilles.add(build(RoleMembreFamille.ENFANT));
        membresFamilles.add(build(RoleMembreFamille.ENFANT));
        assertTrue(membresFamilles.hasEnfant());
    }

    @Test
    public void testHasEnfantFalse() throws Exception {
        membresFamilles.add(build(RoleMembreFamille.REQUERANT));
        membresFamilles.add(build(RoleMembreFamille.CONJOINT));
        assertFalse(membresFamilles.hasEnfant());
    }

    @Test
    public void testHasConjointTrue() throws Exception {
        membresFamilles.add(build(RoleMembreFamille.REQUERANT));
        membresFamilles.add(build(RoleMembreFamille.CONJOINT));
        membresFamilles.add(build(RoleMembreFamille.ENFANT));
        assertTrue(membresFamilles.hasConjoint());
    }

    @Test
    public void testHasConjointFalse() throws Exception {
        membresFamilles.add(build(RoleMembreFamille.REQUERANT));
        membresFamilles.add(build(RoleMembreFamille.ENFANT));
        assertFalse(membresFamilles.hasConjoint());
    }

    @Test
    public void testHasRequerantTrue() throws Exception {
        membresFamilles.add(build(RoleMembreFamille.REQUERANT));
        membresFamilles.add(build(RoleMembreFamille.ENFANT));
        assertTrue(membresFamilles.hasRequerant());
    }

    @Test
    public void testHasRequerantFalse() throws Exception {
        membresFamilles.add(build(RoleMembreFamille.CONJOINT));
        membresFamilles.add(build(RoleMembreFamille.ENFANT));
        assertFalse(membresFamilles.hasRequerant());
    }

    @Test
    public void testCountRequerantConjointOnlyWithConjoint() throws Exception {
        membresFamilles.add(build(RoleMembreFamille.CONJOINT));
        membresFamilles.add(build(RoleMembreFamille.ENFANT));
        assertEquals(1, membresFamilles.countRequerantConjoint());
    }

    @Test
    public void testCountRequerantConjointOnlyWithRequerant() throws Exception {
        membresFamilles.add(build(RoleMembreFamille.REQUERANT));
        membresFamilles.add(build(RoleMembreFamille.ENFANT));
        assertEquals(1, membresFamilles.countRequerantConjoint());
    }

    @Test
    public void testCountRequerantConjointOnlyWithRequerantConjoint() throws Exception {
        membresFamilles.add(build(RoleMembreFamille.REQUERANT));
        membresFamilles.add(build(RoleMembreFamille.CONJOINT));
        membresFamilles.add(build(RoleMembreFamille.ENFANT));
        assertEquals(2, membresFamilles.countRequerantConjoint());
    }

    @Test
    public void testCountRequerantConjointOnlyWithEnfant() throws Exception {
        membresFamilles.add(build(RoleMembreFamille.ENFANT));
        assertEquals(0, membresFamilles.countRequerantConjoint());
    }

    @Test
    public void testCountRequerantConjointEmpty() throws Exception {
        assertEquals(0, membresFamilles.countRequerantConjoint());
    }

    @Test
    public void testGetConjoint() throws Exception {
        membresFamilles.add(build(RoleMembreFamille.REQUERANT));
        membresFamilles.add(build(RoleMembreFamille.CONJOINT));
        membresFamilles.add(build(RoleMembreFamille.ENFANT));
        assertEquals(RoleMembreFamille.CONJOINT, membresFamilles.getConjoint().getRoleMembreFamille());
    }

    @Test
    public void testGetConjointNotExist() throws Exception {
        membresFamilles.add(build(RoleMembreFamille.REQUERANT));
        membresFamilles.add(build(RoleMembreFamille.ENFANT));
        assertEquals(RoleMembreFamille.INDEFINIT, membresFamilles.getConjoint().getRoleMembreFamille());
    }

    @Test
    public void testGetRequerant() throws Exception {
        membresFamilles.add(build(RoleMembreFamille.REQUERANT));
        membresFamilles.add(build(RoleMembreFamille.CONJOINT));
        membresFamilles.add(build(RoleMembreFamille.ENFANT));
        assertEquals(RoleMembreFamille.REQUERANT, membresFamilles.getRequerant().getRoleMembreFamille());
    }

    @Test
    public void testGetRequerantNotExist() throws Exception {
        membresFamilles.add(build(RoleMembreFamille.CONJOINT));
        membresFamilles.add(build(RoleMembreFamille.ENFANT));
        assertEquals(RoleMembreFamille.INDEFINIT, membresFamilles.getRequerant().getRoleMembreFamille());
    }

    @Test
    public void testGetMembresFamilles() throws Exception {
        membresFamilles.add(build(RoleMembreFamille.REQUERANT));
        membresFamilles.add(build(RoleMembreFamille.CONJOINT));
        membresFamilles.add(build(RoleMembreFamille.ENFANT));
        assertEquals(3, membresFamilles.getMembresFamilles().size());
    }

    @Test
    public void testGetMemresFamillesEmpty() throws Exception {
        assertEquals(0, membresFamilles.getMembresFamilles().size());
    }

    @Test
    public void testSize() throws Exception {
        membresFamilles.add(build(RoleMembreFamille.REQUERANT));
        membresFamilles.add(build(RoleMembreFamille.CONJOINT));
        membresFamilles.add(build(RoleMembreFamille.ENFANT));
        membresFamilles.add(build(RoleMembreFamille.ENFANT));
        assertEquals(4, membresFamilles.size());
    }

    @Test
    public void testSizeEmpty() throws Exception {
        assertEquals(0, membresFamilles.size());
    }

    @Test
    public void testResolveByRoleFoundRequerant() throws Exception {
        MembreFamille requerant = build(RoleMembreFamille.REQUERANT);
        membresFamilles.add(requerant);
        membresFamilles.add(build(RoleMembreFamille.CONJOINT));
        assertEquals(requerant, membresFamilles.resolveByRole(RoleMembreFamille.REQUERANT));
    }

    @Test
    public void testResolveByRoleNotFound() throws Exception {
        membresFamilles.add(build(RoleMembreFamille.REQUERANT));
        membresFamilles.add(build(RoleMembreFamille.CONJOINT));
        assertTrue(null == membresFamilles.resolveByRole(RoleMembreFamille.ENFANT));
    }

}
