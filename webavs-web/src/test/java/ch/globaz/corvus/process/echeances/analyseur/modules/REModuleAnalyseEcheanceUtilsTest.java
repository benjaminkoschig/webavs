package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.echeances.REEcheancesEntity;
import globaz.corvus.db.echeances.RERenteJoinDemandeEcheance;
import globaz.pyxis.api.ITIPersonne;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

public class REModuleAnalyseEcheanceUtilsTest {

    @Test
    public void testCompareAgeAvsAgeMoisCourant() {

        Assert.assertEquals(-1,
                REModuleAnalyseEcheanceUtils.compareAgeAvsAgeMoisCourant(ITIPersonne.CS_FEMME, "01.02.1946", "01.2010"));
        Assert.assertEquals(0,
                REModuleAnalyseEcheanceUtils.compareAgeAvsAgeMoisCourant(ITIPersonne.CS_FEMME, "31.01.1946", "01.2010"));
        Assert.assertEquals(0,
                REModuleAnalyseEcheanceUtils.compareAgeAvsAgeMoisCourant(ITIPersonne.CS_FEMME, "01.01.1946", "01.2010"));
        Assert.assertEquals(1,
                REModuleAnalyseEcheanceUtils.compareAgeAvsAgeMoisCourant(ITIPersonne.CS_FEMME, "31.12.1945", "01.2010"));

        Assert.assertEquals(-1,
                REModuleAnalyseEcheanceUtils.compareAgeAvsAgeMoisCourant(ITIPersonne.CS_HOMME, "01.02.1945", "01.2010"));
        Assert.assertEquals(0,
                REModuleAnalyseEcheanceUtils.compareAgeAvsAgeMoisCourant(ITIPersonne.CS_HOMME, "31.01.1945", "01.2010"));
        Assert.assertEquals(0,
                REModuleAnalyseEcheanceUtils.compareAgeAvsAgeMoisCourant(ITIPersonne.CS_HOMME, "01.01.1945", "01.2010"));
        Assert.assertEquals(1,
                REModuleAnalyseEcheanceUtils.compareAgeAvsAgeMoisCourant(ITIPersonne.CS_HOMME, "31.12.1944", "01.2010"));
    }

    @Test
    public void testGetNbMoisVie() {
        Assert.assertEquals(Integer.valueOf(0), REModuleAnalyseEcheanceUtils.getNbMoisVie("01.01.2010", "01.2010"));
        Assert.assertEquals(Integer.valueOf(1), REModuleAnalyseEcheanceUtils.getNbMoisVie("01.01.2010", "02.2010"));
        Assert.assertEquals(Integer.valueOf(12), REModuleAnalyseEcheanceUtils.getNbMoisVie("01.01.2010", "01.2011"));

        Assert.assertEquals(Integer.valueOf(0), REModuleAnalyseEcheanceUtils.getNbMoisVie("31.01.2010", "01.2010"));
        Assert.assertEquals(Integer.valueOf(1), REModuleAnalyseEcheanceUtils.getNbMoisVie("31.01.2010", "02.2010"));
        Assert.assertEquals(Integer.valueOf(12), REModuleAnalyseEcheanceUtils.getNbMoisVie("31.01.2010", "01.2011"));
    }

    @Test
    public void testGetRenteAnticipeeRecalculee() {

        REEcheancesEntity echeance = new REEcheancesEntity();

        echeance.setDateNaissanceTiers("01.01.1945");
        echeance.setCsSexeTiers(ITIPersonne.CS_HOMME);

        RERenteJoinDemandeEcheance rente1 = new RERenteJoinDemandeEcheance();
        rente1.setIdPrestationAccordee("1");
        rente1.setDateDebutDroit("01.2009");
        rente1.setDateFinDroit("01.2010");
        rente1.setAnneeAnticipation("1");
        rente1.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);

        echeance.getRentesDuTiers().add(rente1);

