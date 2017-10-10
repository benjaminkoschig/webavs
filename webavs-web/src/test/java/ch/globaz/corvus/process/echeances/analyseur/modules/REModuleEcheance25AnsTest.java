package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.corvus.db.echeances.REEcheancesEntity;
import globaz.corvus.db.echeances.REPeriodeEcheances;
import globaz.corvus.db.echeances.RERenteJoinDemandeEcheance;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;
import ch.globaz.hera.business.constantes.ISFPeriode;

public class REModuleEcheance25AnsTest extends REModuleAnalyseEcheanceTest {

    private RERenteJoinDemandeEcheance rente;

    public REModuleEcheance25AnsTest() {
        super();
    }

    @Test
    public void avecDateDeFinDeDroit() {
        rente.setCodePrestation("54");
        entity.setDateNaissanceTiers("01.09.1986");

        // 25 ans en octobre 2011 et rente en cours ==> motif de sortie
        rente.setDateFinDroit("10.2011");
        assertTrue(module, entity, "09.2011", REMotifEcheance.Echeance25ans);

        // fin de droit en septembre ==> cas traité
        rente.setDateFinDroit("09.2011");
        assertFalse(module, entity, "09.2011");

        // fin de droit en août ==> Plus de rente, cas déjà traité.
        rente.setDateFinDroit("08.2011");
        assertFalse(module, entity, "09.2011");
    }

    @Test
    public void avecDateEcheance() {

        rente.setCodePrestation("34");
        entity.setDateNaissanceTiers("15.08.1989");
        // 25 ans en août 2014 avec une date d'échéance de rente inférieure à août 2014
        rente.setDateEcheance("06.2014");
        assertFalse(module, entity, "08.2014");

        // 25 ans en août 2014 avec une date d'échéance vide -> doit passer
        rente.setDateEcheance("");
        assertTrue(module, entity, "08.2014", REMotifEcheance.Echeance25ans);

        // 25 ans en août 2014 avec une date d'échéance égale au mois de traitement mais pas de périodes d'études -> ne
        // doit pas passer
        rente.setDateEcheance("08.2014");
        assertFalse(module, entity, "08.2014");
        // Cas ou la date d'échéance est plus grande que le mois de traitement et les 25 ans (ne doit pas arriver car
        // l'écran est blindé pour éviter de mettre une date supérieure aux 25 ans de l'enfant
        rente.setDateEcheance("10.2014");
        assertFalse(module, entity, "08.2014");

    }

    @Test
    public void enqueteIntermediaire() {

        rente.setCodePrestation("34");
        entity.setDateNaissanceTiers("15.08.1989");
        // 25 ans en août 2014 avec une date d'échéance égale au mois de traitement
        rente.setDateEcheance("08.2014");
        // le mois de fin de la période d'étude la plus récente est égal au mois de traitement. Ce cas doit passer.
        entity.getPeriodes().add(
                new REPeriodeEcheances("3", "01.03.2012", "30.08.2014", ISFPeriode.CS_TYPE_PERIODE_ETUDE));
        entity.getPeriodes().add(
                new REPeriodeEcheances("1", "01.11.2011", "01.02.2012", ISFPeriode.CS_TYPE_PERIODE_ETUDE));
        // Cette période ne doit pas être prise en compte par l'échéancier car c'est pas le type études
        entity.getPeriodes().add(
                new REPeriodeEcheances("1", "01.11.2014", "31.12.2016", ISFPeriode.CS_TYPE_PERIODE_AFFILIATION));
        assertTrue(module, entity, "08.2014", REMotifEcheance.Echeance25ans);

        entity.getPeriodes().clear();
        entity.getPeriodes().add(
                new REPeriodeEcheances("3", "01.03.2012", "27.07.2013", ISFPeriode.CS_TYPE_PERIODE_ETUDE));
        entity.getPeriodes().add(
                new REPeriodeEcheances("1", "01.11.2011", "01.02.2012", ISFPeriode.CS_TYPE_PERIODE_ETUDE));
        // Ce cas ne doit pas passer car il s'agit d'une enquête intermédiaire
        assertFalse(module, entity, "08.2014");

    }

