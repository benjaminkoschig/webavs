package ch.globaz.vulpecula.domain.models.parametrage;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import java.net.URISyntaxException;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.junit.BeforeClass;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.absencejustifiee.LienParente;
import ch.globaz.vulpecula.domain.models.absencejustifiee.TypeAbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.servicemilitaire.GenreSM;

public class TableParametrageTest {
    private static TableParametrage tableParametrage;

    @BeforeClass
    public static void before() throws JAXBException, URISyntaxException {
        tableParametrage = TableParametrage.getInstance();
    }

    @Test
    public void getJoursMariage_GivenCaisse10_ShouldBe1() {
        assertEquals(1, tableParametrage.getJoursMariage(10));
    }

    @Test
    public void getJoursMariageEnfants_GivenCaisse10_ShouldBe1() {
        assertEquals(0, tableParametrage.getJoursMariageEnfants(10));
    }

    @Test
    public void getJoursMariage_GivenCaisse50_ShouldBe2() {
        assertEquals(2, tableParametrage.getJoursMariage(50));
    }

    @Test
    public void getJoursMariageEnfants_GivenCaisse40_ShouldBe1() {
        assertEquals(1, tableParametrage.getJoursMariageEnfants(40));
    }

    @Test
    public void getJoursDeces_GivenCaisse10AndParenteConjoint_ShouldBe3() {
        assertEquals(3, tableParametrage.getJoursDeces(10, LienParente.CONJOINT));
    }

    @Test
    public void getJoursDeces_GivenCaisse10AndParenteEnfants_ShouldBe3() {
        assertEquals(3, tableParametrage.getJoursDeces(10, LienParente.ENFANT));
    }

    @Test
    public void getJoursParents_GivenCaisse10AndParenteParent_ShouldBe2() {
        assertEquals(2, tableParametrage.getJoursDeces(10, LienParente.PARENT));
    }

    @Test
    public void getJoursParents_GivenCaisse10AndParenteBeauParent_ShouldBe2() {
        assertEquals(2, tableParametrage.getJoursDeces(10, LienParente.BEAU_PARENT));
    }

    @Test
    public void getJoursParents_GivenCaisse10AndParenteFrere_ShouldBe2() {
        assertEquals(2, tableParametrage.getJoursDeces(10, LienParente.FRERE));
    }

    @Test
    public void getJoursParents_GivenCaisse10AndParenteSoeur_ShouldBe2() {
        assertEquals(2, tableParametrage.getJoursDeces(10, LienParente.SOEUR));
    }

    @Test
    public void getJoursParents_GivenCaisse10AndParenteGrandParent_ShouldBe1() {
        assertEquals(1, tableParametrage.getJoursDeces(10, LienParente.GRAND_PARENT));
    }

    @Test
    public void getJoursParents_GivenCaisse10AndParenteBelleFille_ShouldBe0() {
        assertEquals(0, tableParametrage.getJoursDeces(10, LienParente.BELLE_FILLE));
    }

    @Test
    public void getJoursNaissance_GivenCaisse10_ShouldBe1() {
        assertEquals(3, tableParametrage.getJoursNaissance(10));
    }

    @Test
    public void getJoursRecrutementInfo_GivenCaisse10_ShouldBe1() {
        assertEquals(1.0, tableParametrage.getJoursRecrutementInfo(10), 0);
    }

    @Test
    public void getJoursLiber_GivenCaisse10_ShouldBe0_5() {
        assertEquals(0.5, tableParametrage.getJoursLiber(10), 0);
    }

    @Test
    public void getJoursLiber_GivenCaisse30_ShouldBe1() {
        assertEquals(1, tableParametrage.getJoursLiber(30), 0);
    }

    @Test
    public void getJoursLiber_GivenCaisse50_ShouldBe1() {
        assertEquals(0, tableParametrage.getJoursLiber(50), 0);
    }

