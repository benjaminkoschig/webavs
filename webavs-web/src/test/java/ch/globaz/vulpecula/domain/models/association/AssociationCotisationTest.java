package ch.globaz.vulpecula.domain.models.association;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Periode;

public class AssociationCotisationTest {
    private AssociationCotisation associationCotisation;

    @Before
    public void setUp() {
        associationCotisation = new AssociationCotisation();
    }

    @Test(expected = IllegalStateException.class)
    public void isActive_GivenNoPeriode_ShouldThrowIllegalStateException() {
        associationCotisation.isActive(new Annee(2015));
    }

    @Test
    public void isActive_GivenPeriodeOf2010And2014_ShouldBeActiveIn2010() {
        setPeriode("01.01.2010", "01.01.2014");
        assertTrue(associationCotisation.isActive(new Annee(2010)));
    }

    @Test
    public void isActive_GivenPeriodeOf2009And2014_ShouldBeActiveIn2010() {
        setPeriode("01.01.2009", "01.01.2014");
        assertTrue(associationCotisation.isActive(new Annee(2010)));
    }

    @Test
    public void isActive_GivenPeriodeOf2011And2014_ShouldBeInactiveIn2010() {
        setPeriode("01.01.2011", "01.01.2014");
        assertFalse(associationCotisation.isActive(new Annee(2010)));
    }

    @Test
    public void isActive_GivenPeriodeOf2009WithoutEnd_ShouldBeInactiveIn2010() {
        setPeriode("01.01.2009", null);
        assertTrue(associationCotisation.isActive(new Annee(2010)));
    }

    public void setPeriode(String periodeDebut, String periodeFin) {
        associationCotisation.setPeriode(new Periode(periodeDebut, periodeFin));
    }
}
