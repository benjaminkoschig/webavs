package ch.globaz.corvus.domaine;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import junit.framework.Assert;
import org.junit.Test;
import ch.globaz.common.domaine.Periode;
import ch.globaz.corvus.TestUnitaireAvecGenerateurIDUnique;
import ch.globaz.corvus.domaine.constantes.CodeCasSpecialRente;
import ch.globaz.corvus.domaine.constantes.EtatDemandeRente;
import ch.globaz.corvus.domaine.constantes.TypePrestationDue;
import ch.globaz.prestation.domaine.CodePrestation;
import ch.globaz.pyxis.domaine.PersonneAVS;

public class DemandeRenteTest extends TestUnitaireAvecGenerateurIDUnique {

    @Test
    public void testComporteDesCreances() throws Exception {
        DemandeRente demande = new DemandeRenteInvalidite();
        demande.setId(genererUnIdUnique());

        Assert.assertFalse(demande.comporteDesCreances());

        RenteAccordee rente1 = new RenteAccordee();
        rente1.setId(genererUnIdUnique());
        rente1.setCodePrestation(CodePrestation.CODE_50);

        demande.setRentesAccordees(Arrays.asList(rente1));

        RepartitionCreance repartitionCreance = new RepartitionCreance();
        repartitionCreance.setId(genererUnIdUnique());

        Creance creance = new Creance();
        creance.setId(genererUnIdUnique());
        repartitionCreance.setCreance(creance);

        rente1.setRepartitionCreance(new HashSet<RepartitionCreance>(Arrays.asList(repartitionCreance)));

        Assert.assertTrue(demande.comporteDesCreances());
    }

    @Test
    public void testComporteDesInteretsMoratoires() throws Exception {
        DemandeRente demande = new DemandeRenteInvalidite();
        demande.setId(genererUnIdUnique());

        Assert.assertFalse(demande.comporteDesInteretsMoratoires());

        RenteAccordee rente1 = new RenteAccordee();
        rente1.setId(genererUnIdUnique());
        rente1.setCodePrestation(CodePrestation.CODE_50);

        demande.setRentesAccordees(Arrays.asList(rente1));
        Assert.assertFalse(demande.comporteDesInteretsMoratoires());

        InteretMoratoire interetMoratoire = new InteretMoratoire();
        interetMoratoire.setId(genererUnIdUnique());

        rente1.setInteretMoratoire(interetMoratoire);
        demande.setRentesAccordees(Arrays.asList(rente1));
        Assert.assertTrue(demande.comporteDesInteretsMoratoires());
    }

    @Test
    public void testComporteDesRentesAccordeesAvecCodeCasSpecial() throws Exception {
        DemandeRente demande = new DemandeRenteInvalidite();
        demande.setId(genererUnIdUnique());

        RenteAccordee rente1 = new RenteAccordee();
        rente1.setId(genererUnIdUnique());
        rente1.setCodePrestation(CodePrestation.CODE_50);

        RenteAccordee rente2 = new RenteAccordee();
        rente2.setId(genererUnIdUnique());
        rente2.setCodePrestation(CodePrestation.CODE_50);

        demande.setRentesAccordees(Arrays.asList(rente1, rente2));

        Assert.assertFalse(demande
                .comporteDesRentesAccordeesAvecCodeCasSpecial(CodeCasSpecialRente.CODE_CAS_SPECIAL_07));

        rente2.ajouterCodeCasSpecial(CodeCasSpecialRente.CODE_CAS_SPECIAL_07);

        Assert.assertTrue(demande.comporteDesRentesAccordeesAvecCodeCasSpecial(CodeCasSpecialRente.CODE_CAS_SPECIAL_07));
    }

