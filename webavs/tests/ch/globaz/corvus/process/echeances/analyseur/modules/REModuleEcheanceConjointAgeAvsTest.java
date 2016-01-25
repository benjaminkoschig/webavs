package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.echeances.REEcheancesEntity;
import globaz.corvus.db.echeances.RERelationEcheances;
import globaz.corvus.db.echeances.RERenteJoinDemandeEcheance;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.pyxis.api.ITIPersonne;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;

public class REModuleEcheanceConjointAgeAvsTest extends REModuleAnalyseEcheanceTest {

    private RERelationEcheances relation;
    private RERenteJoinDemandeEcheance rente;

    public REModuleEcheanceConjointAgeAvsTest() {
        super();
    }

    @Test
    public void femmeAvecConjointAgeAvsDepasse() {

        entity.setCsSexeTiers(ITIPersonne.CS_FEMME);
        entity.setDateNaissanceTiers("31.12.1947");

        rente.setCodePrestation("10");

        relation.setCsSexeConjoint(ITIPersonne.CS_HOMME);
        relation.setDateNaissanceConjoint("31.12.1946");

        // comportement par défaut : on ignore ce motif car trop de faux positifs avec les reprises de données
        assertFalse(module, entity, "01.2012");

        // activation détection de dépassement d'âge AVS
        ((REModuleEcheanceConjointAgeAvs) module).setWantAgeAvsDepasse(true);
        assertTrue(module, entity, "01.2012", REMotifEcheance.ConjointAgeAvsDepasse);
    }

    @Test
    public void femmeAvecConjointArrivantAvsAvecRenteAutreQueVieillesse() {

        entity.setCsSexeTiers(ITIPersonne.CS_FEMME);
        entity.setDateNaissanceTiers("31.12.1947");

        rente.setCodePrestation("10");

        relation.setCsSexeConjoint(ITIPersonne.CS_HOMME);
        relation.setDateNaissanceConjoint("01.01.1947");

        RERenteJoinDemandeEcheance renteConjoint = new RERenteJoinDemandeEcheance();
        renteConjoint.setIdPrestationAccordee("2");
        renteConjoint.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        renteConjoint.setDateDebutDroit("01.2000");
        relation.getRentesDuConjoint().add(renteConjoint);

        // aucun cas ne doit sortir, car ils seront traités par le module âge AVS correspondant au
        // sexe du conjoint

        renteConjoint.setCodePrestation("50");
        assertFalse(module, entity, "01.2012");

        renteConjoint.setCodePrestation("33");
        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void femmeAvecConjointArrivantAvsSansRente() {

        entity.setCsSexeTiers(ITIPersonne.CS_FEMME);
        entity.setDateNaissanceTiers("31.12.1947");

        rente.setCodePrestation("10");

        relation.setCsSexeConjoint(ITIPersonne.CS_HOMME);
        relation.setDateNaissanceConjoint("01.01.1947");

        assertTrue(module, entity, "01.2012", REMotifEcheance.ConjointArrivantAgeAvs);
    }

    @Test
    public void hommeAvecConjointeAgeAvsDepasse() {

        entity.setDateNaissanceTiers("31.12.1946");
        rente.setCodePrestation("10");
        relation.setDateNaissanceConjoint("31.12.1947");

        // comportement par défaut : on ignore ce motif car trop de faux positifs avec les reprises de données
        assertFalse(module, entity, "01.2012");

        // activation détection de dépassement d'âge AVS
        ((REModuleEcheanceConjointAgeAvs) module).setWantAgeAvsDepasse(true);
        assertTrue(module, entity, "01.2012", REMotifEcheance.ConjointAgeAvsDepasse);
    }

    @Test
    public void hommeAvecConjointeArrivantAvsAvecRenteAutreQueVieillesse() {

        entity.setDateNaissanceTiers("31.12.1946");
        rente.setCodePrestation("10");
        relation.setDateNaissanceConjoint("01.01.1948");

        RERenteJoinDemandeEcheance renteConjoint = new RERenteJoinDemandeEcheance();
        renteConjoint.setIdPrestationAccordee("2");
        renteConjoint.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        renteConjoint.setDateDebutDroit("01.2000");
        relation.getRentesDuConjoint().add(renteConjoint);

        // aucun cas ne doit sortir, car ils seront traités par le module âge AVS correspondant au
        // sexe du conjoint

        renteConjoint.setCodePrestation("50");
        assertFalse(module, entity, "01.2012");

        renteConjoint.setCodePrestation("33");
        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void hommeAvecConjointeArrivantAvsSansRente() {

        entity.setDateNaissanceTiers("31.12.1946");
        rente.setCodePrestation("10");
        relation.setDateNaissanceConjoint("01.01.1948");

        assertTrue(module, entity, "01.2012", REMotifEcheance.ConjointArrivantAgeAvs);
    }

    @Before
    public void setUp() {
        // module conjoint arrivant à l'âge AVS
        module = new REModuleEcheanceConjointAgeAvs(session, "01.2012");

        entity = new REEcheancesEntity();
        entity.setCsSexeTiers(ITIPersonne.CS_HOMME);

        rente = new RERenteJoinDemandeEcheance();
        rente.setIdPrestationAccordee("1");
        rente.setDateDebutDroit("01.2011");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);

        entity.getRentesDuTiers().add(rente);

        relation = new RERelationEcheances();
        relation.setIdRelation("1");
        relation.setCsTypeRelation(ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT);
        relation.setCsSexeConjoint(ITIPersonne.CS_FEMME);
        entity.getRelations().add(relation);
    }
}
