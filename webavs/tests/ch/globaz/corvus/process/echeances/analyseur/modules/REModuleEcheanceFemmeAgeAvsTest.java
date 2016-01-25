package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.echeances.REEcheancesEntity;
import globaz.corvus.db.echeances.RERenteJoinDemandeEcheance;
import globaz.pyxis.api.ITIPersonne;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;

public class REModuleEcheanceFemmeAgeAvsTest extends REModuleAnalyseEcheanceTest {

    private RERenteJoinDemandeEcheance rente;

    public REModuleEcheanceFemmeAgeAvsTest() {
        super();
    }

    @Test
    public void femmeAgeAvsDepasseAvantEtApres2012() {
        // le but �tant de v�rifier qu'on ignore les gens sans rentes d�s lors que leur droit est survenu avant le
        // 01.01.2012. Ceci afin d'�viter que tous les cas de reprises de donn�es sans date de d�c�s ne viennent polluer
        // la liste.
        module.setMoisTraitement("02.2012");

        // test avec une femme ayant 64 ans apr�s le 1er janvier 2012
        entity.setDateNaissanceTiers("01.01.1948");

        rente.setCodePrestation("50");
        assertTrue(module, entity, "01.2013", REMotifEcheance.FemmeAgeAvsDepasse);

        rente.setCodePrestation("10");
        assertFalse(module, entity, "01.2013");

        rente.setAnneeAnticipation("2");
        assertTrue(module, entity, "01.2013", REMotifEcheance.FemmeAgeAvsAnticipationDepassee);
        rente.setAnneeAnticipation("0");

        rente.setCodePrestation("33");
        assertFalse(module, entity, "01.2013");

        rente.setCodePrestation("10");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
        assertTrue(module, entity, "01.2013", REMotifEcheance.FemmeAgeAvsDepasse);

        // test avec une femme ayant 64 ans avant le 1er janvier 2012
        entity.setDateNaissanceTiers("31.12.1947");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);

