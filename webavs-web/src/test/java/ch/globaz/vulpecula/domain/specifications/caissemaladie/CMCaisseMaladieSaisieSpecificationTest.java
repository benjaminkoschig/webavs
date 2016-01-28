package ch.globaz.vulpecula.domain.specifications.caissemaladie;

import org.junit.Before;
import org.junit.Test;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class CMCaisseMaladieSaisieSpecificationTest {
    private CMCaisseMaladieSaisieSpecification caisseMaladieSaisieSpecification;
    private AffiliationCaisseMaladie affiliationCaisseMaladie;

    @Before
    public void setUp() {
        caisseMaladieSaisieSpecification = new CMCaisseMaladieSaisieSpecification();
        affiliationCaisseMaladie = new AffiliationCaisseMaladie();
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void isSatisfedBy_GivenAffiliationSansCaisseMaladie_ShouldThrowException()
            throws UnsatisfiedSpecificationException {
        caisseMaladieSaisieSpecification.isSatisfiedBy(affiliationCaisseMaladie);
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void isSatisfedBy_GivenAffiliationWithCaisseMaladie_ShouldThrowException()
            throws UnsatisfiedSpecificationException {
        affiliationCaisseMaladie.setCaisseMaladie(new Administration());
        caisseMaladieSaisieSpecification.isSatisfiedBy(affiliationCaisseMaladie);
    }

    @Test
    public void isSatisfedBy_GivenAffiliationWithCaisseMaladie_ShouldBeOK() throws UnsatisfiedSpecificationException {
        Administration administration = new Administration();
        administration.setId("12");
        affiliationCaisseMaladie.setCaisseMaladie(administration);
        caisseMaladieSaisieSpecification.isSatisfiedBy(affiliationCaisseMaladie);
    }
}
