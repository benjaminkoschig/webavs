package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.echeances.REEcheancesEntity;
import globaz.corvus.db.echeances.RERenteJoinDemandeEcheance;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;

public class REModuleEcheancesForceesTest extends REModuleAnalyseEcheanceTest {

    private RERenteJoinDemandeEcheance rente;

    public REModuleEcheancesForceesTest() {
        super();
    }

    @Test
    public void dossierDejaTraite() {
        rente.setDateDebutDroit("01.2010");
        rente.setDateFinDroit("01.2012");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        rente.setDateEcheance("01.2012");

        RERenteJoinDemandeEcheance rente2 = new RERenteJoinDemandeEcheance();
        rente2.setIdPrestationAccordee("2");
        rente2.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        rente2.setDateDebutDroit("02.2012");

        entity.getRentesDuTiers().add(rente2);

        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void echancesForceesAvecDeuxRentesDontUneDiminuee() {
        rente.setDateEcheance("01.2012");
        rente.setCodePrestation("10");
        rente.setDateDebutDroit("01.2011");
        rente.setDateFinDroit("01.2012");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);

        RERenteJoinDemandeEcheance rente2 = new RERenteJoinDemandeEcheance();
        rente2.setIdPrestationAccordee("2");
        rente2.setDateDebutDroit("02.2012");
        rente2.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        entity.getRentesDuTiers().add(rente2);

        assertFalse(module, entity, "01.2012");

        rente.setDateEcheance("01.01.2012");
        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void echeancesForceesMMAAA() {
        // test avec une date d'échéance dans le mois précédant
        rente.setDateEcheance("12.2011");
        assertFalse(module, entity, "01.2012");

        // test avec une date d'échéance dans le mois suivant
        rente.setDateEcheance("02.2012");
        assertFalse(module, entity, "01.2012");

        // test avec une date d'échéance (MM.AAAA) dans le mois
        rente.setDateEcheance("01.2012");
        assertTrue(module, entity, "01.2012", REMotifEcheance.EcheanceForcee);

        // test avec un date d'échéance (MM.AAAA) invalide
        rente.setDateEcheance("00.2012");
        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void echeancesForcessJJMMAAA() {
        // test avec une date d'échéance dans le mois précédant
        rente.setDateEcheance("31.12.2011");
        assertFalse(module, entity, "01.2012");

        // test avec une date d'échéance dans le mois suivant
        rente.setDateEcheance("01.02.2012");
        assertFalse(module, entity, "01.2012");

        // test avec un date d'échéance (JJ.MM.AAAA) au début du mois
        rente.setDateEcheance("01.01.2012");
        assertTrue(module, entity, "01.2012", REMotifEcheance.EcheanceForcee);

        // test avec un date d'échéance (JJ.MM.AAAA) à la fin du mois
        rente.setDateEcheance("31.01.2012");
        assertTrue(module, entity, "01.2012", REMotifEcheance.EcheanceForcee);

        // test avec un date d'échéance (JJ.MM.AAAA) invalide
        rente.setDateEcheance("32.01.2012");
        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void sansDateEcheances() {
        assertFalse(module, entity, "01.2012");
    }

    @Before
    public void setUp() {
        module = new REModuleEcheancesForcees(session, "01.2012");

        entity = new REEcheancesEntity();

        rente = new RERenteJoinDemandeEcheance();
        rente.setIdPrestationAccordee("1");
        entity.getRentesDuTiers().add(rente);
    }
}
