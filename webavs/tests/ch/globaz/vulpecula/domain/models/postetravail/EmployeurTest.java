package ch.globaz.vulpecula.domain.models.postetravail;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.external.models.affiliation.Assurance;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;

/**
 * @author Arnaud Geiser (AGE) | Créé le 14 janv. 2014
 * 
 */
public class EmployeurTest {
    private Employeur employeur;

    @Before
    public void setUp() {
        employeur = new Employeur();
    }

    @Test
    public void givenEmployeurSoumisAVSWhenIsSoumisAVSThenReturnTrue() {
        addCotisation(TypeAssurance.COTISATION_AVS_AI);

        Assert.assertTrue(employeur.isSoumisAVS());
    }

    @Test
    public void givenEmployeurWithoutCotisationsWhenIsSoumisAVSThenReturnFalse() {
        Assert.assertFalse(employeur.isSoumisAVS());
    }

    @Test
    public void givenEmployeurWithTwoCotisationsWhereOneIsAVSWhenIsSoumisAVSThenReturnTrue() {
        addCotisation(TypeAssurance.ASSURANCE_CHOMAGE);
        addCotisation(TypeAssurance.COTISATION_AVS_AI);

        Assert.assertTrue(employeur.isSoumisAVS());
    }

    @Test
    public void givenEmployeurWithOneACCotisationWhenIsSoumisAVSThenReturnFalse() {
        addCotisation(TypeAssurance.ASSURANCE_CHOMAGE);

        Assert.assertFalse(employeur.isSoumisAVS());
    }

    @Test
    public void givenEmployeurWithoutCotisationsWhenIsSoumisACThenReturnFalse() {
        Assert.assertFalse(employeur.isSoumisAC());
    }

    @Test
    public void givenEmployeurWithOneACCotisationWhenIsSoumisACThenReturnTrue() {
        addCotisation(TypeAssurance.ASSURANCE_CHOMAGE);

        Assert.assertTrue(employeur.isSoumisAC());
    }

    @Test
    public void givenEmployeurWithOneAVSCotisationWhenIsSoumisACThenReturnFalse() {
        addCotisation(TypeAssurance.COTISATION_AVS_AI);

        Assert.assertFalse(employeur.isSoumisAC());
    }

    @Test
    public void givenEmployeurWithTwoCotisationsWhereOneIsACWhenIsSoumisACThenReturnTrue() {
        addCotisation(TypeAssurance.COTISATION_AVS_AI);
        addCotisation(TypeAssurance.ASSURANCE_CHOMAGE);

        Assert.assertTrue(employeur.isSoumisAC());
    }

    @Test
    public void givenEmployeurWithoutCotisationsWhenIsSoumisAC2ThenReturnFalse() {
        Assert.assertFalse(employeur.isSoumisAC2());
    }

    @Test
    public void givenEmployeurWithOneACCotisationWhenIsSoumisAC2ThenReturnFalse() {
        addCotisation(TypeAssurance.ASSURANCE_CHOMAGE);

        Assert.assertFalse(employeur.isSoumisAC2());
    }

    @Test
    public void givenEmployeurWithOneAC2CotisationWhenIsSoumisAC2ThenReturnTrue() {
        addCotisation(TypeAssurance.COTISATION_AC2);

        Assert.assertTrue(employeur.isSoumisAC2());
    }

    @Test
    public void givenEmployeurWithoutPosteTravailWhenGetTauxDifferentsPostes() {
        Assert.assertEquals(0, employeur.getTauxContribuablesDifferentsDesPostes().size());
    }

    @Test
    public void givenEmployeurWithPosteTravailOfTauxContribuable10WhenGetTauxDifferentsPostesThenContains10() {
        addPosteWithTauxContribuableOf(12.0);

        Assert.assertEquals(employeur.getTauxContribuablesDifferentsDesPostes().size(), 1);
        Assert.assertThat(employeur.getTauxContribuablesDifferentsDesPostes(), hasItem(new Taux(12.0)));
    }