    @Test
    public void testComporteUnTrouDansLesPeriodesDeDroitDesRentesAccordees() throws Exception {
        DemandeRente demande = new DemandeRenteVieillesse();
        demande.setId(genererUnIdUnique());

        RenteAccordee rente1 = new RenteAccordee();
        rente1.setId(genererUnIdUnique());
        rente1.setCodePrestation(CodePrestation.CODE_10);
        rente1.setMoisDebut("01.2000");
        rente1.setMoisFin("12.2010");

        RenteAccordee rente2 = new RenteAccordee();
        rente2.setId(genererUnIdUnique());
        rente2.setCodePrestation(CodePrestation.CODE_10);
        rente2.setMoisDebut("01.2011");
        rente2.setMoisFin("");

        demande.setRentesAccordees(Arrays.asList(rente1, rente2));

        Assert.assertFalse(demande.comporteUnTrouDansLesPeriodesDeDroitDesRentesAccordees());

        rente2.setMoisDebut("02.2011");

        Assert.assertTrue(demande.comporteUnTrouDansLesPeriodesDeDroitDesRentesAccordees());
    }

    @Test
    public void testEstEnCours() throws Exception {
        DemandeRente demande = new DemandeRenteInvalidite();
        demande.setId(genererUnIdUnique());

        Assert.assertFalse(demande.estEnCours());

        RenteAccordee rente1 = new RenteAccordee();
        rente1.setId(genererUnIdUnique());
        rente1.setCodePrestation(CodePrestation.CODE_50);
        rente1.setMoisDebut("01.2010");

        demande.setRentesAccordees(Arrays.asList(rente1));
        Assert.assertTrue(demande.estEnCours());

        rente1.setMoisFin("02.2010");
        Assert.assertFalse(demande.estEnCours());
    }

    @Test
    public void testGetPeriodeDuDroitDesRentesAccordees() throws Exception {
        // périodes dans le désordre
        RenteAccordee rente1 = new RenteAccordee();
        rente1.setId(genererUnIdUnique());
        rente1.setCodePrestation(CodePrestation.CODE_50);
        rente1.setMoisDebut("01.2000");
        rente1.setMoisFin("05.2005");

        RenteAccordee rente2 = new RenteAccordee();
        rente2.setId(genererUnIdUnique());
        rente2.setCodePrestation(CodePrestation.CODE_50);
        rente2.setMoisDebut("01.2010");
        rente2.setMoisFin("01.2014");

        RenteAccordee rente3 = new RenteAccordee();
        rente3.setId(genererUnIdUnique());
        rente3.setCodePrestation(CodePrestation.CODE_50);
        rente3.setMoisDebut("06.2005");
        rente3.setMoisFin("12.2009");

        DemandeRente demande = new DemandeRenteInvalidite();
        demande.setId(genererUnIdUnique());
        demande.setRentesAccordees(Arrays.asList(rente1, rente2, rente3));

        Assert.assertEquals(new Periode("01.2000", "01.2014"), demande.getPeriodeDuDroitDesRentesAccordees());

        // avec un trou dans les périodes
        rente3.setMoisDebut("01.2009");
        Assert.assertEquals(new Periode("01.2000", "01.2014"), demande.getPeriodeDuDroitDesRentesAccordees());

        // sans date de fin
        rente2.setMoisFin("");
        Assert.assertEquals(new Periode("01.2000", ""), demande.getPeriodeDuDroitDesRentesAccordees());
    }

