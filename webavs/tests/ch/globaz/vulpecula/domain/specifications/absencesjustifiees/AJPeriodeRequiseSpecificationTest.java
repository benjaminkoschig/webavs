package ch.globaz.vulpecula.domain.specifications.absencesjustifiees;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.common.Periode;

public class AJPeriodeRequiseSpecificationTest {
    private AJPeriodeRequiseSpecification specification;
    private AbsenceJustifiee absenceJustifiee;

    @Before
    public void setUp() {
        specification = new AJPeriodeRequiseSpecification();
        absenceJustifiee = new AbsenceJustifiee();
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void isSatisfiedBy_GivenEmptyAbsenceJustifiee_ShouldBeFalse() throws UnsatisfiedSpecificationException {
        specification.isSatisfiedBy(absenceJustifiee);
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void isSatisfiedBy_GivenAbsenceJustifieeWithPeriodeFinNull_ShouldBeFalse()
            throws UnsatisfiedSpecificationException {
        absenceJustifiee.setPeriode(new Periode("01.01.2014", null));

        specification.isSatisfiedBy(absenceJustifiee);
    }

    @Test
    public void isSatisfiedBy_GivenAbsenceJustifieeWithPeriodeFinNullChainee_ShouldBeFalse()
            throws UnsatisfiedSpecificationException {
        absenceJustifiee.setPeriode(new Periode("01.01.2014", null));

        try {
            specification.and(specification).isSatisfiedBy(absenceJustifiee);
            fail();
        } catch (UnsatisfiedSpecificationException ex) {

        }
        assertEquals(specification.hasMessages(), true);
        assertEquals(specification.getNbMessages(), 2);
    }

    @Test
    public void isSatisfiedBy_GivenRightAbsenceJustifiee_ShouldBeOk() throws UnsatisfiedSpecificationException {
        absenceJustifiee.setPeriode(new Periode("01.01.2014", "01.07.2014"));

        assertTrue(specification.isSatisfiedBy(absenceJustifiee));
        assertEquals(specification.hasMessages(), false);
    }
}
