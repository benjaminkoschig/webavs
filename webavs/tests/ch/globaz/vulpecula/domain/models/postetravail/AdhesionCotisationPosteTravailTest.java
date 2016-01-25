package ch.globaz.vulpecula.domain.models.postetravail;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;

/**
 * @author Arnaud Geiser (AGE) | Créé le 24 mars 2014
 * 
 */
public class AdhesionCotisationPosteTravailTest {
    private AdhesionCotisationPosteTravail adhesionCotisationPosteTravail;

    @Before
    public void setUp() {
        adhesionCotisationPosteTravail = new AdhesionCotisationPosteTravail();
    }

    @Test
    public void givenAdhesionCotisationWithPeriodeFrom03042014To05062016WhenGetDateEntreeThenReturn03042014() {
        adhesionCotisationPosteTravail.setPeriode(new Periode("03.04.2014", "05.06.2016"));

        Assert.assertEquals("03.04.2014", adhesionCotisationPosteTravail.getDateEntree());
    }

    @Test
    public void givenPeriode2004To2030AndCotisationWithDateFin01012006WhenIsActifThenReturnFalse() {
        Cotisation coti = mock(Cotisation.class);
        when(coti.getDateFin()).thenReturn(new Date("01.01.2006"));
        adhesionCotisationPosteTravail.setCotisation(coti);

        adhesionCotisationPosteTravail.setPeriode(new Periode("01.01.2004", "01.01.2030"));
        Assert.assertFalse(adhesionCotisationPosteTravail.isActif(new Date("01.01.2015")));
    }

    @Test
    public void givenPeriode2004To2030AndCotisationWithDateFinNullWhenIsActifThenReturnTrue() {
        Cotisation coti = mock(Cotisation.class);
        when(coti.getDateFin()).thenReturn(null);
        adhesionCotisationPosteTravail.setCotisation(coti);

        adhesionCotisationPosteTravail.setPeriode(new Periode("01.01.2004", "01.01.2030"));
        Assert.assertTrue(adhesionCotisationPosteTravail.isActif(new Date("01.01.2015")));
    }

    @Test
    public void givenPeriode2004To2014AndCotisationWithDateFinNullWhenIsActifThenReturnFalse() {
        Cotisation coti = mock(Cotisation.class);
        when(coti.getDateFin()).thenReturn(null);
        adhesionCotisationPosteTravail.setCotisation(coti);

        adhesionCotisationPosteTravail.setPeriode(new Periode("01.01.2004", "01.01.2014"));
        Assert.assertFalse(adhesionCotisationPosteTravail.isActif(new Date("01.01.2015")));
    }

    @Test
    public void isActifIn2004_Given01012004And31012014_ShouldBeTrue() {
        adhesionCotisationPosteTravail.setPeriode(new Periode("01.01.2004", "31.01.2004"));
        assertTrue(adhesionCotisationPosteTravail.isActifIn(new Annee(2004)));
    }

    @Test
    public void isActifIn2004_Given01012002And31012014_ShouldBeTrue() {
        adhesionCotisationPosteTravail.setPeriode(new Periode("01.01.2002", "31.01.2004"));
        assertTrue(adhesionCotisationPosteTravail.isActifIn(new Annee(2004)));
    }

    @Test
    public void isActifIn2004_Given07012004And31082004_ShouldBeTrue() {
        adhesionCotisationPosteTravail.setPeriode(new Periode("07.01.2004", "31.08.2004"));
        assertTrue(adhesionCotisationPosteTravail.isActifIn(new Annee(2004)));
    }

    @Test
    public void isActifIn2004_Given07012003And31122003_ShouldBeFalse() {
        adhesionCotisationPosteTravail.setPeriode(new Periode("07.01.2003", "31.12.2003"));
        assertFalse(adhesionCotisationPosteTravail.isActifIn(new Annee(2004)));
    }

    @Test
    public void isActifIn2004_Given07012003_ShouldBeTrue() {
        adhesionCotisationPosteTravail.setPeriode(new Periode("07.01.2003", null));
        assertTrue(adhesionCotisationPosteTravail.isActifIn(new Annee(2004)));
    }

    @Test
    public void isActifPourLeMois01022015_GivenCotisationWithoutDateFinAndPeriodeInMonthFevrier_ShouldBeTrue() {
        Cotisation cotisation = createCotisation("01.01.2015");
        adhesionCotisationPosteTravail.setPeriode(new Periode("15.02.2015", "17.02.2015"));
        adhesionCotisationPosteTravail.setCotisation(cotisation);
        assertTrue(adhesionCotisationPosteTravail.isActifPourLeMois(new Date("01.02.2015")));
    }