    @Test
    public void testGetRenteAccordeePrincipale() throws Exception {
        DemandeRente demande = new DemandeRenteVieillesse();
        demande.setId(genererUnIdUnique());

        Set<RenteAccordee> RentesAccordees = new HashSet<RenteAccordee>();

        RenteAccordee renteVieillessePrincipale = new RenteAccordee();
        renteVieillessePrincipale.setId(genererUnIdUnique());
        renteVieillessePrincipale.setCodePrestation(CodePrestation.CODE_10);

        RenteAccordee renteVieillesseComplementaireConjoint = new RenteAccordee();
        renteVieillesseComplementaireConjoint.setId(genererUnIdUnique());
        renteVieillesseComplementaireConjoint.setCodePrestation(CodePrestation.CODE_33);

        RenteAccordee renteVieillesseComplementaireEnfant = new RenteAccordee();
        renteVieillesseComplementaireEnfant.setId(genererUnIdUnique());
        renteVieillesseComplementaireEnfant.setCodePrestation(CodePrestation.CODE_35);

        RenteAccordee renteAPI1 = new RenteAccordee();
        renteAPI1.setId(genererUnIdUnique());
        renteAPI1.setCodePrestation(CodePrestation.CODE_91);
        renteAPI1.setMoisDebut("01.2010");
        renteAPI1.setMoisFin("12.2012");

        RenteAccordee renteAPI2 = new RenteAccordee();
        renteAPI2.setId(genererUnIdUnique());
        renteAPI2.setCodePrestation(CodePrestation.CODE_96);
        renteAPI2.setMoisDebut("01.2013");

        demande.setRentesAccordees(RentesAccordees);

        try {
            demande.getRenteAccordeePrincipale();
            Assert.fail();
        } catch (Exception ex) {
            // ok
        }

        demande.setRentesAccordees(Arrays.asList(renteVieillessePrincipale));
        Assert.assertEquals(renteVieillessePrincipale, demande.getRenteAccordeePrincipale());

        demande.setRentesAccordees(Arrays.asList(renteVieillessePrincipale, renteVieillesseComplementaireConjoint));
        Assert.assertEquals(renteVieillessePrincipale, demande.getRenteAccordeePrincipale());

        demande.setRentesAccordees(Arrays.asList(renteVieillessePrincipale, renteVieillesseComplementaireConjoint,
                renteVieillesseComplementaireEnfant));
        Assert.assertEquals(renteVieillessePrincipale, demande.getRenteAccordeePrincipale());

        demande.setRentesAccordees(Arrays.asList(renteVieillesseComplementaireConjoint));
        Assert.assertEquals(renteVieillesseComplementaireConjoint, demande.getRenteAccordeePrincipale());

        demande.setRentesAccordees(Arrays.asList(renteVieillesseComplementaireConjoint,
                renteVieillesseComplementaireEnfant));
        Assert.assertEquals(renteVieillesseComplementaireConjoint, demande.getRenteAccordeePrincipale());

        demande.setRentesAccordees(Arrays.asList(renteVieillesseComplementaireEnfant));
        Assert.assertEquals(renteVieillesseComplementaireEnfant, demande.getRenteAccordeePrincipale());

        demande = new DemandeRenteAPI();
        demande.setId(genererUnIdUnique());

        demande.setRentesAccordees(Arrays.asList(renteAPI1));
        Assert.assertEquals(renteAPI1, demande.getRenteAccordeePrincipale());

        demande.setRentesAccordees(Arrays.asList(renteAPI1, renteAPI2));
        Assert.assertEquals(renteAPI2, demande.getRenteAccordeePrincipale());
        Assert.assertNotSame(renteAPI1, demande.getRenteAccordeePrincipale());
    }

    @Test
    public void testComporteDesRentesAccordeesNeCorrespondantPasAuTypeDeLaDemande() throws Exception {
        DemandeRente demandeInvalidite = new DemandeRenteInvalidite();
        demandeInvalidite.setId(genererUnIdUnique());

        RenteAccordee rente50 = new RenteAccordee();
        rente50.setId(genererUnIdUnique());
        rente50.setCodePrestation(CodePrestation.CODE_50);

        RenteAccordee rente10 = new RenteAccordee();
        rente10.setId(genererUnIdUnique());
        rente10.setCodePrestation(CodePrestation.CODE_10);

        demandeInvalidite.setRentesAccordees(Arrays.asList(rente50));

        Assert.assertFalse("Il n'y a que des rentes d'invalidité dans cette demande, doit retourner faux",
                demandeInvalidite.comporteDesRentesAccordeesNeCorrespondantPasAuTypeDeLaDemande());

        demandeInvalidite.setRentesAccordees(Arrays.asList(rente10));

        Assert.assertTrue("Il y a une rente 10 et une rente 50, doit retourner vrai",
                demandeInvalidite.comporteDesRentesAccordeesNeCorrespondantPasAuTypeDeLaDemande());
    }

