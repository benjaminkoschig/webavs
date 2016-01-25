package ch.globaz.vulpecula.domain.models.absencejustifiee;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.prestations.Etat;

public class AbsenceJustifieeTest {
    private AbsenceJustifiee absenceJustifiee;

    @Before
    public void setUp() {
        absenceJustifiee = new AbsenceJustifiee();
    }

    @Test
    public void isModifiable_WithEmptyAbsence_ShouldBeTrue() {
        assertTrue(absenceJustifiee.isModifiable());
    }

    @Test
    public void isModifiable_AbsenceWithEtatComptabilisee_ShouldBeFalse() {
        absenceJustifiee.setEtat(Etat.COMPTABILISEE);
        assertFalse(absenceJustifiee.isModifiable());
    }

    @Test
    public void isModifiable_AbsenceWithEtatSaisie_ShouldBeTrue() {
        absenceJustifiee.setEtat(Etat.SAISIE);
        assertTrue(absenceJustifiee.isModifiable());
    }

    @Test
    public void compareTo_emptiesAbsences_ShouldBe0() {
        AbsenceJustifiee absenceJustifiee = new AbsenceJustifiee();
        AbsenceJustifiee absenceJustifiee2 = new AbsenceJustifiee();

        assertThat(absenceJustifiee.compareTo(absenceJustifiee2), is(0));
    }

    @Test
    public void compareTo_absenceWithId1AndWithId2_ShouldBeMinus1() {
        AbsenceJustifiee absenceJustifiee = new AbsenceJustifiee();
        absenceJustifiee.setId("1");
        AbsenceJustifiee absenceJustifiee2 = new AbsenceJustifiee();
        absenceJustifiee2.setId("2");

        assertThat(absenceJustifiee.compareTo(absenceJustifiee2), is(-1));
    }

    @Test(expected = NullPointerException.class)
    public void getPartPatronale_WithEmptyAJ_ShouldReturn0() {
        AbsenceJustifiee absenceJustifiee = new AbsenceJustifiee();

        assertThat(absenceJustifiee.getPartPatronale(), is(Montant.valueOf(0)));
    }

    @Test
    public void getPartPatronale_WithMontantBrutOf1000AndBothTauxOf0_ShouldReturn0() {
        AbsenceJustifiee absenceJustifiee = new AbsenceJustifiee();
        absenceJustifiee.setMontantBrut(Montant.valueOf(1000));
        absenceJustifiee.setTauxAC(new Taux(0));
        absenceJustifiee.setTauxAVS(new Taux(0));

        assertThat(absenceJustifiee.getPartPatronale(), is(Montant.valueOf(0)));
    }

    @Test
    public void getPartPatronale_WithMontantBrutOf1000AndBothTauxOf5_ShouldReturn100() {
        AbsenceJustifiee absenceJustifiee = new AbsenceJustifiee();
        absenceJustifiee.setMontantBrut(Montant.valueOf(1000));
        absenceJustifiee.setTauxAC(new Taux(5));
        absenceJustifiee.setTauxAVS(new Taux(5));

        assertThat(absenceJustifiee.getPartPatronale(), is(Montant.valueOf(100)));
    }

    @Test(expected = NullPointerException.class)
    public void isQualificationEmploye_WithEmptyPoste_ShouldThrowIllegalStateException() {
        absenceJustifiee.isQualificationEmploye();
    }

    @Test
    public void isQualificationEmploye_WithPosteOfEmploye_ShouldBeTrue() {
        PosteTravail posteTravail = mock(PosteTravail.class);
        when(posteTravail.isQualificationEmploye()).thenReturn(true);
        absenceJustifiee.setPosteTravail(posteTravail);

        assertTrue(absenceJustifiee.isQualificationEmploye());
    }

    @Test(expected = NullPointerException.class)
    public void isQualificationOuvrier_WithEmptyPoste_ShouldThrowNullPointerException() {
        absenceJustifiee.isQualificationOuvrier();
    }

    @Test
    public void isQualificationOuvrier_WithPosteOfOuvrier_ShouldBeTrue() {
        PosteTravail posteTravail = mock(PosteTravail.class);
        when(posteTravail.isQualificationOuvrier()).thenReturn(true);
        absenceJustifiee.setPosteTravail(posteTravail);

        assertTrue(absenceJustifiee.isQualificationOuvrier());
    }
}