    @Test
    public void givenEmployeurWithTwoPosteTravailsWithSameTauxContribuableWhenGetTauxDifferentsPostesThenContainsOnlyOne10() {
        addPosteWithTauxContribuableOf(10.0);
        addPosteWithTauxContribuableOf(10.0);

        Assert.assertEquals(employeur.getTauxContribuablesDifferentsDesPostes().size(), 1);
        Assert.assertThat(employeur.getTauxContribuablesDifferentsDesPostes(), hasItem(new Taux(10.0)));
    }

    @Test
    public void givenEmployeurWithSixPosteTravailsWithTauxContribuableOf5And2And5And2_5And3_5And3_5WhenGetTauxDifferentsPostesThenContains2And2_5And3_5And5() {
        addPosteWithTauxContribuableOf(5.0);
        addPosteWithTauxContribuableOf(2.0);
        addPosteWithTauxContribuableOf(5.0);
        addPosteWithTauxContribuableOf(2.5);
        addPosteWithTauxContribuableOf(3.5);
        addPosteWithTauxContribuableOf(3.5);

        Taux[] expected = { new Taux(2.0), new Taux(2.5), new Taux(3.5), new Taux(5.0) };

        Assert.assertEquals(employeur.getTauxContribuablesDifferentsDesPostes().size(), 4);
        Assert.assertThat(employeur.getTauxContribuablesDifferentsDesPostes(), hasItems(expected));
    }

    @Test
    public void equals() {
        Employeur employeur = new Employeur();
        employeur.setId("1");
        Employeur employeur2 = new Employeur();
        employeur2.setId("1");
        List<Employeur> employeurs = new ArrayList<Employeur>(Arrays.asList(employeur));
        List<Employeur> employeurs2 = new ArrayList<Employeur>(Arrays.asList(employeur2));

        assertTrue(employeurs.removeAll(employeurs2));
        assertEquals(employeurs.size(), 0);
    }

    public void addPosteWithTauxContribuableOf(final double tauxContribuable) {
        PosteTravail posteTravail = mock(PosteTravail.class);
        when(posteTravail.getTauxContribuable()).thenReturn(new Taux(tauxContribuable));

        employeur.addPosteTravail(posteTravail);
    }

    public void addCotisation(final TypeAssurance typeAssurance) {
        Assurance assurance = new Assurance();
        assurance.setTypeAssurance(typeAssurance);

        Cotisation cotisation = new Cotisation();
        cotisation.setAssurance(assurance);

        employeur.addCotisation(cotisation);
    }

    @Test
    public void isActif_GivenPeriodeNotInActivityWithEndDate_ShouldReturnFalse() {
        employeur.setDateDebut("01.01.2014");
        employeur.setDateFin("01.01.2015");
        Periode periodeDemande = new Periode(new Date("01.01.2013"), new Date("01.02.2013"));
        assertEquals(false, employeur.isActif(periodeDemande));
    }

    @Test
    public void isActif_GivenPeriodeInActivityWithEndDate_ShouldReturnTrue() {
        employeur.setDateDebut("01.01.2014");
        employeur.setDateFin("01.01.2015");
        Periode periodeDemande = new Periode(new Date("01.01.2013"), new Date("01.02.2014"));
        assertEquals(true, employeur.isActif(periodeDemande));
    }

    @Test
    public void isActif_GivenPeriodeNotInActivityWithoutEndDate_ShouldReturnFalse() {
        employeur.setDateDebut("01.01.2014");
        employeur.setDateFin(null);
        Periode periodeDemande = new Periode(new Date("01.01.2013"), new Date("01.02.2013"));
        assertEquals(false, employeur.isActif(periodeDemande));
    }

    @Test
    public void isActif_GivenPeriodeInActivityWithoutEndDate_ShouldReturnTrue() {
        employeur.setDateDebut("01.01.2014");
        employeur.setDateFin(null);
        Periode periodeDemande = new Periode(new Date("01.01.2013"), new Date("01.02.2014"));
        assertEquals(true, employeur.isActif(periodeDemande));
    }

    @Test
    public void isActif() {
        employeur.setDateDebut("01.03.2015");
        employeur.setDateFin("01.12.2016");
        Periode periodeDemande = new Periode(new Date("01.01.2015"), new Date("31.12.2015"));
        assertEquals(true, employeur.isActif(periodeDemande));
    }
}