    @Test
    public void testRentesAccordeesNeCorrespondantPasAuTypeDeLaDemande() throws Exception {
        DemandeRente demandeInvalidite = new DemandeRenteInvalidite();
        demandeInvalidite.setId(genererUnIdUnique());

        RenteAccordee renteInvalidite = new RenteAccordee();
        renteInvalidite.setId(genererUnIdUnique());
        renteInvalidite.setCodePrestation(CodePrestation.CODE_50);

        RenteAccordee renteSurvivant = new RenteAccordee();
        renteSurvivant.setId(genererUnIdUnique());
        renteSurvivant.setCodePrestation(CodePrestation.CODE_13);

        RenteAccordee renteVieillesse = new RenteAccordee();
        renteVieillesse.setId(genererUnIdUnique());
        renteVieillesse.setCodePrestation(CodePrestation.CODE_10);

        RenteAccordee renteAPI = new RenteAccordee();
        renteAPI.setId(genererUnIdUnique());
        renteAPI.setCodePrestation(CodePrestation.CODE_81);

        demandeInvalidite.setRentesAccordees(Arrays.asList(renteInvalidite, renteSurvivant, renteVieillesse, renteAPI));

        Set<RenteAccordee> rentesNeCorrespondantPasAuTypeDeLaDemande = demandeInvalidite
                .filtrerRentesAccordeesNeCorrespondantPasAuTypeDeLaDemande();

        Assert.assertTrue("La rente API n'est pas une rente d'invalidité",
                rentesNeCorrespondantPasAuTypeDeLaDemande.contains(renteAPI));
        Assert.assertTrue("La rente de vieillesse n'est pas une rente d'invalidité",
                rentesNeCorrespondantPasAuTypeDeLaDemande.contains(renteVieillesse));
        Assert.assertTrue("La rente de survivant n'est pas une rente d'invalidité",
                rentesNeCorrespondantPasAuTypeDeLaDemande.contains(renteSurvivant));
        Assert.assertFalse("La rente d'invalidité est dans la demande du bon type",
                rentesNeCorrespondantPasAuTypeDeLaDemande.contains(renteInvalidite));
    }

    @Test
    public void testAjouterRenteAccordee() throws Exception {
        DemandeRente demande = new DemandeRenteInvalidite();
        demande.setId(genererUnIdUnique());

        try {
            demande.ajouterRenteAccordee(null);
            Assert.fail("Une exception doit être levée si la valeur d'entrée est null");
        } catch (NullPointerException ex) {
            // ok
        }

        RenteAccordee renteAccordee = new RenteAccordee();
        renteAccordee.setId(genererUnIdUnique());

        demande.setEtat(EtatDemandeRente.ENREGISTRE);
        demande.ajouterRenteAccordee(renteAccordee);

        demande.setEtat(EtatDemandeRente.AU_CALCUL);
        demande.ajouterRenteAccordee(renteAccordee);

        demande.setEtat(EtatDemandeRente.CALCULE);
        demande.ajouterRenteAccordee(renteAccordee);

        try {
            demande.setEtat(EtatDemandeRente.COURANT_VALIDE);
            demande.ajouterRenteAccordee(renteAccordee);

            Assert.fail("Ne doit pas être permis");
        } catch (IllegalStateException ex) {
            // ok
        }
        try {
            demande.setEtat(EtatDemandeRente.VALIDE);
            demande.ajouterRenteAccordee(renteAccordee);

            Assert.fail("Ne doit pas être permis");
        } catch (IllegalStateException ex) {
            // ok
        }
        try {
            demande.setEtat(EtatDemandeRente.TERMINE);
            demande.ajouterRenteAccordee(renteAccordee);

            Assert.fail("Ne doit pas être permis");
        } catch (IllegalStateException ex) {
            // ok
        }
        try {
            demande.setEtat(EtatDemandeRente.TRANSFERE);
            demande.ajouterRenteAccordee(renteAccordee);

            Assert.fail("Ne doit pas être permis");
        } catch (IllegalStateException ex) {
            // ok
        }
    }

