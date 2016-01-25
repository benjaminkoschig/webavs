package ch.globaz.vulpecula.domain.models.common;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

public class PeriodeTest {

    @Test
    public void givenTwoDates01012013And01012012ShouldBeInvalid() {
        Date dateDebut = new Date("20130101");
        Date dateFin = new Date("20120101");

        try {
            new Periode(dateDebut, dateFin);
            Assert.fail("La date de début est supérieure à la date de fin et l'exception n'a pas été levée");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void givenTwoDates01012013And30012013ShouldBeValid() {
        Date dateDebut = new Date("20130101");
        Date dateFin = new Date("20131230");

        new Periode(dateDebut, dateFin);
    }

    @Test
    public void givenTwoDates01012013And01012013ShouldBeValid() {
        Date dateDebut = new Date("20130101");
        Date dateFin = new Date("20130101");

        new Periode(dateDebut, dateFin);
    }

    @Test
    public void givenTwoPeriodesChevauchentWhenChevaucheWhenChevaucheShouldReturnTrue() {
        Date dateDebut1 = new Date("01.03.2014");
        Date dateFin1 = new Date("01.05.2014");

        Date dateDebut2 = new Date("01.04.2014");
        Date dateFin2 = new Date("01.06.2014");

        Periode periode1 = new Periode(dateDebut1, dateFin1);
        Periode periode2 = new Periode(dateDebut2, dateFin2);

        assertTrue(periode1.chevauche(periode2));
        assertTrue(periode2.chevauche(periode1));
    }

    @Test
    public void givenTwoPeriodesChevauchentWithDateDebutWhenChevaucheShouldReturnTrue() {
        Date dateDebut1 = new Date("01.03.2014");
        Date dateFin1 = new Date("01.05.2014");

        Date dateDebut2 = new Date("01.03.2014");
        Date dateFin2 = new Date("01.03.2014");

        Periode periode1 = new Periode(dateDebut1, dateFin1);
        Periode periode2 = new Periode(dateDebut2, dateFin2);

        assertTrue(periode1.chevauche(periode2));
        assertTrue(periode2.chevauche(periode1));
    }

    @Test
    public void givenTwoPeriodesIndependantesWhenChevaucheShouldReturnTrue() {
        Date dateDebut1 = new Date("01.03.2014");
        Date dateFin1 = new Date("01.05.2014");

        Date dateDebut2 = new Date("01.05.2014");
        Date dateFin2 = new Date("01.06.2014");

        Periode periode1 = new Periode(dateDebut1, dateFin1);
        Periode periode2 = new Periode(dateDebut2, dateFin2);

        assertFalse(periode1.chevauche(periode2));
        assertFalse(periode2.chevauche(periode1));
    }

    @Test
    public void givenTwoPeriodesChevauchentWithoutDatefinWhenChevaucheShouldReturnTrue() {
        Date dateDebut1 = new Date("01.03.2014");

        Date dateDebut2 = new Date("01.05.2014");

        Periode periode1 = new Periode(dateDebut1);
        Periode periode2 = new Periode(dateDebut2);

        assertTrue(periode1.chevauche(periode2));
        assertTrue(periode2.chevauche(periode1));
    }

    @Test
    public void givenTwoPeriodesChevauchentWithoutDateFinOnFirstDateWhenChevaucheShouldReturnTrue() {
        Date dateDebut1 = new Date("01.03.2014");

        Date dateDebut2 = new Date("01.05.2014");
        Date dateFin2 = new Date("01.06.2014");

        Periode periode1 = new Periode(dateDebut1);
        Periode periode2 = new Periode(dateDebut2, dateFin2);

        assertTrue(periode1.chevauche(periode2));
        assertTrue(periode2.chevauche(periode1));
    }

    @Test
    public void givenTwoPeriodesChevauchentWithoutDateFinOnSecondDateWhenChevaucheShouldReturnTrue() {
        Date dateDebut1 = new Date("01.03.2014");
        Date dateFin1 = new Date("01.06.2014");

        Date dateDebut2 = new Date("01.05.2014");

        Periode periode1 = new Periode(dateDebut1, dateFin1);
        Periode periode2 = new Periode(dateDebut2);

        assertTrue(periode1.chevauche(periode2));
        assertTrue(periode2.chevauche(periode1));
    }

    @Test
    public void givenTwoPeriodesIndependantesQuiSeSuiventWhenChevaucheShouldReturnTrue() {
        Date dateDebut1 = new Date("01.03.2014");
        Date dateFin1 = new Date("01.04.2014");

        Date dateDebut2 = new Date("01.04.2014");
        Date dateFin2 = new Date("01.05.2014");

        Periode periode1 = new Periode(dateDebut1, dateFin1);
        Periode periode2 = new Periode(dateDebut2, dateFin2);

        assertFalse(periode1.chevauche(periode2));
        assertFalse(periode2.chevauche(periode1));
    }

    @Test
    public void givenTwoPeriodesChevauchentOneWithDefinitePeriodeAndOneWithUndefinedWhenChevauchePeriodeShouldReturnTrue() {
        Date dateDebut1 = new Date("01.03.1987");

        Date dateDebut2 = new Date("01.04.2014");
        Date dateFin2 = new Date("01.04.2014");

        Periode periode1 = new Periode(dateDebut1);
        Periode periode2 = new Periode(dateDebut2, dateFin2);

        assertTrue(periode1.chevauche(periode2));
        assertTrue(periode2.chevauche(periode1));
    }

    @Test
    public void givenTwoValidDatesAsStringShouldBeValid() {
        new Periode("01.01.2014", "03.03.2014");
    }

    @Test
    public void givenAValidStartDateAndNullAsStringShouldBeValid() {
        new Periode("01.01.2014", null);
    }

    @Test
    public void givenAValidStartDateAndEmptyAsStringShouldBeValid() {
        new Periode("01.01.2014", "");
    }

    @Test
    public void givenTwoValidStartWhereEndDateIsBeforeStartDateShouldBeInvalid() {
        try {
            new Periode("01.01.2014", "01.01.2013");
            fail("La date de fin est avant la date de début et l'exception n'a pas été levée");
        } catch (IllegalArgumentException ex) {

        }
    }

    @Test
    public void givenOnePeriodeAndOneDateOutsideItWhenContainsShouldReturnFalse() {
        Periode periode = new Periode("01.01.2014", "01.03.2014");
        Date date = new Date("31.01.2013");

        assertFalse(periode.contains(date));
    }

    @Test
    public void givenOnePeriodeAndOneDateInsideItWhenContainsShouldReturnTrue() {
        Periode periode = new Periode("01.01.2014", "01.03.2014");
        Date date = new Date("01.01.2014");

        assertTrue(periode.contains(date));
    }

    @Test
    public void givenOnePeriodeWithNoEndAndOneDateInsideItWhenContainsShouldReturnTrue() {
        Periode periode = new Periode("01.01.2014", null);
        Date date = new Date("01.01.2016");

        assertTrue(periode.contains(date));
    }

    @Test
    public void givenOnePeriodeWithEndAndOneDateAtEndWhenContainsShouldReturnTrue() {
        Periode periode = new Periode("01.01.2014", "01.01.2016");
        Date date = new Date("01.01.2016");

        assertTrue(periode.contains(date));
    }

    @Test
    public void givenOnePeriodeWithEndAndOneDateWhenContainsShouldReturnTrue() {
        Periode periode = new Periode("01.01.2014", "01.01.2016");
        Date date = new Date("31.12.2015");

        assertTrue(periode.contains(date));
    }

    @Test
    public void compareTo_givenSameReferencePeriode_ThenReturn0() {
        Periode periode = new Periode("01.01.2014", "01.01.2016");

        int actual = periode.compareTo(periode);
        assertEquals(0, actual);
    }

    @Test
    public void compareTo_givenSamePeriode_ThenReturn0() {
        Periode periode1 = new Periode("01.01.2014", "01.01.2016");
        Periode periode2 = new Periode("01.01.2014", "01.01.2016");

        int actual = periode1.compareTo(periode2);
        assertEquals(0, actual);
    }

    @Test
    public void compareTo_givenPeriode1BeforePeriode2WithNullFin_ThenReturn1() {
        Periode periode1 = new Periode("01.01.2013", null);
        Periode periode2 = new Periode("01.01.2012", "01.01.2016");

        int actual = periode1.compareTo(periode2);

        assertEquals(1, actual);
    }

    @Test
    public void compareTo_givenPeriode1BeforePeriode2WithBothNull_ThenReturnMinus1() {
        Periode periode1 = new Periode("01.01.2012", null);
        Periode periode2 = new Periode("01.01.2013", null);

        int actual = periode1.compareTo(periode2);

        assertEquals(-1, actual);
    }

    @Test
    public void compareTo_givenSamePeriodsWithNull_ThenReturn0() {
        Periode periode1 = new Periode("01.01.2012", null);
        Periode periode2 = new Periode("01.01.2012", null);

        int actual = periode1.compareTo(periode2);

        assertEquals(0, actual);
    }

    @Test
    public void containsPeriode_givenSamePeriodeShouldReturnTrue() {
        Periode periode1 = new Periode("01.01.2014", "01.03.2014");
        Periode periode2 = new Periode("01.01.2014", "01.03.2014");

        assertTrue(periode1.contains(periode2));
    }

    @Test
    public void containsPeriode_givenOnePeriode1AndOneOtherPeriode2IncludedShouldReturnTrue() {
        Periode periode1 = new Periode("01.01.2014", "01.03.2014");
        Periode periode2 = new Periode("01.02.2014", "20.02.2014");

        assertTrue(periode1.contains(periode2));
    }

    @Test
    public void containsPeriode_givenOnePeriode1AndAnotherPeriode2NotIncludedShouldReturnFalse() {
        Periode periode1 = new Periode("01.01.2014", "01.03.2014");
        Periode periode2 = new Periode("01.02.2014", "01.04.2014");

        assertFalse(periode1.contains(periode2));
    }

    @Test
    public void containsPeriode_givenOnePeriode1AndAnotherPeriode2NotIncludedAtAllShouldReturnFalse() {
        Periode periode1 = new Periode("01.01.2014", "01.03.2014");
        Periode periode2 = new Periode("01.02.2013", "01.04.2015");

        assertFalse(periode1.contains(periode2));
    }

    @Test
    public void containsPeriode_givenOnePeriode1AndAnotherIncludedPeriode2WithNoEndShouldReturnTrue() {
        Periode periode1 = new Periode("01.01.2014", "01.03.2014");
        Periode periode2 = new Periode("01.02.2014", null);

        assertTrue(periode1.contains(periode2));
    }

    @Test
    public void compareTo_givenOnePeriode1AndAnotherIncludedPeriode2WithNoEndShouldReturnChevauche() {
        Periode periode1 = new Periode("01.07.1974", "30.09.1995");
        Periode periode2 = new Periode("01.07.1974", null);

        boolean seChevauche = periode1.chevauche(periode2);

        assertTrue(seChevauche);
    }

    @Test
    public void isSameMonthAndYear_WithTwoDifferentsPeriodesWithoutDateFin_ShouldBeTrue() {
        Periode periode1 = new Periode("01.01.2014", null);
        Periode periode2 = new Periode("15.01.2014", null);

        assertTrue(periode1.isSameMonthAndYear(periode2));
    }

    @Test
    public void isSameMonthAndYear_WithPeriodesWithoutDateFin_ShouldBeFalse() {
        Periode periode1 = new Periode("01.01.2014", null);

        assertFalse(periode1.isSameMonthAndYear());
    }

    @Test
    public void chevauche_GivenNoPeriode_ShouldReturnNull() {
        Periode periode = new Periode("01.01.2014", null);

        assertNull(periode.chevauche(new ArrayList<Periode>()));
    }

    @Test
    public void chevauche_WithPeriodeSansDateFin_GivenPeriodeNonChevauchanteAuDebut_ShouldBeNull() {
        Periode periode = new Periode("01.01.2014", null);
        Periode periodeNonChevauchante = new Periode("01.01.2010", "31.12.2013");

        assertNull(periode.chevauche(Arrays.asList(periodeNonChevauchante)));
    }

    @Test
    public void chevauche_WithPeriodeSansDateFin_GivenPeriodeChevauchanteFin_ShouldReturnThisPeriode() {
        Periode periode = new Periode("01.01.2014", null);
        Periode periodeChevauchante = new Periode("01.01.2010", "01.01.2014");

        assertNull(periode.chevauche(Arrays.asList(periodeChevauchante)));
    }

    @Test
    public void seChevauchent_GivenOnePeriode_ShouldReturnEmptyMap() {
        Periode periode = new Periode("01.01.2015", null);
        assertTrue(Periode.seChevauchent(Arrays.asList(periode)).isEmpty());
    }

    @Test
    public void seChevauchent_GivenTwoPeriodeSeChevauchent_ShouldMapWithOneEntry() {
        Periode periode = new Periode("01.01.2015", null);
        Periode periode2 = new Periode("01.01.2013", null);
        assertEquals(1, Periode.seChevauchent(Arrays.asList(periode, periode2)).size());
    }

    @Test
    public void isMemeAnnee_Given01012013_31122014_ShouldBeFalse() {
        Periode periode = new Periode("01.01.2013", "31.12.2014");
        assertThat(periode.isSameYear(), is(false));
    }

    @Test
    public void isMemeAnnee_Given01012013_31122013_ShouldBeFalse() {
        Periode periode = new Periode("01.01.2013", "31.12.2013");
        assertThat(periode.isSameYear(), is(true));
    }

    @Test
    public void isMemeAnnee_Given01012013_null_ShouldBeFalse() {
        Periode periode = new Periode("01.01.2013", null);
        assertThat(periode.isSameYear(), is(false));
    }

    @Test
    public void isActifToday_Given010012010_ShouldBeTrue() {
        Periode periode = new Periode("02.01.2010", null);
        assertThat(periode.isActif(), is(true));
    }

    @Test
    public void isActif01012010_Given01012010_ShouldBeFalse() {
        Periode periode = new Periode("01.01.2099", null);
        assertThat(periode.isActif(), is(false));
    }

    @Test
    public void isActif_GivenPeriodeNotInActivityWithEndDate_ShouldReturnFalse() {
        Periode periode = new Periode(new Date("01.01.2014"), new Date("01.01.2015"));
        Periode periodeDemande = new Periode(new Date("01.01.2013"), new Date("01.02.2013"));
        assertEquals(false, periode.isActif(periodeDemande));
    }

    @Test
    public void isActif_GivenPeriodeInActivityWithEndDate_ShouldReturnTrue() {
        Periode periode = new Periode(new Date("01.01.2014"), new Date("01.01.2015"));
        Periode periodeDemande = new Periode(new Date("01.01.2013"), new Date("01.02.2014"));
        assertEquals(true, periode.isActif(periodeDemande));
    }

    @Test
    public void isActif_GivenPeriodeNotInActivityWithoutEndDate_ShouldReturnFalse() {
        Periode periode = new Periode(new Date("01.01.2014"), null);
        Periode periodeDemande = new Periode(new Date("01.01.2013"), new Date("01.02.2013"));
        assertEquals(false, periode.isActif(periodeDemande));
    }

    @Test
    public void isActif_GivenPeriodeInActivityWithoutEndDate_ShouldReturnTrue() {
        Periode periode = new Periode(new Date("01.01.2014"), null);
        Periode periodeDemande = new Periode(new Date("01.01.2013"), new Date("01.02.2014"));
        assertEquals(true, periode.isActif(periodeDemande));
    }

    @Test
    public void isActif() {
        Periode periode = new Periode(new Date("01.03.2015"), new Date("31.03.2015"));
        assertEquals(true, periode.chevauche(new Periode("01.01.2015", "31.12.2015")));
    }

}
