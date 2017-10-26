package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
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

    @Test
    public void testFiltreForPeriode() throws Exception {
        DonneesFinancieresListBase<AssuranceVie> dhs = new DonneesFinancieresListBase<AssuranceVie>();
        dhs.add(new AssuranceVie(new Montant(0), BuilderDf.createDF(new Date("01.2015"), null)));
        dhs.add(new AssuranceVie(new Montant(10), BuilderDf.createDF(new Date("01.2018"), null)));
        assertEquals(1, dhs.filtreForPeriode(new Date("01.2015"), new Date("01.2015")).size());
        assertEquals(new Date("01.2015"), dhs.filtreForPeriode(new Date("01.2015"), new Date("01.2015")).get(0)
                .getDebut());
    }

    @Test
    public void testFiltreForPeriode2() throws Exception {
        DonneesFinancieresListBase<AssuranceVie> dhs = new DonneesFinancieresListBase<AssuranceVie>();
        dhs.add(new AssuranceVie(new Montant(20), BuilderDf.createDF(new Date("06.2011"), new Date("01.2012"))));
        dhs.add(new AssuranceVie(new Montant(20), BuilderDf.createDF(new Date("01.2011"), new Date("05.2011"))));
        dhs.add(new AssuranceVie(new Montant(20), BuilderDf.createDF(new Date("05.2010"), new Date("12.2010"))));

        assertEquals(1, dhs.filtreForPeriode(new Date("01.2012"), new Date("01.2012")).size());
    }

    @Test
    public void testFiltreForPeriode1() throws Exception {
        DonneesFinancieresListBase<AssuranceVie> dhs = new DonneesFinancieresListBase<AssuranceVie>();
        dhs.add(new AssuranceVie(new Montant(10), BuilderDf.createDF(new Date("01.2015"), new Date("01.2015"))));
        dhs.add(new AssuranceVie(new Montant(20), BuilderDf.createDF(new Date("01.2013"), new Date("12.2014"))));
        dhs.add(new AssuranceVie(new Montant(20), BuilderDf.createDF(new Date("06.2011"), new Date("12.2012"))));
        dhs.add(new AssuranceVie(new Montant(20), BuilderDf.createDF(new Date("01.2011"), new Date("05.2011"))));
        dhs.add(new AssuranceVie(new Montant(20), BuilderDf.createDF(new Date("05.2010"), new Date("12.2010"))));

        assertEquals(1, dhs.filtreForPeriode(new Date("01.2012"), new Date("01.2012")).size());

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

}
