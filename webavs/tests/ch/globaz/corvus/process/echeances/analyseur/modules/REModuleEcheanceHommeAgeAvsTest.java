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

public class REModuleEcheanceHommeAgeAvsTest extends REModuleAnalyseEcheanceTest {

    private RERenteJoinDemandeEcheance rente;

    public REModuleEcheanceHommeAgeAvsTest() {
        super();
    }

    @Test
    public void femmeAgeAvs() {
        // une femme avec une rente AI principale
        entity.setCsSexeTiers(ITIPersonne.CS_FEMME);
        rente.setCodePrestation("50");

        // test avec une femme ayant 64 ans dans le mois courant
        entity.setDateNaissanceTiers("01.01.1949");
        assertFalse(module, entity, "01.2013");

        // test avec une femme ayant 65 ans dans le mois courant
        entity.setDateNaissanceTiers("01.01.1948");
        assertFalse(module, entity, "01.2013");

        // test avec une femme ayant 65 ans et 1 mois dans le mois courant
        entity.setDateNaissanceTiers("31.12.1947");
        assertFalse(module, entity, "01.2013");
    }

    @Test
    public void hommeAgeAvsDepasseAvantEtApres2012() {
        // le but étant de vérifier qu'on ignore les gens sans rentes dès lors que leur droit est survenu avant le
        // 01.01.2012. Ceci afin d'éviter que tous les cas de reprises de données sans date de décès ne viennent polluer
        // la liste.
        module.setMoisTraitement("02.2012");

        // test avec un homme ayant 65 ans après le 1er janvier 2012
        entity.setDateNaissanceTiers("01.01.1947");

        rente.setDateDebutDroit("01.2011");
        rente.setCodePrestation("50");
        assertTrue(module, entity, "01.2013", REMotifEcheance.HommeAgeAvsDepasse);

        rente.setCodePrestation("10");
        assertFalse(module, entity, "01.2013");

        rente.setAnneeAnticipation("2");
        assertTrue(module, entity, "01.2013", REMotifEcheance.HommeAgeAvsAnticipationDepassee);
        rente.setAnneeAnticipation("0");

        rente.setCodePrestation("33");
        assertFalse(module, entity, "01.2013");

        rente.setCodePrestation("10");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
        assertTrue(module, entity, "01.2013", REMotifEcheance.HommeAgeAvsDepasse);

        // test avec un homme ayant 65 ans avant le 1er janvier 2012
        entity.setDateNaissanceTiers("31.12.1946");
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

        // même test mais avec cette fois ci la propriété en DB assignée un jour avant la date par défaut
        module = new REModuleEcheanceHommeAgeAvs(session, "01.2013", "31.12.2011");
        assertTrue(module, entity, "01.2013", REMotifEcheance.HommeAgeAvsDepasse);
    }

    @Test
    public void hommeAvecRenteAIeteinteAVSenCoursEtAPIAIenCours() { // Nouveau test
        // une femme ayant 65 ans au mois de mai 2014
        entity.setDateNaissanceTiers("01.05.1949");

        // avec une API-AI en cours
        rente.setCodePrestation("81"); // Rente API (degré faible) AI
        // this.rente.setCsGenreDroitApi(IREDemandeRente.CS_GENRE_DROIT_API_API_AI_PRST); // AJOUTE PAR AME
        rente.setDateDebutDroit("01.2010");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);

        // Avec rente invalidité ordinaire jusqu'à mai 2014
        RERenteJoinDemandeEcheance renteAIechue = new RERenteJoinDemandeEcheance();
        renteAIechue.setIdPrestationAccordee("2");
        renteAIechue.setDateDebutDroit("01.2010");
        renteAIechue.setDateFinDroit("05.2014");
        renteAIechue.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        renteAIechue.setCodePrestation("50"); // Rente invalidité ordinaire

        // Avec rente AVS ordinaire depuis juin 2014
        RERenteJoinDemandeEcheance renteAVSenCours = new RERenteJoinDemandeEcheance();
        renteAVSenCours.setIdPrestationAccordee("3");
        renteAVSenCours.setDateDebutDroit("06.2014");
        renteAVSenCours.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        renteAVSenCours.setCodePrestation("10");

        entity.getRentesDuTiers().add(renteAVSenCours);
        entity.getRentesDuTiers().add(renteAIechue);

