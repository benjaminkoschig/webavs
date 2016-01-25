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
