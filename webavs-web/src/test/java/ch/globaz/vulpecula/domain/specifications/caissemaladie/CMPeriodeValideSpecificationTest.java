package ch.globaz.vulpecula.domain.specifications.caissemaladie;

import org.junit.Before;
import org.junit.Test;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;
import ch.globaz.vulpecula.domain.models.common.Date;

public class CMPeriodeValideSpecificationTest {
    private CMPeriodeValideSpecification periodeValideSpecification;
    private AffiliationCaisseMaladie affiliationCaisseMaladie;

    @Before
    public void setUp() {
        periodeValideSpecification = new CMPeriodeValideSpecification();
        affiliationCaisseMaladie = new AffiliationCaisseMaladie();
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void isSatisfiedBy_GivenNoPeriode_ShouldThrowException() throws UnsatisfiedSpecificationException {
        periodeValideSpecification.isSatisfiedBy(affiliationCaisseMaladie);
    }

    @Test
    public void isSatisfiedBy_GivenValidPeriode_ShouldBeOk() throws UnsatisfiedSpecificationException {
        affiliationCaisseMaladie.setMoisDebut(new Date("01.2014"));
        affiliationCaisseMaladie.setMoisFin(new Date("04.2014"));
        periodeValideSpecification.isSatisfiedBy(affiliationCaisseMaladie);
    }
}
