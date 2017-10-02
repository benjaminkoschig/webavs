package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.echeances.REEcheancesEntity;
import globaz.corvus.db.echeances.REEnfantEcheances;
import globaz.corvus.db.echeances.RERenteJoinDemandeEcheance;
import globaz.pyxis.api.ITIPersonne;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;

public class REModuleEcheanceRenteDeVeufTest extends REModuleAnalyseEcheanceTest {

    private REEnfantEcheances enfant;
    private RERenteJoinDemandeEcheance rente;
    private RERenteJoinDemandeEcheance renteEnfant;

    public REModuleEcheanceRenteDeVeufTest() {
        super();
    }

    @Test
    public void dossierDejaTraite() {
        enfant.setDateNaissance("01.01.1994");
        rente.setDateFinDroit("01.2012");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);

        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void enfant18ansDansLeMois() {
        enfant.setDateNaissance("01.01.1994");
        assertTrue(module, entity, "01.2012", REMotifEcheance.RenteDeVeuf);
    }

    @Test
    public void enfantDeja18ansDansLeMois() {
        enfant.setDateNaissance("31.12.1993");
        assertTrue(module, entity, "01.2012", REMotifEcheance.RenteDeVeufSansEnfant);
    }

    @Test
    public void enfantMoinsDe18ans() {
        enfant.setDateNaissance("01.02.1994");
        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void plusieursEnfantAvecRentesDiverses() {
        enfant.setDateNaissance("01.01.1994");
        renteEnfant.setCodePrestation("15");
        enfant.getRentes().add(renteEnfant);

        // deuxième enfant plus âgé
        REEnfantEcheances enfant2 = new REEnfantEcheances();
        enfant2.setIdTiers("2");
        enfant2.setDateNaissance("01.01.1990");
        entity.getEnfantsDuTiers().add(enfant2);

        // l'enfant atteint 18 ans dans le mois courant, sortie du motif 'Rente de veuf'
        RERenteJoinDemandeEcheance renteEnfant2 = new RERenteJoinDemandeEcheance();
        renteEnfant2.setIdPrestationAccordee("3");
        renteEnfant2.setCodePrestation("15");
        renteEnfant2.setDateDebutDroit("01.2011");
        enfant2.getRentes().add(renteEnfant2);
        assertTrue(module, entity, "01.2012", REMotifEcheance.RenteDeVeuf);

        // l'enfant à 18 ans ET 1 MOIS dans le mois courant, sortie du motif 'Rente de veuf sans enfant'
        enfant.setDateNaissance("31.12.1993");
        assertTrue(module, entity, "01.2012", REMotifEcheance.RenteDeVeufSansEnfant);
    }

    @Test
    public void plusieursEnfantsDontUnAvecDateDeces() {
        // Test réalisé suite à la correction de K160318_005
        // Premier enfant plus jeune mais décédé
        enfant.setDateNaissance("01.01.2005");
        enfant.setDateDeces("01.01.2016");
        renteEnfant.setCodePrestation("15");
        enfant.getRentes().add(renteEnfant);

        // Deuxième enfant -> 18 ans le mois courant
        REEnfantEcheances enfant2 = new REEnfantEcheances();
        enfant2.setIdTiers("2");
        enfant2.setDateNaissance("01.01.2000");
        entity.getEnfantsDuTiers().add(enfant2);

        RERenteJoinDemandeEcheance renteEnfant2 = new RERenteJoinDemandeEcheance();
        renteEnfant2.setIdPrestationAccordee("3");
        renteEnfant2.setCodePrestation("15");
        renteEnfant2.setDateDebutDroit("01.2011");
        enfant2.getRentes().add(renteEnfant2);
        assertTrue(module, entity, "01.2018", REMotifEcheance.RenteDeVeuf);

        // Si le premier enfant n'a pas de date de décès, il ne devrait pas y avoir des fin de rente
        enfant.setDateDeces("");
        assertFalse(module, entity, "01.2018");

    }

    @Test
    public void plusieursEnfantDontUnAyant18ansDansLeMois() {
        enfant.setDateNaissance("01.01.1994");
        enfant.getRentes().add(renteEnfant);

        // deuxième enfant plus âgé
        REEnfantEcheances enfant2 = new REEnfantEcheances();
        enfant2.setIdTiers("2");
        enfant2.setDateNaissance("01.01.1993");
        entity.getEnfantsDuTiers().add(enfant2);

        RERenteJoinDemandeEcheance renteEnfant2 = new RERenteJoinDemandeEcheance();
        renteEnfant2.setIdPrestationAccordee("3");
        renteEnfant2.setCodePrestation("15");
        renteEnfant2.setDateDebutDroit("01.2011");
        enfant2.getRentes().add(renteEnfant2);

        assertTrue(module, entity, "01.2012", REMotifEcheance.RenteDeVeuf);

        // on inverse les dates de naissance des enfants
        enfant.setDateNaissance("01.01.1993");
        enfant2.setDateNaissance("01.01.1994");
        entity.getEnfantsDuTiers().clear();
        entity.getEnfantsDuTiers().addAll(Arrays.asList(enfant, enfant2));

        assertTrue(module, entity, "01.2012", REMotifEcheance.RenteDeVeuf);
    }

    @Before
    public void setUp() {
        module = new REModuleEcheanceRenteDeVeuf(session, "01.2012");

        entity = new REEcheancesEntity();
        entity.setCsSexeTiers(ITIPersonne.CS_HOMME);

        rente = new RERenteJoinDemandeEcheance();
        rente.setIdPrestationAccordee("1");
        rente.setCodePrestation("13");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        rente.setDateDebutDroit("01.2011");
        entity.getRentesDuTiers().add(rente);

        enfant = new REEnfantEcheances();
        enfant.setIdTiers("1");
        entity.getEnfantsDuTiers().add(enfant);

        renteEnfant = new RERenteJoinDemandeEcheance();
        renteEnfant.setIdPrestationAccordee("2");
        renteEnfant.setCodePrestation("15");
        renteEnfant.setDateDebutDroit("01.2011");
    }
}