    @Test
    public void testRetirerRenteAccordee() throws Exception {
        DemandeRente demande = new DemandeRenteInvalidite();
        demande.setId(genererUnIdUnique());

        try {
            demande.retirerRenteAccordee(null);
            Assert.fail("Une exception doit être levée si la valeur d'entrée est null");
        } catch (NullPointerException ex) {
            // ok
        }

        RenteAccordee renteAccordee = new RenteAccordee();
        renteAccordee.setId(genererUnIdUnique());

        demande.setRentesAccordees(Arrays.asList(renteAccordee));
        demande.setEtat(EtatDemandeRente.ENREGISTRE);
        Assert.assertTrue("Cette rente accordée fait partie de cette demande, et doit donc pouvoir en être retirer",
                demande.retirerRenteAccordee(renteAccordee));

        demande.setRentesAccordees(Arrays.asList(renteAccordee));
        demande.setEtat(EtatDemandeRente.AU_CALCUL);
        Assert.assertTrue("Cette rente accordée fait partie de cette demande, et doit donc pouvoir en être retirer",
                demande.retirerRenteAccordee(renteAccordee));

        demande.setRentesAccordees(Arrays.asList(renteAccordee));
        demande.setEtat(EtatDemandeRente.CALCULE);
        Assert.assertTrue("Cette rente accordée fait partie de cette demande, et doit donc pouvoir en être retirer",
                demande.retirerRenteAccordee(renteAccordee));

        try {
            demande.setRentesAccordees(Arrays.asList(renteAccordee));
            demande.setEtat(EtatDemandeRente.COURANT_VALIDE);
            demande.retirerRenteAccordee(renteAccordee);

            Assert.fail("La demande ne doit pas pouvoir être modifiable dans l'état où elle est");
        } catch (IllegalStateException ex) {
            // ok
        }

        try {
            demande.setRentesAccordees(Arrays.asList(renteAccordee));
            demande.setEtat(EtatDemandeRente.VALIDE);
            demande.retirerRenteAccordee(renteAccordee);

            Assert.fail("La demande ne doit pas pouvoir être modifiable dans l'état où elle est");
        } catch (IllegalStateException ex) {
            // ok
        }

        try {
            demande.setRentesAccordees(Arrays.asList(renteAccordee));
            demande.setEtat(EtatDemandeRente.TERMINE);
            demande.retirerRenteAccordee(renteAccordee);

            Assert.fail("La demande ne doit pas pouvoir être modifiable dans l'état où elle est");
        } catch (IllegalStateException ex) {
            // ok
        }

        try {
            demande.setRentesAccordees(Arrays.asList(renteAccordee));
            demande.setEtat(EtatDemandeRente.TRANSFERE);
            demande.retirerRenteAccordee(renteAccordee);

            Assert.fail("La demande ne doit pas pouvoir être modifiable dans l'état où elle est");
        } catch (IllegalStateException ex) {
            // ok
        }

    }