        assertTrue(module, entity, "05.2014", REMotifEcheance.HommeArrivantAgeAvsAvecApiAi);
    }

    @Test
    public void hommeAvecRenteAPI() {
        // test avec une API AI en cours, et un homme ayant 65 ans dans le mois courant
        entity.setDateNaissanceTiers("01.01.1948");
        rente.setCodePrestation("82"); // API AI (impotence de degré moyen)
        rente.setAnneeAnticipation("");
        assertTrue(module, entity, "01.2013", REMotifEcheance.HommeArrivantAgeAvsAvecApiAi);

        // test avec une API AVS en cours, et un homme ayant 65 ans dans le mois courant
        rente.setCodePrestation("86"); // API AVS (impotence de degré moyen)
        assertTrue(module, entity, "01.2013", REMotifEcheance.HommeArrivantAgeAvs);

        // test avec une rente AVS mais une API AI
        rente.setCodePrestation("81"); // API AI (impotence de degré faible)
        RERenteJoinDemandeEcheance renteVieillesse = new RERenteJoinDemandeEcheance();
        renteVieillesse.setIdPrestationAccordee("2");
        renteVieillesse.setCodePrestation("10");
        renteVieillesse.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        entity.getRentesDuTiers().add(renteVieillesse);
        assertTrue(module, entity, "01.2013", REMotifEcheance.HommeArrivantAgeAvsAvecApiAi);
    }

    @Test
    public void hommeAvecRenteTransferee() {
        // homme ayant 65 ans en janvier 2013 avec une rente AI transférée
        entity.setDateNaissanceTiers("01.01.1948");
        rente.setCodePrestation("50");
        rente.setDateDebutDroit("01.2010");
        rente.setDateFinDroit("12.2011");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        rente.setCsEtatDemandeRente(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE);

        assertFalse(module, entity, "01.2013");

        // homme ayant déjà 65 en janvier 2013 avec une rente AVS transférée
        entity.setDateNaissanceTiers("31.12.1947");
        rente.setCodePrestation("10");
        rente.setDateDebutDroit("01.2010");
        rente.setDateFinDroit("12.2011");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        rente.setCsEtatDemandeRente(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE);

        assertFalse(module, entity, "01.2013");
    }

    @Test
    public void hommeAvecRenteVieillesseFuture() {
        // test avec une rente qui n'est pas en cours et un homme ayant déjà 65 ans dans le mois courant
        rente.setCodePrestation("10");
        rente.setDateDebutDroit("03.2013"); // février serait une valeur valide
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        entity.setDateNaissanceTiers("31.12.1947");

        assertTrue(module, entity, "01.2013", REMotifEcheance.HommeAgeAvsDepasse);
    }

    @Test
    public void hommePassantDeAIaAVS() {
        rente.setCodePrestation("50");
        rente.setDateDebutDroit("01.2012");
        rente.setDateFinDroit("12.2012");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        rente.setDateEcheance("12.2012");

        entity.setDateNaissanceTiers("31.12.1947");

        RERenteJoinDemandeEcheance rente2 = new RERenteJoinDemandeEcheance();
        rente2.setCodePrestation("10");
        rente2.setDateDebutDroit("01.2013");
        rente2.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        entity.getRentesDuTiers().add(rente2);

        assertFalse(module, entity, "01.2013");

        entity.setDateNaissanceTiers("01.01.1948");
        rente.setDateFinDroit("01.2013");
        rente2.setDateDebutDroit("02.2013");

        assertFalse(module, entity, "01.2013");
    }

    @Test
    public void hommeRenteAI() {
        // un homme avec une rente AI principale
        rente.setCodePrestation("50");

        // test avec un homme ayant 64 ans, 11 mois et 31 jours dans le mois courant
        entity.setDateNaissanceTiers("01.02.1948");
        assertFalse(module, entity, "01.2013");

        // test avec un homme ayant 65 ans dans le mois courant
        entity.setDateNaissanceTiers("31.01.1948");
        assertTrue(module, entity, "01.2013", REMotifEcheance.HommeArrivantAgeAvs);

        // test avec un homme ayant 65 ans et 31 jours dans le mois courant
        entity.setDateNaissanceTiers("01.01.1948");
        assertTrue(module, entity, "01.2013", REMotifEcheance.HommeArrivantAgeAvs);

        // test avec un homme ayant 65 ans, 1 mois et 1 jour dans le mois courant
        entity.setDateNaissanceTiers("31.12.1947");
        assertTrue(module, entity, "01.2013", REMotifEcheance.HommeAgeAvsDepasse);
    }

    @Test
    public void hommeRenteAnticipe() {
        // un homme avec une rente vieillesse anticipée
        rente.setCodePrestation("10");

        // test avec un homme ayant 65 ans et 1 jour, dans le mois courant
        entity.setDateNaissanceTiers("31.12.1947");
        assertFalse(module, entity, "01.2013");

        // test avec un homme ayant 65 ans et 1 jour, dans le mois courant (sans anticipation, mais année d'anticipation
        // forcée à zéro)
        rente.setAnneeAnticipation("0");
        assertFalse(module, entity, "01.2013");

        // avec une année d'anticipation
        rente.setAnneeAnticipation("1");

        // test avec un homme ayant 64 ans, 11 mois et 31 jours dans le mois courant
        entity.setDateNaissanceTiers("01.02.1948");
        assertFalse(module, entity, "01.2013");

        // test avec un homme ayant 65 ans dans le mois courant
        entity.setDateNaissanceTiers("31.01.1948");
        assertTrue(module, entity, "01.2013", REMotifEcheance.HommeArrivantAgeAvsRenteAnticipee);

        // test avec un homme ayant 65 ans et 31 jours dans le mois courant
        entity.setDateNaissanceTiers("01.01.1948");
        assertTrue(module, entity, "01.2013", REMotifEcheance.HommeArrivantAgeAvsRenteAnticipee);

        // test avec un homme ayant 65 ans, 1 mois et 1 jour dans le mois courant
        entity.setDateNaissanceTiers("31.12.1947");
        assertTrue(module, entity, "01.2013", REMotifEcheance.HommeAgeAvsAnticipationDepassee);

        // test avec un homme ayant 65 ans et 31 jours dans le mois courant et une rente de vieillesse
        // reprenant le droit le mois suivant
        entity.setDateNaissanceTiers("01.01.1948");
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
    public void hommeRentesVeuf() {
        // un homme avec une rente de veuf
        rente.setCodePrestation("13");

        // test avec un homme ayant 64 ans, 11 mois et 31 jours dans le mois courant
        entity.setDateNaissanceTiers("01.02.1948");
        assertFalse(module, entity, "01.2013");

        // test avec un homme ayant 65 ans dans le mois courant
        entity.setDateNaissanceTiers("31.01.1948");
        assertTrue(module, entity, "01.2013", REMotifEcheance.HommeArrivantAgeAvs);

        // test avec un homme ayant 65 ans et 31 jours dans le mois courant
        entity.setDateNaissanceTiers("01.01.1948");
        assertTrue(module, entity, "01.2013", REMotifEcheance.HommeArrivantAgeAvs);

        // test avec un homme ayant 65 ans, 1 mois et 1 jour dans le mois courant
        entity.setDateNaissanceTiers("31.12.1947");
        assertTrue(module, entity, "01.2013", REMotifEcheance.HommeAgeAvsDepasse);
    }

    @Test
    public void hommeRentesVieillesseComplementaire() {
        // un homme avec une rente vieillesse complémentaire
        rente.setCodePrestation("33");

        // test avec un homme ayant 64 ans, 11 mois et 31 jours dans le mois courant
        entity.setDateNaissanceTiers("01.02.1948");
        assertFalse(module, entity, "01.2013");

        // test avec un homme ayant 65 ans dans le mois courant
        entity.setDateNaissanceTiers("31.01.1948");
        assertTrue(module, entity, "01.2013", REMotifEcheance.HommeArrivantAgeAvs);

        // test avec un homme ayant 65 ans et 31 jours dans le mois courant
        entity.setDateNaissanceTiers("01.01.1948");
        assertTrue(module, entity, "01.2013", REMotifEcheance.HommeArrivantAgeAvs);

        // test avec un homme ayant 65 ans et 1 jour dans le mois courant
        // doit retourner faux, car ce cas est assez courant (dans le cas d'une bénéficiaire féminine) et aura une
        // implementation propre dans l'application (rente complémentaire vieillesse perdure) dans un futur proche
        entity.setDateNaissanceTiers("31.12.1947");
        assertFalse(module, entity, "01.2013");
    }

    @Test
    public void hommeRenteVieillesseAjournee() {
        // test avec une rente ajournée dans le mois, et un homme ayant déjà 65 ans dans ce mois
        entity.setDateNaissanceTiers("01.01.1947");
        rente.setCodePrestation("10");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_AJOURNE);
        assertFalse(module, entity, "01.2013");

        // avec révocation de l'ajournement dans le mois de traitement
        // pas encore géré par le module d'ajournement -> homme âge AVS dépassé
        rente.setDateRevocationAjournement("01.2013");
        assertTrue(module, entity, "01.2013", REMotifEcheance.HommeAgeAvsDepasse);

        // avec révocation de l'ajournement avant le mois de traitement
        // pas encore géré par le module d'ajournement -> homme âge AVS dépassé
        rente.setDateRevocationAjournement("12.2012");
        assertTrue(module, entity, "01.2013", REMotifEcheance.HommeAgeAvsDepasse);
    }

    @Test
    public void hommeVersAVSavecAPIAIEtAPIAVS() {
        entity.setDateNaissanceTiers("01.05.1949");

        // avec rente principale AVS
        rente.setCodePrestation("10");
        rente.setDateDebutDroit("06.2014");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);

        // avec rente API-AVS de faible degré
        RERenteJoinDemandeEcheance apiAvsValidee = new RERenteJoinDemandeEcheance();
        apiAvsValidee.setIdPrestationAccordee("2000");
        apiAvsValidee.setDateDebutDroit("06.2014");
        apiAvsValidee.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        apiAvsValidee.setCodePrestation("89");

        // API AI extraordinaire diminuée
        RERenteJoinDemandeEcheance apiAIextraDiminuee = new RERenteJoinDemandeEcheance();
        apiAIextraDiminuee.setIdPrestationAccordee("3");
        apiAIextraDiminuee.setDateDebutDroit("01.2004");
        apiAIextraDiminuee.setDateFinDroit("05.2014");
        apiAIextraDiminuee.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        apiAIextraDiminuee.setCodePrestation("81");
        apiAIextraDiminuee.setIsPrestationBloquee(true);
        apiAIextraDiminuee.setDateEcheance("05.2014");

        // Rente AI ordinaire diminuée
        RERenteJoinDemandeEcheance renteAIordinaireDiminuee3 = new RERenteJoinDemandeEcheance();
        renteAIordinaireDiminuee3.setIdPrestationAccordee("4");
        renteAIordinaireDiminuee3.setDateDebutDroit("01.2014");
        renteAIordinaireDiminuee3.setDateFinDroit("05.2014");
        renteAIordinaireDiminuee3.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        renteAIordinaireDiminuee3.setCodePrestation("50");

        // Rente AI ordinaire diminuée
        RERenteJoinDemandeEcheance renteAIordinaireDiminuee2 = new RERenteJoinDemandeEcheance();
        renteAIordinaireDiminuee2.setIdPrestationAccordee("5");
        renteAIordinaireDiminuee2.setDateDebutDroit("11.2003");
        renteAIordinaireDiminuee2.setDateFinDroit("12.2013");
        renteAIordinaireDiminuee2.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        renteAIordinaireDiminuee2.setCodePrestation("50");
        renteAIordinaireDiminuee2.setDateEcheance("12.2013");

        // API AI en home validée
        RERenteJoinDemandeEcheance apiAIhomeValidee = new RERenteJoinDemandeEcheance();
        apiAIhomeValidee.setIdPrestationAccordee("6");
        apiAIhomeValidee.setDateDebutDroit("01.1997");
        apiAIhomeValidee.setDateFinDroit("12.2003");
        apiAIhomeValidee.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        apiAIhomeValidee.setCodePrestation("91");

        // Rente AI ordinaire diminuée
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

    @Before
    public void setUp() {
        // module homme arrivant à l'âge AVS
        module = new REModuleEcheanceHommeAgeAvs(session, "01.2013", null);

        entity = new REEcheancesEntity();
        entity.setCsSexeTiers(ITIPersonne.CS_HOMME);

        rente = new RERenteJoinDemandeEcheance();
        rente.setIdPrestationAccordee("1");
        rente.setDateDebutDroit("01.2012");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);

        entity.getRentesDuTiers().add(rente);
    }
}