    @Test
    public void getJoursDemenagement_GivenCaisse10PaidNon_ShouldBe0() {
        assertEquals(0, tableParametrage.getJoursDemenagement(10));
    }

    @Test
    public void getJoursDemenagement_GivenCaisse12Paid_ShouldBe0() {
        double nbJours = tableParametrage.getJoursDemenagement(12);
        assertThat(nbJours, is(0.0));
    }

    @Test
    public void getJoursDemenagement_GivenCaisse5_ShouldBe1() {
        assertEquals(1, tableParametrage.getJoursDemenagement(50));
    }

    @Test
    public void getJoursFeries_GivenCaisse10_ShouldBe0() {
        assertEquals(0, tableParametrage.getJoursFeries(10));
    }

    @Test
    public void getHoraireTravailJour_GivenCaisse10_ShouldBe8_20() {
        assertEquals(8.20, tableParametrage.getHeuresTravailJour(10), 0);
    }

    @Test
    public void getHoraireTravailSemaine_GivenCaisse10_ShouldBe41_00() {
        assertEquals(41.00, tableParametrage.getHeuresTravailSemaine(10), 0);
    }

    @Test
    public void getHoraireTravailMois_GivenCaisse10_ShouldBe41_00() {
        assertEquals(177.70, tableParametrage.getHeuresTravailMois(10), 0);
    }

    @Test
    public void getJoursFormation_GivenCaisse10_ShouldBe0() {
        assertEquals(0, tableParametrage.getJoursFormation(10));
    }

    @Test
    public void getJoursVacances_GivenCaisse10WithAgeOf49_ShouldBe25() {
        assertEquals(25, tableParametrage.getJoursVacances(10, 49));
    }

    @Test
    public void getJoursVacances_GivenCaisse10WithAgeOf50_ShouldBe30() {
        assertEquals(30, tableParametrage.getJoursVacances(10, 50));
    }

    @Test
    public void getJoursVacances_GivenCaisse10WithAgeOf51_ShouldBe30() {
        assertEquals(30, tableParametrage.getJoursVacances(10, 51));
    }

    @Test
    public void getJoursVacances_givenCaisse30WithAgeOf56_ShouldBe25() {
        assertEquals(25, tableParametrage.getJoursVacances(30, 56));
    }

    @Test
    public void getJoursVacances_givenCaisse30WithAgeOf57_ShouldBe30() {
        assertEquals(30, tableParametrage.getJoursVacances(30, 57));
    }

    @Test
    public void getTauxVacances_givenCaisse10AndJoursVacances25_ShouldBe16_04() {
        assertEquals(13.64, tableParametrage.getTauxVacances(10, 25), 0);
    }

    @Test
    public void getNbJoursVacances_givenCaisse10WithAgeOf50_ShouldBe30And13_64() {
        NbJoursTaux nbJoursTaux = tableParametrage.getNbJoursTaux(10, 50);
        assertEquals(30, nbJoursTaux.getNbJours());
        assertEquals(16.04, nbJoursTaux.getTaux(), 0);
    }

    @Test
    public void hasCotisationsCongesPayes_givenCaisse10_ShouldBeFlalse() {
        assertFalse(tableParametrage.hasCotisationsCongesPays(10));
    }

    @Test
    public void hasCotisationsCongesPayes_givenCaisse50_ShouldBeTrue() {
        assertTrue(tableParametrage.hasCotisationsCongesPays(50));
    }

    @Test
    public void getGratification_givenCaisse10_ShouldBe8_33() {
        assertThat(tableParametrage.getGratification(10), is(8.33));
    }

    @Test
    public void getCouvertureAPG_GivenCaisse10AndEcoleDeRecrue_ShouldBe50() {
        assertThat(tableParametrage.getCouvertureAPG(10, GenreSM.ECOLE_DE_RECRUE), is(new Taux(50)));
    }

