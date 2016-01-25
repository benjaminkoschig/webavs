package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.assuranceVie.AssuranceVie;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;

public class DonneesFinancieresListTest {
    private static DonneesFinancieresListBase<AssuranceVie> list = createList();

    @Test
    public void testGetDonneesForEnfant() throws Exception {
        assertTrue(list.getDonneesForEnfant().get(0).getRoleMembreFamille().isEnfant());
    }

    @Test
    public void testGetDonneesForConjoint() throws Exception {
        assertTrue(list.getDonneesForConjoint().get(0).getRoleMembreFamille().isConjoint());
    }

    @Test
    public void testGetDonneesForRequerant() throws Exception {
        list = createList();
        assertTrue(list.getDonneesForRequerant().get(0).getRoleMembreFamille().isRequerant());
    }

    @Test
    public void testGetDonneesForConjointEnfant() throws Exception {
        list = createList();
        DonneesFinancieresListBase<AssuranceVie> l = list.getDonneesForConjointEnfant();
        assertEquals(2, l.size());
        assertTrue(l.getDonneesForConjoint().get(0).getRoleMembreFamille().isConjoint());
        assertTrue(l.getDonneesForEnfant().get(0).getRoleMembreFamille().isEnfant());
        assertEquals(3, list.size());
    }

    @Test
    public void testGetDonneesForRequerantVide() throws Exception {
        AssuranceVie assuranceVieC = new AssuranceVie(new Montant(0), BuilderDf.createDF(RoleMembreFamille.CONJOINT));
        AssuranceVie assuranceVieE = new AssuranceVie(new Montant(0), BuilderDf.createDF(RoleMembreFamille.ENFANT));
        DonneesFinancieresListBase<AssuranceVie> list = new DonneesFinancieresListBase<AssuranceVie>();

        list.add(assuranceVieC);
        list.add(assuranceVieE);
        assertTrue(list.getDonneesForRequerant().isEmpty());
    }

    private static DonneesFinancieresListBase<AssuranceVie> createList() {
        AssuranceVie assuranceVieR = new AssuranceVie(new Montant(25), BuilderDf.createDF(RoleMembreFamille.REQUERANT));
        AssuranceVie assuranceVieC = new AssuranceVie(new Montant(5), BuilderDf.createDF(RoleMembreFamille.CONJOINT));
        AssuranceVie assuranceVieE = new AssuranceVie(new Montant(30), BuilderDf.createDF(RoleMembreFamille.ENFANT));
        DonneesFinancieresListBase<AssuranceVie> list = new DonneesFinancieresListBase<AssuranceVie>();
        list.add(assuranceVieR);
        list.add(assuranceVieC);
        list.add(assuranceVieE);
        return list;
    }

    // @Test
    // public void testsumRevenuAnnuel() throws Exception {
    // assertEquals(new Montant(60), list.sumRevenuAnnuel());
    // }

}
