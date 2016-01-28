package ch.globaz.vulpecula.domain.models.caissemaladie;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class AffiliationCaisseMaladieTest {
    private AffiliationCaisseMaladie affiliationCaisseMaladie = new AffiliationCaisseMaladie();

    @Before
    public void setUp() {
        affiliationCaisseMaladie = new AffiliationCaisseMaladie();
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void validate_GivenEmptyAffiliation_ShouldThrowException() throws UnsatisfiedSpecificationException {
        affiliationCaisseMaladie.validate(new ArrayList<AffiliationCaisseMaladie>());
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void validate_GivenAffiliationWithCaisseMaladie_ShouldThrowException()
            throws UnsatisfiedSpecificationException {
        affiliationCaisseMaladie.setCaisseMaladie(new Administration());
        affiliationCaisseMaladie.validate(new ArrayList<AffiliationCaisseMaladie>());
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void validate_GivenAffiliationWithPeriode_ShouldThrowException() throws UnsatisfiedSpecificationException {
        affiliationCaisseMaladie.setMoisDebut(new Date("01.2014"));
        affiliationCaisseMaladie.setMoisFin(new Date("02.2014"));
        affiliationCaisseMaladie.validate(new ArrayList<AffiliationCaisseMaladie>());
    }

    @Test
    public void validate_GivenAffiliationWithPeriodeAndCaisseMaladie_ShouldBeOk()
            throws UnsatisfiedSpecificationException {
        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setPeriodeActivite(new Periode("20.01.2014", "16.02.2014"));
        Administration administration = new Administration();
        administration.setId("1");
        affiliationCaisseMaladie.setPosteTravail(posteTravail);
        affiliationCaisseMaladie.setCaisseMaladie(administration);
        affiliationCaisseMaladie.setMoisDebut(new Date("01.2014"));
        affiliationCaisseMaladie.setMoisFin(new Date("02.2014"));
        affiliationCaisseMaladie.validate(new ArrayList<AffiliationCaisseMaladie>());
    }

    @Test
    public void isSupprimable_GivenEmptyAffiliation_ShouldBeTrue() {
        assertTrue(affiliationCaisseMaladie.isSupprimable());
    }

    @Test
    public void isSupprimable_GivenAffiliationWithDateAnnonceDebut_ShouldBeFalse() {
        affiliationCaisseMaladie.setDateAnnonceDebut(new Date("01.01.2014"));
        assertFalse(affiliationCaisseMaladie.isSupprimable());
    }

    @Test
    public void isSupprimable_GivenAffiliationWithDateAnnonceFin_ShouldBeFalse() {
        affiliationCaisseMaladie.setDateAnnonceFin(new Date("01.01.2014"));
        assertFalse(affiliationCaisseMaladie.isSupprimable());
    }

    @Test
    public void isSupprimable_GivenAffiliationWithDateAnnonceFinAndDebut_ShouldBeFalse() {
        affiliationCaisseMaladie.setDateAnnonceFin(new Date("01.01.2014"));
        affiliationCaisseMaladie.setDateAnnonceDebut(new Date("01.01.2014"));
        assertFalse(affiliationCaisseMaladie.isSupprimable());
    }

    @Test
    public void isModifiable_GivenEmptyAffiliation_ShouldBeTrue() {
        assertTrue(affiliationCaisseMaladie.isModifiable());
    }

    @Test
    public void isModifiable_GivenAffiliationWithDateAnnonceDebut_ShouldBeTrue() {
        affiliationCaisseMaladie.setDateAnnonceDebut(new Date("01.01.2014"));
        assertTrue(affiliationCaisseMaladie.isModifiable());
    }

    @Test
    public void isModifiable_GivenAffiliationWithDateAnnonceFin_ShouldBeTrue() {
        affiliationCaisseMaladie.setDateAnnonceFin(new Date("01.01.2014"));
        assertTrue(affiliationCaisseMaladie.isModifiable());
    }

    @Test
    public void isModifiable_GivenAffiliationWithDateAnnonceFinAndDebut_ShouldBeFalse() {
        affiliationCaisseMaladie.setDateAnnonceFin(new Date("01.01.2014"));
        affiliationCaisseMaladie.setDateAnnonceDebut(new Date("01.01.2014"));
        assertFalse(affiliationCaisseMaladie.isModifiable());
    }
}
