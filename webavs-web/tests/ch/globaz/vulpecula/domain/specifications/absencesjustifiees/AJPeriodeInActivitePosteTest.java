package ch.globaz.vulpecula.domain.specifications.absencesjustifiees;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.common.Periode;

public class AJPeriodeInActivitePosteTest {
    private AJPeriodeInActivitePoste ajPeriodeInActivitePoste;

    @Before
    public void setUp() {
        ajPeriodeInActivitePoste = new AJPeriodeInActivitePoste();
    }

    @Test
    public void isSatisfiedBy_GivenAPeriodeActiviteThatContainsPeriodeAJ_ShouldBeTrue()
            throws UnsatisfiedSpecificationException {
        AbsenceJustifiee absenceJustifiee = mock(AbsenceJustifiee.class);
        when(absenceJustifiee.getPeriode()).thenReturn(new Periode("01.01.2010", "31.01.2010"));
        when(absenceJustifiee.getPeriodeActivitePoste()).thenReturn(new Periode("01.01.2010", "31.12.2010"));
        assertTrue(ajPeriodeInActivitePoste.isSatisfiedBy(absenceJustifiee));
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void isSatisfiedBy_GivenAPeriodeActiviteThatNotContainsPeriodeAJ_ShouldBeFalse()
            throws UnsatisfiedSpecificationException {
        AbsenceJustifiee absenceJustifiee = mock(AbsenceJustifiee.class);
        when(absenceJustifiee.getPeriode()).thenReturn(new Periode("01.01.2011", "31.01.2011"));
        when(absenceJustifiee.getPeriodeActivitePoste()).thenReturn(new Periode("01.01.2010", "31.12.2010"));
        assertFalse(ajPeriodeInActivitePoste.isSatisfiedBy(absenceJustifiee));
    }
}
