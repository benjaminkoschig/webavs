package ch.globaz.vulpecula.domain.specifications.decompte;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;

public class DecompteSalaireInPeriodeActivitePosteTest {
    private DecompteSalaireInPeriodeActivitePoste decompteSalaireInPeriodeActivitePoste;
    private DecompteSalaire decompteSalaire;

    @Before
    public void setUp() {
        decompteSalaireInPeriodeActivitePoste = new DecompteSalaireInPeriodeActivitePoste();
        decompteSalaire = mock(DecompteSalaire.class);
    }

    @Test(expected = NullPointerException.class)
    public void isValid_GivenEmptyDecompteSalaire_ShouldReturnNullPointerException() {
        decompteSalaireInPeriodeActivitePoste.isValid(decompteSalaire);
    }

    @Test
    public void isValid_GivenDecompteSalaireInsidePeriodePoste_ShouldBeTrue() throws UnsatisfiedSpecificationException {
        when(decompteSalaire.getPeriodeActivitePoste()).thenReturn(new Periode("01.01.2014", "31.12.2014"));
        when(decompteSalaire.getPeriode()).thenReturn(new Periode("01.01.2014", "31.01.2014"));
        decompteSalaireInPeriodeActivitePoste.isValid(decompteSalaire);
        assertTrue(decompteSalaireInPeriodeActivitePoste.isValid(decompteSalaire));
    }

    @Test
    public void isValid_GivenDecompteSalaireOutsidePeriodePoste_ShouldBeFalse()
            throws UnsatisfiedSpecificationException {
        when(decompteSalaire.getPeriodeActivitePoste()).thenReturn(new Periode("01.01.2011", "31.12.2013"));
        when(decompteSalaire.getPeriode()).thenReturn(new Periode("01.01.2014", "31.01.2014"));
        assertFalse(decompteSalaireInPeriodeActivitePoste.isValid(decompteSalaire));
    }

    @Test
    public void isValid_GivenDecompteSalaireSameMonthAndYearPeriodePoste_ShouldBeTrue()
            throws UnsatisfiedSpecificationException {
        when(decompteSalaire.getPeriodeActivitePoste()).thenReturn(new Periode("15.01.2011", "31.01.2011"));
        when(decompteSalaire.getPeriode()).thenReturn(new Periode("01.01.2011", "31.01.2011"));
        assertTrue(decompteSalaireInPeriodeActivitePoste.isValid(decompteSalaire));
    }

    @Test
    public void isValid_GivenDecompteSalaireOutsideWithSameYearDifferentMonthPeriodePoste_ShouldBeFalse()
            throws UnsatisfiedSpecificationException {
        when(decompteSalaire.getPeriodeActivitePoste()).thenReturn(new Periode("15.01.2011", "31.01.2011"));
        when(decompteSalaire.getPeriode()).thenReturn(new Periode("01.02.2011", "28.02.2011"));
        assertFalse(decompteSalaireInPeriodeActivitePoste.isValid(decompteSalaire));
    }

    @Test
    public void isValid_GivenDecompteSalaire_ShouldBeTrue() {
        when(decompteSalaire.getPeriodeActivitePoste()).thenReturn(new Periode("01.01.1975", "15.05.2014"));
        when(decompteSalaire.getPeriode()).thenReturn(new Periode("01.05.2014", "31.05.2014"));
        assertTrue(decompteSalaireInPeriodeActivitePoste.isValid(decompteSalaire));
    }
}