    @Test
    public void testGetRentesAccordeesDuBeneficiaire() throws Exception {
        DemandeRente demande = new DemandeRenteInvalidite();
        demande.setId(genererUnIdUnique());

        try {
            demande.getRentesAccordeesDuBeneficiaire(null);
            Assert.fail("doit lever une exception car la valeur d'entrée est null");
        } catch (NullPointerException ex) {
            // ok
        }

        PersonneAVS personne1 = new PersonneAVS();

        try {
            demande.getRentesAccordeesDuBeneficiaire(personne1);
            Assert.fail("doit lever une exception car le bénéficiaire n'as pas d'ID");
        } catch (IllegalArgumentException ex) {
            // ok
        }

        personne1.setId(genererUnIdUnique());

        try {
            Assert.assertTrue("La liste retournée doit être vide", demande.getRentesAccordeesDuBeneficiaire(personne1)
                    .isEmpty());
        } catch (NullPointerException ex) {
            Assert.fail("La liste retournée doit être non null");
        }

        RenteAccordee renteAccordee1Personne1 = new RenteAccordee();
        renteAccordee1Personne1.setId(genererUnIdUnique());
        renteAccordee1Personne1.setBeneficiaire(personne1);

        demande.setRentesAccordees(Arrays.asList(renteAccordee1Personne1));

        Assert.assertTrue("la rente doit être retournée car elle est au bénéfice de personne1", demande
                .getRentesAccordeesDuBeneficiaire(personne1).contains(renteAccordee1Personne1));

        PersonneAVS personne2 = new PersonneAVS();
        personne2.setId(genererUnIdUnique());

        Assert.assertTrue(
                "la rente ne doit pas être retournée car elle est au bénéfice de personne1 et pas de personne2",
                demande.getRentesAccordeesDuBeneficiaire(personne2).isEmpty());
    }

    @Test
    public void testComporteDesRentesAccordeesCommencantAvantCeMois() throws Exception {

        DemandeRente demande = new DemandeRenteInvalidite();
        demande.setId(genererUnIdUnique());

        try {
            demande.comporteDesRentesAccordeesCommencantAvantCeMois(null);
            Assert.fail("doit lever une exception si le mois est null");
        } catch (NullPointerException ex) {
            // ok
        }

        try {
            demande.comporteDesRentesAccordeesCommencantAvantCeMois("abc");
            Assert.fail("doit lever une exception si le mois n'est pas au bon format");
        } catch (IllegalArgumentException ex) {
            // ok
        }

        RenteAccordee rentePassee1 = new RenteAccordee();
        rentePassee1.setId(genererUnIdUnique());
        rentePassee1.setCodePrestation(CodePrestation.CODE_50);
        rentePassee1.setMoisDebut("01.2015");

        Assert.assertFalse("doit retourner faux si pas de rente dans la demande",
                demande.comporteDesRentesAccordeesCommencantAvantCeMois("12.2014"));

        demande.ajouterRenteAccordee(rentePassee1);

        Assert.assertFalse("doit retourner faux si la rente commence dans le mois",
                demande.comporteDesRentesAccordeesCommencantAvantCeMois("01.2015"));
        Assert.assertFalse("doit retourner faux si la rente commence après le mois",
                demande.comporteDesRentesAccordeesCommencantAvantCeMois("12.2014"));
        Assert.assertTrue("doit retourner vrai si la rente commence avant le mois",
                demande.comporteDesRentesAccordeesCommencantAvantCeMois("02.2015"));
    }