    @Test
    public void isActifPourLeMois01022015_GivenCotisationWithoutDateFinAndPeriodeBetweenJanvierAndFevrier_ShouldBeTrue() {
        Cotisation cotisation = createCotisation("01.01.2015");
        adhesionCotisationPosteTravail.setPeriode(new Periode("15.01.2015", "17.02.2015"));
        adhesionCotisationPosteTravail.setCotisation(cotisation);
        assertTrue(adhesionCotisationPosteTravail.isActifPourLeMois(new Date("01.02.2015")));
    }

    @Test
    public void isActifPourLeMois01022015_GivenCotisationWithDateFinJanvierAndPeriodeBetweenJanvierAndFevrier_ShouldBeFalse() {
        Cotisation cotisation = createCotisation("01.01.2015", "31.01.2015");
        adhesionCotisationPosteTravail.setPeriode(new Periode("15.01.2015", "17.02.2015"));
        adhesionCotisationPosteTravail.setCotisation(cotisation);
        assertFalse(adhesionCotisationPosteTravail.isActifPourLeMois(new Date("01.02.2015")));
    }

    @Test
    public void isActifPourLeMois01032015_GivenCotisationWithoutDateFinAndPeriodeBetweenJanvierAndFevrier_ShouldBeFalse() {
        Cotisation cotisation = createCotisation("01.01.2015");
        adhesionCotisationPosteTravail.setPeriode(new Periode("15.01.2015", "17.02.2015"));
        adhesionCotisationPosteTravail.setCotisation(cotisation);
        assertFalse(adhesionCotisationPosteTravail.isActifPourLeMois(new Date("01.03.2015")));
    }

    @Test
    public void isActifPourLeMois01032015_GivenCotisationWithoutDateFinAndPeriodeBetweenJanvierAndNone_ShouldBeTrue() {
        Cotisation cotisation = createCotisation("01.01.2015");
        adhesionCotisationPosteTravail.setPeriode(new Periode("15.01.2015", null));
        adhesionCotisationPosteTravail.setCotisation(cotisation);
        assertTrue(adhesionCotisationPosteTravail.isActifPourLeMois(new Date("01.03.2015")));
    }

    @Test
    public void aIgnorer1() {
        Convention convention = createConvention(true);
        configureIgnorer(TypeAssurance.CONTRIBUTION_GENERALE);
        assertEquals(true, adhesionCotisationPosteTravail.aIgnorer(TypeDecompte.COMPLEMENTAIRE, convention));
    }

    @Test
    public void aIgnorer2() {
        Convention convention = createConvention(false);
        configureIgnorer(TypeAssurance.CONTRIBUTION_GENERALE);
        assertEquals(false, adhesionCotisationPosteTravail.aIgnorer(TypeDecompte.COMPLEMENTAIRE, convention));
    }

    @Test
    public void aIgnorer3() {
        Convention convention = createConvention(true);
        configureIgnorer(TypeAssurance.CONGES_PAYES);
        assertEquals(false, adhesionCotisationPosteTravail.aIgnorer(TypeDecompte.COMPLEMENTAIRE, convention));
    }

    @Test
    public void aIgnorer4() {
        Convention convention = createConvention(false);
        configureIgnorer(TypeAssurance.CONGES_PAYES);
        assertEquals(true, adhesionCotisationPosteTravail.aIgnorer(TypeDecompte.COMPLEMENTAIRE, convention));
    }

    @Test
    public void aIgnorer5() {
        Convention convention = createConvention(false);
        configureIgnorer(TypeAssurance.CPR_EMPLOYEUR);
        assertEquals(false, adhesionCotisationPosteTravail.aIgnorer(TypeDecompte.COMPLEMENTAIRE, convention));
    }

    private Convention createConvention(boolean isElectricite) {
        Convention convention = mock(Convention.class);
        when(convention.isElectricite()).thenReturn(isElectricite);
        return convention;
    }

    private void configureIgnorer(TypeAssurance typeAssurance) {
        Cotisation cotisation = mock(Cotisation.class);
        when(cotisation.getTypeAssurance()).thenReturn(typeAssurance);
        adhesionCotisationPosteTravail.setCotisation(cotisation);
    }

    private Cotisation createCotisation(String date) {
        Cotisation cotisation = new Cotisation();
        cotisation.setDateDebut(new Date(date));
        return cotisation;
    }

    private Cotisation createCotisation(String dateDebut, String dateFin) {
        Cotisation cotisation = new Cotisation();
        cotisation.setDateDebut(new Date(dateDebut));
        cotisation.setDateFin(new Date(dateFin));
        return cotisation;
    }
}