        RERenteJoinDemandeEcheance rente2 = new RERenteJoinDemandeEcheance();
        rente2.setIdPrestationAccordee("2");
        rente2.setDateDebutDroit("02.2010");
        rente2.setAnneeAnticipation("1");
        rente2.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);

        echeance.getRentesDuTiers().add(rente2);

        Assert.assertEquals(rente2, REModuleAnalyseEcheanceUtils.getRenteAnticipeeRecalculee(echeance));
    }

    @Test
    public void testGetRenteAPIAIEnCoursDurantLeMois() throws Exception {

        REEcheancesEntity entity = new REEcheancesEntity();
        entity.setDateNaissanceTiers("01.05.1950");

        // avec rente principale AVS
        RERenteJoinDemandeEcheance renteVieillessePrincipale = new RERenteJoinDemandeEcheance();
        renteVieillessePrincipale.setIdPrestationAccordee("1");
        renteVieillessePrincipale.setCodePrestation("10");
        renteVieillessePrincipale.setDateDebutDroit("06.2014");
        renteVieillessePrincipale.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);

        // avec rente API-AVS de faible degré
        RERenteJoinDemandeEcheance apiAvsValidee = new RERenteJoinDemandeEcheance();
        apiAvsValidee.setIdPrestationAccordee("2");
        apiAvsValidee.setCodePrestation("89");
        apiAvsValidee.setDateDebutDroit("06.2014");
        apiAvsValidee.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);

        // API AI extraordinaire diminuée
        RERenteJoinDemandeEcheance apiAIextraDiminuee = new RERenteJoinDemandeEcheance();
        apiAIextraDiminuee.setIdPrestationAccordee("3");
        apiAIextraDiminuee.setCodePrestation("81");
        apiAIextraDiminuee.setDateDebutDroit("01.2004");
        apiAIextraDiminuee.setDateFinDroit("05.2014");
        apiAIextraDiminuee.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        apiAIextraDiminuee.setIsPrestationBloquee(true);
        apiAIextraDiminuee.setDateEcheance("05.2014");

        // Rente AI ordinaire diminuée
        RERenteJoinDemandeEcheance renteAIordinaireDiminuee3 = new RERenteJoinDemandeEcheance();
        renteAIordinaireDiminuee3.setIdPrestationAccordee("4");
        renteAIordinaireDiminuee3.setCodePrestation("50");
        renteAIordinaireDiminuee3.setDateDebutDroit("01.2014");
        renteAIordinaireDiminuee3.setDateFinDroit("05.2014");
        renteAIordinaireDiminuee3.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);

        // Rente AI ordinaire diminuée
        RERenteJoinDemandeEcheance renteAIordinaireDiminuee2 = new RERenteJoinDemandeEcheance();
        renteAIordinaireDiminuee2.setIdPrestationAccordee("5");
        renteAIordinaireDiminuee2.setCodePrestation("50");
        renteAIordinaireDiminuee2.setDateDebutDroit("11.2003");
        renteAIordinaireDiminuee2.setDateFinDroit("12.2013");
        renteAIordinaireDiminuee2.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        renteAIordinaireDiminuee2.setDateEcheance("12.2013");

        // API AI en home validée
        RERenteJoinDemandeEcheance apiAIhomeValidee = new RERenteJoinDemandeEcheance();
        apiAIhomeValidee.setIdPrestationAccordee("6");
        apiAIhomeValidee.setCodePrestation("91");
        apiAIhomeValidee.setDateDebutDroit("01.1997");
        apiAIhomeValidee.setDateFinDroit("12.2003");
        apiAIhomeValidee.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);

        // Rente AI ordinaire diminuée
        RERenteJoinDemandeEcheance renteAIordinaireDiminuee = new RERenteJoinDemandeEcheance();
        renteAIordinaireDiminuee.setIdPrestationAccordee("7");
        renteAIordinaireDiminuee.setCodePrestation("50");
        renteAIordinaireDiminuee.setDateDebutDroit("01.1997");
        renteAIordinaireDiminuee.setDateFinDroit("10.2003");
        renteAIordinaireDiminuee.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);

        entity.getRentesDuTiers().addAll(
                Arrays.asList(renteVieillessePrincipale, apiAvsValidee, apiAIextraDiminuee, renteAIordinaireDiminuee3,
                        renteAIordinaireDiminuee2, apiAIhomeValidee, renteAIordinaireDiminuee));

        Assert.assertEquals(apiAIextraDiminuee,
                REModuleAnalyseEcheanceUtils.getRenteAPIAIEnCoursDurantLeMois(entity, "05.2014"));
    }

    @Test
    public void testGetRenteAPIAVSEnCoursDurantLeMois() throws Exception {

        REEcheancesEntity entity = new REEcheancesEntity();
        entity.setDateNaissanceTiers("01.05.1950");

        // avec rente principale AVS
        RERenteJoinDemandeEcheance renteVieillessePrincipale = new RERenteJoinDemandeEcheance();
        renteVieillessePrincipale.setIdPrestationAccordee("1");
        renteVieillessePrincipale.setCodePrestation("10");
        renteVieillessePrincipale.setDateDebutDroit("06.2014");
        renteVieillessePrincipale.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);

        // avec rente API-AVS de faible degré
        RERenteJoinDemandeEcheance apiAvsValidee = new RERenteJoinDemandeEcheance();
        apiAvsValidee.setIdPrestationAccordee("2");
        apiAvsValidee.setCodePrestation("89");
        apiAvsValidee.setDateDebutDroit("06.2014");
        apiAvsValidee.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);

        // API AI extraordinaire diminuée
        RERenteJoinDemandeEcheance apiAIextraDiminuee = new RERenteJoinDemandeEcheance();
        apiAIextraDiminuee.setIdPrestationAccordee("3");
        apiAIextraDiminuee.setCodePrestation("81");
        apiAIextraDiminuee.setDateDebutDroit("01.2004");
        apiAIextraDiminuee.setDateFinDroit("05.2014");
        apiAIextraDiminuee.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        apiAIextraDiminuee.setIsPrestationBloquee(true);
        apiAIextraDiminuee.setDateEcheance("05.2014");

        // Rente AI ordinaire diminuée
        RERenteJoinDemandeEcheance renteAIordinaireDiminuee3 = new RERenteJoinDemandeEcheance();
        renteAIordinaireDiminuee3.setIdPrestationAccordee("4");
        renteAIordinaireDiminuee3.setCodePrestation("50");
        renteAIordinaireDiminuee3.setDateDebutDroit("01.2014");
        renteAIordinaireDiminuee3.setDateFinDroit("05.2014");
        renteAIordinaireDiminuee3.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);

        // Rente AI ordinaire diminuée
        RERenteJoinDemandeEcheance renteAIordinaireDiminuee2 = new RERenteJoinDemandeEcheance();
        renteAIordinaireDiminuee2.setIdPrestationAccordee("5");
        renteAIordinaireDiminuee2.setCodePrestation("50");
        renteAIordinaireDiminuee2.setDateDebutDroit("11.2003");
        renteAIordinaireDiminuee2.setDateFinDroit("12.2013");
        renteAIordinaireDiminuee2.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        renteAIordinaireDiminuee2.setDateEcheance("12.2013");

        // API AI en home validée
        RERenteJoinDemandeEcheance apiAIhomeValidee = new RERenteJoinDemandeEcheance();
        apiAIhomeValidee.setIdPrestationAccordee("6");
        apiAIhomeValidee.setCodePrestation("91");
        apiAIhomeValidee.setDateDebutDroit("01.1997");
        apiAIhomeValidee.setDateFinDroit("12.2003");
        apiAIhomeValidee.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);

        // Rente AI ordinaire diminuée
        RERenteJoinDemandeEcheance renteAIordinaireDiminuee = new RERenteJoinDemandeEcheance();
        renteAIordinaireDiminuee.setIdPrestationAccordee("7");
        renteAIordinaireDiminuee.setCodePrestation("50");
        renteAIordinaireDiminuee.setDateDebutDroit("01.1997");
        renteAIordinaireDiminuee.setDateFinDroit("10.2003");
        renteAIordinaireDiminuee.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);

        entity.getRentesDuTiers().addAll(
                Arrays.asList(renteVieillessePrincipale, apiAvsValidee, apiAIextraDiminuee, renteAIordinaireDiminuee3,
                        renteAIordinaireDiminuee2, apiAIhomeValidee, renteAIordinaireDiminuee));

        Assert.assertEquals(apiAvsValidee,
                REModuleAnalyseEcheanceUtils.getRenteAPIAVSEnCoursDurantLeMois(entity, "06.2014"));
    }

    @Test
    public void testHasRenteEnCours() {
        REEcheancesEntity echeance = new REEcheancesEntity();
        echeance.setDateNaissanceTiers("01.01.1945");

        RERenteJoinDemandeEcheance rente1 = new RERenteJoinDemandeEcheance();
        rente1.setCodePrestation("10");
        rente1.setIdPrestationAccordee("1");
        rente1.setDateDebutDroit("01.2009");
        rente1.setDateFinDroit("01.2010");
        rente1.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        echeance.getRentesDuTiers().add(rente1);

        Assert.assertTrue(REModuleAnalyseEcheanceUtils.hasRenteEnCours(echeance, "12.2009"));
        Assert.assertTrue(REModuleAnalyseEcheanceUtils.hasRenteEnCours(echeance, "01.2010"));
        Assert.assertFalse(REModuleAnalyseEcheanceUtils.hasRenteEnCours(echeance, "02.2010"));

        rente1.setDateFinDroit("");
        rente1.setDateFinDroit("");
        Assert.assertTrue(REModuleAnalyseEcheanceUtils.hasRenteEnCours(echeance, "02.2010"));
    }

    @Test
    public void testHasRenteVieillesseValideMoisSuivant() {

        REEcheancesEntity echeance = new REEcheancesEntity();
        echeance.setDateNaissanceTiers("01.01.1945");
        echeance.setCsSexeTiers(ITIPersonne.CS_HOMME);

        RERenteJoinDemandeEcheance rente1 = new RERenteJoinDemandeEcheance();
        rente1.setIdPrestationAccordee("1");
        rente1.setDateDebutDroit("01.2009");
        rente1.setDateFinDroit("01.2010");
        rente1.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        echeance.getRentesDuTiers().add(rente1);

        // Une seule rente vieillesse finissant dans la mois -> faux
        rente1.setCodePrestation("10");
        Assert.assertFalse(REModuleAnalyseEcheanceUtils.hasRenteVieillesseValideMoisSuivant(echeance, "01.2010"));

        RERenteJoinDemandeEcheance rente2 = new RERenteJoinDemandeEcheance();
        rente2.setIdPrestationAccordee("2");
        rente2.setCodePrestation("10");
        rente2.setDateDebutDroit("02.2010");
        rente2.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        echeance.getRentesDuTiers().add(rente2);
        // une rente vieillesse reprenant le droit le mois suivant (mutation) -> vrai
        Assert.assertTrue(REModuleAnalyseEcheanceUtils.hasRenteVieillesseValideMoisSuivant(echeance, "01.2010"));

        // une rente AI dans le mois précédant (passage à l'âge AVS) -> vrai
        rente1.setCodePrestation("50");
        Assert.assertTrue(REModuleAnalyseEcheanceUtils.hasRenteVieillesseValideMoisSuivant(echeance, "01.2010"));

        // la rente vieillesse n'est pas validé -> faux
        rente2.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
        Assert.assertFalse(REModuleAnalyseEcheanceUtils.hasRenteVieillesseValideMoisSuivant(echeance, "01.2010"));
    }

}