    @Test
    public void getNombreJoursPrestationAJ_GivenCaisse10AndGenreDeuilLienConjoint_ShouldBe3() {
        double actual = tableParametrage.getNombreJoursPrestationAJ(10, TypeAbsenceJustifiee.DEUIL,
                LienParente.CONJOINT);
        assertThat(actual, is(3.0));
    }

    @Test
    public void getNombreJoursPrestationAJ_GivenCaisse10AndGenreDeuilLienPetitFile_ShouldBe3() {
        double actual = tableParametrage.getNombreJoursPrestationAJ(10, TypeAbsenceJustifiee.DEUIL,
                LienParente.PETIT_FILS);
        assertThat(actual, is(0.0));
    }

    @Test
    public void getNombreJoursPrestationAJ_GivenCaisse120AndGenreDeuilLienPetitFile_ShouldBe() {
        double actual = tableParametrage.getNombreJoursPrestationAJ(120, TypeAbsenceJustifiee.DEUIL,
                LienParente.PETIT_FILS);
        assertThat(actual, is(0.0));
    }

    @Test
    public void getNombreJoursPrestationAJ_GivenCaisse120AndGenreDeuilLienFrere_ShouldBe2() {
        double actual = tableParametrage.getNombreJoursPrestationAJ(120, TypeAbsenceJustifiee.DEUIL, LienParente.FRERE);
        assertThat(actual, is(2.0));
    }

    @Test
    public void getNombreJoursPrestationAJ_GivenCaisse10AndGenreDemenagement_ShouldBe0() {
        double actual = tableParametrage.getNombreJoursPrestationAJ(10, TypeAbsenceJustifiee.DEMENAGEMENT);
        assertThat(actual, is(0.0));
    }

    @Test
    public void getNombreJoursPrestationAJ_GivenCaisse12AndGenreDemenagement() {
        double nbJours = tableParametrage.getNombreJoursPrestationAJ(12, TypeAbsenceJustifiee.DEMENAGEMENT);
        assertThat(nbJours, is(0.0));
    }

    @Test
    public void getNombreJoursPrestationAJ_GivenCaisse10AndGenreInspection_ShouldBe0() {
        double actual = tableParametrage.getNombreJoursPrestationAJ(10, TypeAbsenceJustifiee.INSPECTION);
        assertThat(actual, is(0.0));
    }

    @Test
    public void getNombreJoursPrestationAJ_GivenCaisse10AndGenreInfoRecrutement_ShouldBe1() {
        double actual = tableParametrage.getNombreJoursPrestationAJ(10, TypeAbsenceJustifiee.INFO_RECRUTEMENT);
        assertThat(actual, is(1.0));
    }

    @Test
    public void getNombreJoursPrestationAJ_GivenCaisse10AndGenreMariage_ShouldBe1() {
        double actual = tableParametrage.getNombreJoursPrestationAJ(10, TypeAbsenceJustifiee.MARIAGE);
        assertThat(actual, is(1.0));
    }

    @Test
    public void getNombreJoursPrestationAJ_GivenCaisse10AndGenreNaissance_ShouldBe3() {
        double actual = tableParametrage.getNombreJoursPrestationAJ(10, TypeAbsenceJustifiee.NAISSANCE);
        assertThat(actual, is(3.0));
    }

    @Test
    public void getJoursInspection_GivenCaisse10_ShouldBe0() {
        assertThat(tableParametrage.getNombreJoursPrestationAJ(10, TypeAbsenceJustifiee.INSPECTION), is(0.0));
    }

    @Test
    public void IsTauxCPForce0_GivenCaisse10_ShouldBeFalse() {
        boolean tauxForce = tableParametrage.isTauxCPForce0(10);
        assertFalse(tauxForce);
    }

    @Test
    public void IsTauxCPForce0_GivenCaisse40_ShouldBeTrue() {
        boolean tauxForce = tableParametrage.isTauxCPForce0(40);
        assertTrue(tauxForce);
    }