        rente.setCodePrestation("50");
        assertFalse(module, entity, "01.2013");

        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        rente.setDateFinDroit("31.12.2011");
        assertFalse(module, entity, "01.2013");

        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_AJOURNE);
        rente.setCodePrestation("10");
        rente.setDateRevocationAjournement("01.02.2012");
        assertFalse(module, entity, "01.2013");

        // m�me test mais avec cette fois ci la propri�t� en DB assign�e un jour avant la date par d�faut
        module = new REModuleEcheanceFemmeAgeAvs(session, "01.2013", "31.12.2011");
        assertTrue(module, entity, "01.2013", REMotifEcheance.FemmeAgeAvsDepasse);
    }

    @Test
    public void femmeAvecRenteAIeteinteAVSenCoursEtAPIAIenCours() { // Nouveau test
        // une femme ayant 64 ans au mois de mai 2014
        entity.setDateNaissanceTiers("01.05.1950");

        // avec une API-AI en cours
        rente.setCodePrestation("81"); // Rente API (degr� faible) AI
        rente.setDateDebutDroit("01.2010");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);

        // Avec rente invalidit� ordinaire jusqu'� mai 2014
        RERenteJoinDemandeEcheance renteAIechue = new RERenteJoinDemandeEcheance();
        renteAIechue.setIdPrestationAccordee("2");
        renteAIechue.setDateDebutDroit("01.2010");
        renteAIechue.setDateFinDroit("05.2014");
        renteAIechue.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        renteAIechue.setCodePrestation("50"); // Rente invalidit� ordinaire

        // Avec rente AVS ordinaire depuis juin 2014
        RERenteJoinDemandeEcheance renteAVSenCours = new RERenteJoinDemandeEcheance();
        renteAVSenCours.setIdPrestationAccordee("3");
        renteAVSenCours.setDateDebutDroit("06.2014");
        renteAVSenCours.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        renteAVSenCours.setCodePrestation("10");

        entity.getRentesDuTiers().add(renteAVSenCours);
        entity.getRentesDuTiers().add(renteAIechue);

        assertTrue(module, entity, "05.2014", REMotifEcheance.FemmeArrivantAgeAvsAvecApiAi);
    }

    @Test
    public void femmeAvecRenteAPI() {
        // test avec une API AI en cours, et une femme ayant 64 ans dans le mois courant
        entity.setDateNaissanceTiers("01.01.1949");
        rente.setCodePrestation("82");
        rente.setAnneeAnticipation("");
        assertTrue(module, entity, "01.2013", REMotifEcheance.FemmeArrivantAgeAvsAvecApiAi);

        // Avec une API-AVS
        rente.setCodePrestation("86");
        assertTrue(module, entity, "01.2013", REMotifEcheance.FemmeArrivantAgeAvs);

        // test avec une rente AVS mais une API AI
        rente.setCodePrestation("81");
        RERenteJoinDemandeEcheance renteVieillesse = new RERenteJoinDemandeEcheance();
        renteVieillesse.setIdPrestationAccordee("2");
        renteVieillesse.setCodePrestation("10");
        renteVieillesse.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        entity.getRentesDuTiers().add(renteVieillesse);
        assertTrue(module, entity, "01.2013", REMotifEcheance.FemmeArrivantAgeAvsAvecApiAi);
    }

    @Test
    public void femmeAvecRenteTransferee() {
        // femme ayant 64 ans en janvier 2013 avec une rente AI transf�r�e
        entity.setDateNaissanceTiers("01.01.1949");
        rente.setCodePrestation("50");
        rente.setDateDebutDroit("01.2010");
        rente.setDateFinDroit("12.2011");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        rente.setCsEtatDemandeRente(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE);

        assertFalse(module, entity, "01.2013");

        // femme ayant d�j� 64 en janvier 2013 avec une rente AVS transf�r�e
        entity.setDateNaissanceTiers("31.12.1948");
        rente.setCodePrestation("10");
        rente.setDateDebutDroit("01.2010");
        rente.setDateFinDroit("12.2011");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        rente.setCsEtatDemandeRente(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE);

        assertFalse(module, entity, "01.2013");
    }

    @Test
    public void femmeAvecRenteVieillesseAjournee() {

        // test avec une rente ajourn�e dans le mois, et une femme ayant 64 ans dans ce mois
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_AJOURNE);
        assertFalse(module, entity, "01.2013");
    }

    @Test
    public void femmeAvecRenteVieillesseFuture() {
        // test avec une rente qui n'est pas en cours et une femme ayant d�j� 64 ans dans le mois courant
        rente.setCodePrestation("10");
        rente.setDateDebutDroit("02.2013");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        entity.setDateNaissanceTiers("31.12.1948");

        assertTrue(module, entity, "01.2013", REMotifEcheance.FemmeAgeAvsDepasse);
    }

    @Test
    public void femmeDossierDejaTraite() {
        entity.setDateNaissanceTiers("01.01.1949");
        rente.setDateDebutDroit("12.2012");
        rente.setDateFinDroit("01.2013");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        rente.setDateEcheance("01.2013");

        RERenteJoinDemandeEcheance rente2 = new RERenteJoinDemandeEcheance();
        rente2.setIdPrestationAccordee("2");
        rente2.setDateDebutDroit("02.2013");
        rente2.setCodePrestation("10");
        rente2.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);

        entity.getRentesDuTiers().add(rente2);

        rente.setCodePrestation("50");
        assertFalse(module, entity, "01.2013");

        rente.setCodePrestation("13");
        assertFalse(module, entity, "01.2013");

        rente.setCodePrestation("10");
        assertFalse(module, entity, "01.2013");

        // avec �ge AVS d�pass�
        entity.setDateNaissanceTiers("31.12.1948");

        rente.setCodePrestation("50");
        assertFalse(module, entity, "01.2013");

        rente.setCodePrestation("13");
        assertFalse(module, entity, "01.2013");

        rente.setCodePrestation("10");
        assertFalse(module, entity, "01.2013");
    }

    @Test
    public void femmePassantDeAIaAVS() {
        rente.setCodePrestation("50");
        rente.setDateDebutDroit("01.2012");
        rente.setDateFinDroit("12.2012");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        rente.setDateEcheance("12.2012");

        entity.setDateNaissanceTiers("31.12.1948");

        RERenteJoinDemandeEcheance rente2 = new RERenteJoinDemandeEcheance();
        rente2.setCodePrestation("10");
        rente2.setDateDebutDroit("01.2013");
        rente2.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        entity.getRentesDuTiers().add(rente2);

        assertFalse(module, entity, "01.2013");

        entity.setDateNaissanceTiers("01.01.1949");
        rente.setDateFinDroit("01.2013");
        rente2.setDateDebutDroit("02.2013");

        assertFalse(module, entity, "01.2013");
    }

    @Test
    public void femmeRenteAI() {
        // un homme avec une rente AI principale
        rente.setCodePrestation("50");

        // test avec une femme ayant 64 ans, 11 mois et 31 jours dans le mois courant
        entity.setDateNaissanceTiers("01.02.1949");
        assertFalse(module, entity, "01.2013");

        // test avec une femme ayant 64 ans dans le mois courant
        entity.setDateNaissanceTiers("31.01.1949");
        assertTrue(module, entity, "01.2013", REMotifEcheance.FemmeArrivantAgeAvs);

        // test avec une femme ayant 64 ans et 31 jours dans le mois courant
        entity.setDateNaissanceTiers("01.01.1949");
        assertTrue(module, entity, "01.2013", REMotifEcheance.FemmeArrivantAgeAvs);

        // test avec une femme ayant 64 ans et 1 jour dans le mois courant
        entity.setDateNaissanceTiers("31.12.1948");
        assertTrue(module, entity, "01.2013", REMotifEcheance.FemmeAgeAvsDepasse);
    }

    @Test
    public void femmeRenteAnticipe() {
        // une femme avec une rente vieillesse anticip�e
        rente.setCodePrestation("10");

        // test avec une femme ayant 64ans moins un jour dans le mois courant
        entity.setDateNaissanceTiers("01.02.1949");
        assertFalse(module, entity, "01.2013");

        // test avec une femme ayant 64ans moins un jour dans le mois courant (avec ann�e d'anticipation forc�e � z�ro)
        rente.setAnneeAnticipation("0");
        assertFalse(module, entity, "01.2013");

        // avec une ann�e d'anticipation
        rente.setAnneeAnticipation("1");

        // test avec une femme ayant 64ans moins un jour dans le mois courant
        entity.setDateNaissanceTiers("01.02.1949");
        assertFalse(module, entity, "01.2013");

        // test avec une femme ayant 64 ans dans le mois courant
        entity.setDateNaissanceTiers("01.01.1949");
        assertTrue(module, entity, "01.2013", REMotifEcheance.FemmeArrivantAgeAvsRenteAnticipee);

        // test avec une femme ayant 64 ans dans le mois courant
        entity.setDateNaissanceTiers("31.01.1949");
        assertTrue(module, entity, "01.2013", REMotifEcheance.FemmeArrivantAgeAvsRenteAnticipee);

        // test avec une femme ayant 64 ans et 1 mois
        entity.setDateNaissanceTiers("31.12.1948");
        assertTrue(module, entity, "01.2013", REMotifEcheance.FemmeAgeAvsAnticipationDepassee);

        // test avec une femme ayant 64 ans dans le mois courant et une rente de vieillesse
        // reprenant le droit le mois suivant
        entity.setDateNaissanceTiers("01.01.1949");
        rente.setDateDebutDroit("01.2012");
        rente.setDateFinDroit("01.2013");
        RERenteJoinDemandeEcheance rente2 = new RERenteJoinDemandeEcheance();
        rente2.setIdPrestationAccordee("2");
        rente2.setCodePrestation("10");
        rente2.setAnneeAnticipation("1");
        rente2.setDateDebutDroit("02.2013");
        rente2.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        entity.getRentesDuTiers().add(rente2);
        assertFalse(module, entity, "01.2013");
    }

    @Test
    public void femmeRenteAvecMutation() {

        // cas �ge AVS d�pass� si mauvaise d�tection
        entity.setDateNaissanceTiers("01.12.1948");
        rente.setCodePrestation("10");
        rente.setDateDebutDroit("12.2012");
        rente.setDateFinDroit("01.2013");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        rente.setDateEcheance("01.2013");

        RERenteJoinDemandeEcheance rente2 = new RERenteJoinDemandeEcheance();
        rente2.setIdPrestationAccordee("2");
        rente2.setCodePrestation("10");
        rente2.setDateDebutDroit("02.2013");
        rente2.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);

        entity.getRentesDuTiers().add(rente2);

        assertFalse(module, entity, "01.2013");

        // cas �ge AVS dans le mois de traitement si mauvaise d�tection
        entity.setDateNaissanceTiers("01.01.1949");
        assertFalse(module, entity, "01.2013");
    }

    @Test
    public void femmeRentesVeuf() {
        // un homme avec une rente de veuf
        rente.setCodePrestation("13");

        // test avec une femme ayant 64 ans, 11 mois et 31 jours dans le mois courant
        entity.setDateNaissanceTiers("01.02.1949");
        assertFalse(module, entity, "01.2013");

        // test avec une femme ayant 64 ans dans le mois courant
        entity.setDateNaissanceTiers("31.01.1949");
        assertTrue(module, entity, "01.2013", REMotifEcheance.FemmeArrivantAgeAvs);

        // test avec une femme ayant 64 ans et 31 jours dans le mois courant
        entity.setDateNaissanceTiers("01.01.1949");
        assertTrue(module, entity, "01.2013", REMotifEcheance.FemmeArrivantAgeAvs);

        // test avec une femme ayant 64 ans et 1 jour dans le mois courant
        // si d�but du droit AVS ant�rieur au 1er janvier 2012, on ignore le cas car trop de faux-positifs d� � la non
        // implementation des rentes de veuve perdure.
        // Depuis janvier 2012, l'application g�re les rentes de veuve perdure, et ces cas sont d�sormais en erreur
        entity.setDateNaissanceTiers("31.12.1947");
        assertFalse(module, entity, "01.2013");

        module.setMoisTraitement("02.2012");
        entity.setDateNaissanceTiers("01.01.1948");
        assertTrue(module, entity, "01.2013", REMotifEcheance.FemmeAgeAvsDepasse);
    }

    @Test
    public void femmeRentesVieillesseComplementaire() {
        // une femme avec une rente vieillesse compl�mentaire
        rente.setCodePrestation("33");

        // test avec une femme ayant 63 ans, 11 mois et 31 jours dans le mois courant
        entity.setDateNaissanceTiers("01.02.1949");
        assertFalse(module, entity, "01.2013");

        // test avec une femme ayant 64 ans dans le mois courant
        entity.setDateNaissanceTiers("31.01.1949");
        assertTrue(module, entity, "01.2013", REMotifEcheance.FemmeArrivantAgeAvs);

        // test avec une femme ayant 64 ans et 31 jours dans le mois courant
        entity.setDateNaissanceTiers("01.01.1949");
        assertTrue(module, entity, "01.2013", REMotifEcheance.FemmeArrivantAgeAvs);

        // test avec une femme ayant 64 ans et 1 jour dans le mois courant
        // doit retourner faux, car ce cas est assez courant (dans le cas d'une b�n�ficiaire f�minine) et aura une
        // implementation propre dans l'application (rente compl�mentaire vieillesse perdure) dans un futur proche
        entity.setDateNaissanceTiers("31.12.1948");
        assertFalse(module, entity, "01.2013");
    }

    @Test
    public void femmeVersAVSavecApiAiEtApiAvs() {
        entity.setDateNaissanceTiers("01.05.1950");

        // avec rente principale AVS
        rente.setCodePrestation("10");
        rente.setDateDebutDroit("06.2014");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);

        // avec rente API-AVS de faible degr�
        RERenteJoinDemandeEcheance apiAvsValidee = new RERenteJoinDemandeEcheance();
        apiAvsValidee.setIdPrestationAccordee("2000");
        apiAvsValidee.setDateDebutDroit("06.2014");
        apiAvsValidee.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        apiAvsValidee.setCodePrestation("89");

        // API AI extraordinaire diminu�e
        RERenteJoinDemandeEcheance apiAIextraDiminuee = new RERenteJoinDemandeEcheance();
        apiAIextraDiminuee.setIdPrestationAccordee("3");
        apiAIextraDiminuee.setDateDebutDroit("01.2004");
        apiAIextraDiminuee.setDateFinDroit("05.2014");
        apiAIextraDiminuee.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        apiAIextraDiminuee.setCodePrestation("81");
        apiAIextraDiminuee.setIsPrestationBloquee(true);
        apiAIextraDiminuee.setDateEcheance("05.2014");

        // Rente AI ordinaire diminu�e
        RERenteJoinDemandeEcheance renteAIordinaireDiminuee3 = new RERenteJoinDemandeEcheance();
        renteAIordinaireDiminuee3.setIdPrestationAccordee("4");
        renteAIordinaireDiminuee3.setDateDebutDroit("01.2014");
        renteAIordinaireDiminuee3.setDateFinDroit("05.2014");
        renteAIordinaireDiminuee3.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        renteAIordinaireDiminuee3.setCodePrestation("50");

        // Rente AI ordinaire diminu�e
        RERenteJoinDemandeEcheance renteAIordinaireDiminuee2 = new RERenteJoinDemandeEcheance();
        renteAIordinaireDiminuee2.setIdPrestationAccordee("5");
        renteAIordinaireDiminuee2.setDateDebutDroit("11.2003");
        renteAIordinaireDiminuee2.setDateFinDroit("12.2013");
        renteAIordinaireDiminuee2.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        renteAIordinaireDiminuee2.setCodePrestation("50");
        renteAIordinaireDiminuee2.setDateEcheance("12.2013");

        // API AI en home valid�e
        RERenteJoinDemandeEcheance apiAIhomeValidee = new RERenteJoinDemandeEcheance();
        apiAIhomeValidee.setIdPrestationAccordee("6");
        apiAIhomeValidee.setDateDebutDroit("01.1997");
        apiAIhomeValidee.setDateFinDroit("12.2003");
        apiAIhomeValidee.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        apiAIhomeValidee.setCodePrestation("91");

        // Rente AI ordinaire diminu�e
        RERenteJoinDemandeEcheance renteAIordinaireDiminuee = new RERenteJoinDemandeEcheance();
        renteAIordinaireDiminuee.setIdPrestationAccordee("7");
        renteAIordinaireDiminuee.setDateDebutDroit("01.1997");
        renteAIordinaireDiminuee.setDateFinDroit("10.2003");
        renteAIordinaireDiminuee.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        renteAIordinaireDiminuee.setCodePrestation("50");

        entity.getRentesDuTiers().addAll(
                Arrays.asList(apiAvsValidee, apiAIextraDiminuee, renteAIordinaireDiminuee3, renteAIordinaireDiminuee2,
                        apiAIhomeValidee, renteAIordinaireDiminuee));

        assertFalse(module, entity, "05.2014");
    }

    @Test
    public void hommeAgeAvs() {
        // une femme avec une rente AI principale
        entity.setCsSexeTiers(ITIPersonne.CS_HOMME);
        rente.setCodePrestation("50");

        // test avec un homme ayant 65 ans dans le mois courant
        entity.setDateNaissanceTiers("01.01.1948");
        assertFalse(module, entity, "01.2013");

        // test avec un homme ayant 64 ans dans le mois courant
        entity.setDateNaissanceTiers("01.01.1949");
        assertFalse(module, entity, "01.2013");

        // test avec un homme ayant 64 ans et 1 mois dans le mois courant
        entity.setDateNaissanceTiers("31.12.1948");
        assertFalse(module, entity, "01.2013");
    }

    @Before
    public void setUp() {
        // module femme arrivant � l'�ge AVS
        module = new REModuleEcheanceFemmeAgeAvs(session, "01.2013", null);

        entity = new REEcheancesEntity();
        entity.setCsSexeTiers(ITIPersonne.CS_FEMME);

        rente = new RERenteJoinDemandeEcheance();
        rente.setIdPrestationAccordee("1");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        rente.setDateDebutDroit("01.2012");

        entity.getRentesDuTiers().add(rente);
    }
}
