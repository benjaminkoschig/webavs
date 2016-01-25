package ch.globaz.vulpecula.domain.specifications.postetravail;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.postetravail.Occupation;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

public class PosteTravailTauxOccupationInPeriodeActiviteTest {
    private PosteTravailTauxOccupationInPeriodeActivite posteTravailTauxOccupationInPeriodeActivite;

    @Before
    public void setUp() {
        posteTravailTauxOccupationInPeriodeActivite = new PosteTravailTauxOccupationInPeriodeActivite();
    }

    @Test
    public void isSatisfiedBy_GivenEmptyPoste_ShouldBeTrue() throws UnsatisfiedSpecificationException {
        PosteTravail posteTravail = new PosteTravail();
        assertTrue(posteTravailTauxOccupationInPeriodeActivite.isSatisfiedBy(posteTravail));
    }

    @Test
    public void isSatisfiedBy_GivenPosteWithAffiliationFrom01_01_2013ToInfiniteAndOccupationOf01_01_2014()
            throws UnsatisfiedSpecificationException {
        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setPeriodeActivite(new Periode("01.01.2013", null));

        Occupation occupation = new Occupation();
        occupation.setDateValidite(new Date("01.01.2014"));
        posteTravail.addTauxOccupation(occupation);

        assertTrue(posteTravailTauxOccupationInPeriodeActivite.isSatisfiedBy(posteTravail));
    }

    @Test
    public void isSatisfiedBy_GivenPosteWithAffiliationFrom01_01_2013ToInfiniteAndOccupationOf01_01_2012_ShouldThrowException() {
        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setPeriodeActivite(new Periode("01.01.2013", null));

        Occupation occupation = new Occupation();
        occupation.setDateValidite(new Date("01.01.2012"));
        posteTravail.addTauxOccupation(occupation);

        try {
            posteTravailTauxOccupationInPeriodeActivite.isSatisfiedBy(posteTravail);
            fail();
        } catch (UnsatisfiedSpecificationException ex) {

        }
    }

    @Test
    public void isSatisfiedBy_GivenPosteWithAffiliationFrom01_01_2013To01_01_2014AndOccupationOf01_01_2015_ShouldBeFalse() {
        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setPeriodeActivite(new Periode("01.01.2013", "01.01.2014"));

        Occupation occupation = new Occupation();
        occupation.setDateValidite(new Date("01.01.2015"));
        posteTravail.addTauxOccupation(occupation);

        try {
            posteTravailTauxOccupationInPeriodeActivite.isSatisfiedBy(posteTravail);
            fail();
        } catch (UnsatisfiedSpecificationException ex) {

        }
    }
}