    @Test
    public void getConfigurationSM_GivenCaisse10AndCoursRepetitionAnd30Ans_ShouldBeCorrect() {
        ConfigurationSM configurationSM = tableParametrage.getConfigurationSM(10, GenreSM.COURS_DE_REPETITION, 30);
        ConfigurationSM expected = new ConfigurationSM(10, 24, 100, 13.64, 8.33, false);

        assertThat(configurationSM, is(expected));
    }

    @Test
    public void getConfigurationSM_GivenCaisse40AndEcoleDeRecrueAnd30Ans_ShouldBeCorrect() {
        ConfigurationSM configurationSM = tableParametrage.getConfigurationSM(40, GenreSM.ECOLE_DE_RECRUE, 30);
        ConfigurationSM expected = new ConfigurationSM(0, 0, 50, 0, 0, true);

        assertThat(configurationSM, is(expected));
    }

    @Test
    public void hasDroitAJ_GivenCaisse10AndNoTypeAssurance_ShouldBeFalse() {
        assertThat(tableParametrage.hasDroitAJ(10), is(false));
    }

    @Test
    public void hasDroitAJ_GivenCaisse12AndNoTypeAssurance_ShouldBeFalse() {
        assertThat(tableParametrage.hasDroitAJ(12), is(false));
    }

    @Test
    public void hasDroitAJ_GivenCaisse10AndTypeAssuranceAJSM_ShouldBeTrue() {
        assertThat(tableParametrage.hasDroitAJ(10, TypeAssurance.COTISATION_SM_AJ), is(true));
    }

    @Test
    public void hasDroitSM_GivenCaisse10AndTypeAssuranceAJSM_ShouldBeTrue() {
        assertThat(tableParametrage.hasDroitSM(10, TypeAssurance.COTISATION_SM_AJ), is(true));
    }

    @Test
    public void hasDroitSM_GivenCaisse50AndTypeAssuranceAJSM_ShouldBeFalse() {
        assertThat(tableParametrage.hasDroitSM(50, TypeAssurance.COTISATION_SM_AJ), is(false));
    }

    @Test
    public void hasDroitSM_GivenCaisse50AndTypeAssuranceContributionGenerale_ShouldBeTrue() {
        assertThat(tableParametrage.hasDroitSM(50, TypeAssurance.CONTRIBUTION_GENERALE), is(true));
    }

    @Test
    public void hasDroitCP_GivenCaisse50AndTypeAssuranceContributionGenerale_ShouldBeTrue() {
        assertThat(tableParametrage.hasDroitCP(50, TypeAssurance.CONTRIBUTION_GENERALE), is(true));
    }

    @Test
    public void getHeuresTravailMois_GivenCaisseMetier120_ShouldReturn0() {
        assertThat(tableParametrage.getHeuresTravailMois(120), is(180.0));
    }

    @Test
    public void hasDroitCP_GivenCaisseMetier12WithAssuranceSMAJ_ShouldBeFalse() {
        boolean actual = tableParametrage.hasDroitCP(12, TypeAssurance.COTISATION_SM_AJ);
        assertThat(actual, is(false));
    }

    @Test
    public void getTypeAssuranceObligatoireForCP_Given120_ShouldContainsOneAssurance() {
        List<TypeAssurance> types = tableParametrage.getTypeAssuranceObligatoireForCP(120);
        assertEquals(1, types.size());
    }

    @Test
    public void getTypeAssuranceObligatoireForCP_Given1200_ShouldBeEmpty() {
        List<TypeAssurance> types = tableParametrage.getTypeAssuranceObligatoireForCP(1200);
        assertEquals(true, types.isEmpty());
    }

    @Test
    public void getTypeAssuranceObligatoireForCP_Given12_ShouldBeEmpty() {
        List<TypeAssurance> types = tableParametrage.getTypeAssuranceObligatoireForCP(12);
        assertEquals(1, types.size());
    }
}