    @Before
    public void setUp() {
        module = new REModuleEcheance25Ans(session, "09.2011", false);

        entity = new REEcheancesEntity();

        rente = new RERenteJoinDemandeEcheance();
        rente.setIdPrestationAccordee("1");
        entity.getRentesDuTiers().add(rente);
    }

    @Test
    public void testModuleEcheanceEtude25Ans() {
        REModuleAnalyseEcheance moduleEcheanceEtude25ans = new REModuleEcheance25Ans(session, "09.2011", false);
        rente.setCodePrestation("13");

        RERenteJoinDemandeEcheance rentePourTest = new RERenteJoinDemandeEcheance();
        entity.getRentesDuTiers().add(rentePourTest);

        entity.getPeriodes().add(
                new REPeriodeEcheances("1", "01.01.2011", "30.09.2011", ISFPeriode.CS_TYPE_PERIODE_ETUDE));
        entity.getPeriodes().add(
                new REPeriodeEcheances("2", "01.11.2011", "31.12.2011", ISFPeriode.CS_TYPE_PERIODE_ETUDE));
        rentePourTest.setCodePrestation("14");

        // test avec 25 ans dans le mois précédant
        entity.setDateNaissanceTiers("31.08.1986");
        assertTrue(moduleEcheanceEtude25ans, entity, "09.2011", REMotifEcheance.Echeance25ansDepassee);

        // test avec 25 ans dans le mois suivant
        entity.setDateNaissanceTiers("01.10.1986");
        assertFalse(moduleEcheanceEtude25ans, entity, "09.2011");

        // test avec 25 ans au début du mois courant
        entity.setDateNaissanceTiers("01.09.1986");
        assertTrue(moduleEcheanceEtude25ans, entity, "09.2011", REMotifEcheance.Echeance25ans);

        // test avec 25 ans au début du mois courant avec une rente principale
        rentePourTest.setCodePrestation("10");
        entity.setDateNaissanceTiers("01.09.1986");
        assertFalse(moduleEcheanceEtude25ans, entity, "09.2011");

        // test avec 25 ans au début du mois courant tous les types de complémentaire pour enfant (vieillesse)
        rentePourTest.setCodePrestation("15");
        entity.setDateNaissanceTiers("01.09.1986");
        assertTrue(moduleEcheanceEtude25ans, entity, "09.2011", REMotifEcheance.Echeance25ans);

        rentePourTest.setCodePrestation("16");
        entity.setDateNaissanceTiers("01.09.1986");
        assertTrue(moduleEcheanceEtude25ans, entity, "09.2011", REMotifEcheance.Echeance25ans);

        // test avec 25 ans à la fin du mois courant
        entity.setDateNaissanceTiers("30.09.1986");
        assertTrue(moduleEcheanceEtude25ans, entity, "09.2011", REMotifEcheance.Echeance25ans);

        // test avec 25 ans à la fin du mois courant avec une date invalide
        entity.setDateNaissanceTiers("31.09.1986");
        assertFalse(moduleEcheanceEtude25ans, entity, "09.2011");

        // test avec 25 ans au début du mois courant, et rente bloquée, sans distinction du blocage
        rentePourTest.setIsPrestationBloquee(true);
        entity.setDateNaissanceTiers("01.09.1986");
        assertTrue(moduleEcheanceEtude25ans, entity, "09.2011", REMotifEcheance.Echeance25ans);

        // test avec 25 ans au début du mois courant, et rente bloquée, avec distinction du blocage
        moduleEcheanceEtude25ans = new REModuleEcheance25Ans(session, "09.2011", true);
        rentePourTest.setIsPrestationBloquee(true);
        entity.setDateNaissanceTiers("01.09.1986");
        assertTrue(moduleEcheanceEtude25ans, entity, "09.2011", REMotifEcheance.Echeance25ansRenteBloquee);
    }
}
