package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.echeances.REEcheancesEntity;
import globaz.corvus.db.echeances.RERenteJoinDemandeEcheance;
import globaz.pyxis.api.ITIPersonne;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;

public class REModuleEcheanceAjournementTest extends REModuleAnalyseEcheanceTest {

    private RERenteJoinDemandeEcheance rente;

    public REModuleEcheanceAjournementTest() {
        super();
    }

    /**
     * Femme en age AVS + 5 ans Rente non-ajournée
     * 
     * @
     */
    @Test
    public void femme69ansAutreEtatRente() {
        // test pour une femme avec 69 ans au début du mois, mais une rente non ajournée
        entity.setCsSexeTiers(ITIPersonne.CS_FEMME);
        entity.setDateNaissanceTiers("01.01.1943");

        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
        assertFalse(module, entity, "01.2012");

        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        assertFalse(module, entity, "01.2012");
        /*
         * le cas ci-dessous permet de vérifier qu'un ajournement n'est valide qu'avec le code cas special 08
         */
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        Set<String> codesCasSpeciaux = new HashSet<String>();
        codesCasSpeciaux.add("02");
        codesCasSpeciaux.add("05");
        codesCasSpeciaux.add("108");
        rente.setCodesCasSpeciaux(codesCasSpeciaux);
        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void femmeFinAjournement() {
        // pour une femme : 64 ans + 5 années d'ajournement
        entity.setCsSexeTiers(ITIPersonne.CS_FEMME);

        // test pour une femme avec 69 ans dans le mois précédant
        entity.setDateNaissanceTiers("31.12.1942");
        assertTrue(module, entity, "01.2012", REMotifEcheance.AjournementDepasse);

        // test pour une femme avec 69 ans dans le mois suivant
        entity.setDateNaissanceTiers("01.02.1943");
        assertFalse(module, entity, "01.2012");

        // test pour une femme avec 69 ans au début du mois courant
        entity.setDateNaissanceTiers("01.01.1943");
        assertTrue(module, entity, "01.2012", REMotifEcheance.Ajournement);

        // test pour une femme avec 69 ans à la fin du mois courant
        entity.setDateNaissanceTiers("31.01.1943");
        assertTrue(module, entity, "01.2012", REMotifEcheance.Ajournement);

        // test pour une femme avec 69 ans avec une date de naissance invalide
        entity.setDateNaissanceTiers("00.01.1943");
        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void femmeRenteAvecDateRevocationAjournement() {
        // test pour une femme avec 69 ans à la fin du mois courant, dont la rente à déjà une date de revocation
        // d'ajournement
        entity.setDateNaissanceTiers("30.09.1942");
        rente.setDateEcheance("08.2011");
        assertTrue(module, entity, "01.2012", REMotifEcheance.AjournementRevocationDemandeeDepassee);
    }

    @Test
    public void homme70ansAutreEtatRente() {
        // un homme avec 70 ans dans le mois
        entity.setCsSexeTiers(ITIPersonne.CS_HOMME);
        entity.setDateNaissanceTiers("01.01.1942");

        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
        assertFalse(module, entity, "01.2012");

        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        assertFalse(module, entity, "01.2012");

        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        rente.setCodesCasSpeciaux(new HashSet<String>());
        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void hommeFinAjournement() {
        // pour un homme : 65 ans + 5 années d'ajournement
        entity.setCsSexeTiers(ITIPersonne.CS_HOMME);

        // test pour un homme avec 70 ans dans le mois précédant
        entity.setDateNaissanceTiers("31.12.1941");
        assertTrue(module, entity, "01.2012", REMotifEcheance.AjournementDepasse);

        // test pour un homme avec 70 ans dans le mois suivant
        entity.setDateNaissanceTiers("01.02.1942");
        assertFalse(module, entity, "01.2012");

        // test pour un homme avec 70 ans au début du mois courant
        entity.setDateNaissanceTiers("01.01.1942");
        assertTrue(module, entity, "01.2012", REMotifEcheance.Ajournement);

        // test pour un homme avec 70 ans à la fin du mois courant
        entity.setDateNaissanceTiers("31.01.1942");
        assertTrue(module, entity, "01.2012", REMotifEcheance.Ajournement);

        // test pour un homme avec une date de naissance invalide
        entity.setDateNaissanceTiers("00.01.1942");
        assertFalse(module, entity, "01.2012");

    }

    @Test
    public void hommeRenteAvecDateRevocationAjournement() {

        // un homme en age AVS (ajournement < 5 ans)
        entity.setCsSexeTiers(ITIPersonne.CS_HOMME);
        entity.setDateNaissanceTiers("01.01.1945");

        // date échéance ajourn < mois traitement
        rente.setDateEcheance("12.2011");
        assertTrue(module, entity, "01.2012", REMotifEcheance.AjournementRevocationDemandeeDepassee);

        // date échéance ajourn == mois traitement
        rente.setDateEcheance("01.2012");
        assertTrue(module, entity, "01.2012", REMotifEcheance.AjournementRevocationDemandee);

        // date échéance ajourn > mois traitement
        rente.setDateEcheance("02.2012");
        assertFalse(module, entity, "01.2012");

        // Test sur les autres état de rente
        rente.setDateEcheance("12.2011");

        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
        assertFalse(module, entity, "01.2012");

        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        assertFalse(module, entity, "01.2012");

        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        rente.setCodesCasSpeciaux(new HashSet<String>());
        assertFalse(module, entity, "01.2012");

        rente.setDateEcheance("01.2012");

        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
        assertFalse(module, entity, "01.2012");

        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        assertFalse(module, entity, "01.2012");

        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        rente.setCodesCasSpeciaux(new HashSet<String>());
        assertFalse(module, entity, "01.2012");

        rente.setDateEcheance("02.2012");

        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
        assertFalse(module, entity, "01.2012");

        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        assertFalse(module, entity, "01.2012");

        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        // Test en ayant la liste de codes cas spéciaux à null
        rente.setCodesCasSpeciaux(null);
        assertFalse(module, entity, "01.2012");
    }

    @Before
    public void setUp() {
        module = new REModuleEcheanceAjournement(session, "01.2012");

        entity = new REEcheancesEntity();

        rente = new RERenteJoinDemandeEcheance();
        rente.setIdPrestationAccordee("1");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        Set<String> codesCasSpeciaux = new HashSet<String>();
        codesCasSpeciaux.add("08");
        rente.setCodesCasSpeciaux(codesCasSpeciaux);
        entity.getRentesDuTiers().add(rente);
    }
}