    @Test
    public void testComporteDesRentesAccordeesCommencantApresCeMois() throws Exception {

        DemandeRente demande = new DemandeRenteInvalidite();
        demande.setId(genererUnIdUnique());

        try {
            demande.comporteDesRentesAccordeesCommencantApresCeMois(null);
            Assert.fail("doit lever une exception si le mois est null");
        } catch (NullPointerException ex) {
            // ok
        }

        try {
            demande.comporteDesRentesAccordeesCommencantApresCeMois("abc");
            Assert.fail("doit lever une exception si le mois n'est pas au bon format");
        } catch (IllegalArgumentException ex) {
            // ok
        }

        RenteAccordee renteFuture1 = new RenteAccordee();
        renteFuture1.setId(genererUnIdUnique());
        renteFuture1.setCodePrestation(CodePrestation.CODE_50);
        renteFuture1.setMoisDebut("01.2015");

        Assert.assertFalse("doit retourner faux si pas de rente dans la demande",
                demande.comporteDesRentesAccordeesCommencantApresCeMois("12.2014"));

        demande.ajouterRenteAccordee(renteFuture1);

        Assert.assertFalse("doit retourner faux si la rente commence dans le mois",
                demande.comporteDesRentesAccordeesCommencantApresCeMois("01.2015"));
        Assert.assertTrue("doit retourner vrai si la rente commence après le mois",
                demande.comporteDesRentesAccordeesCommencantApresCeMois("12.2014"));
        Assert.assertFalse("doit retourner faux si la rente commence avant le mois",
                demande.comporteDesRentesAccordeesCommencantApresCeMois("02.2015"));
    }

    @Test
    public void testComporteDesRentesAccordeesCommencantDansCeMois() throws Exception {

        DemandeRente demande = new DemandeRenteInvalidite();
        demande.setId(genererUnIdUnique());

        try {
            demande.comporteDesRentesAccordeesCommencantDansCeMois(null);
            Assert.fail("doit lever une exception si le mois est null");
        } catch (NullPointerException ex) {
            // ok
        }

        try {
            demande.comporteDesRentesAccordeesCommencantDansCeMois("abc");
            Assert.fail("doit lever une exception si le mois n'est pas au bon format");
        } catch (IllegalArgumentException ex) {
            // ok
        }

        RenteAccordee renteActuelle = new RenteAccordee();
        renteActuelle.setId(genererUnIdUnique());
        renteActuelle.setCodePrestation(CodePrestation.CODE_50);
        renteActuelle.setMoisDebut("01.2015");

        Assert.assertFalse("doit retourner faux si pas de rente dans la demande",
                demande.comporteDesRentesAccordeesCommencantDansCeMois("12.2014"));

        demande.ajouterRenteAccordee(renteActuelle);

        Assert.assertTrue("doit retourner vrai si la rente commence dans le mois",
                demande.comporteDesRentesAccordeesCommencantDansCeMois("01.2015"));
        Assert.assertFalse("doit retourner faux si la rente commence après le mois",
                demande.comporteDesRentesAccordeesCommencantDansCeMois("12.2014"));
        Assert.assertFalse("doit retourner faux si la rente commence avant le mois",
                demande.comporteDesRentesAccordeesCommencantDansCeMois("02.2015"));
    }

    @Test
    public void testGetMontantRetroactif() throws Exception {
        DemandeRente demande = new DemandeRenteInvalidite();
        demande.setId(genererUnIdUnique());

        RenteAccordee rente1 = new RenteAccordee();
        rente1.setId(genererUnIdUnique());
        rente1.setCodePrestation(CodePrestation.CODE_50);
        rente1.setMoisDebut("01.2014");

        PrestationDue montantMensuel = new PrestationDue();
        montantMensuel.setId(genererUnIdUnique());
        montantMensuel.setType(TypePrestationDue.PAIEMENT_MENSUEL);
        montantMensuel.setPeriode(new Periode("10.2014", ""));
        montantMensuel.setMontant(BigDecimal.TEN);

        rente1.ajouterPrestationDue(montantMensuel);

        demande.ajouterRenteAccordee(rente1);

        Assert.assertEquals(BigDecimal.ZERO, demande.getMontantRetroactif());

        PrestationDue retro = new PrestationDue();
        retro.setId(genererUnIdUnique());
        retro.setType(TypePrestationDue.MONTANT_RETROACTIF_TOTAL);
        retro.setPeriode(new Periode("01.2014", "09.2014"));
        retro.setMontant(BigDecimal.ONE);

        rente1.ajouterPrestationDue(retro);

        Assert.assertEquals(BigDecimal.ONE, demande.getMontantRetroactif());
    }
}
