package ch.globaz.vulpecula.domain.specifications.postetravail;

import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.postetravail.Occupation;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

public class PosteTravailOccupationsSansDoublonsSpecificationTest {
    private PosteTravailOccupationsSansDoublonsSpecification posteTravailOccupationsSansDoublonsSpecification;

    @Before
    public void setUp() {
        posteTravailOccupationsSansDoublonsSpecification = new PosteTravailOccupationsSansDoublonsSpecification();
    }

    @Test
    public void isValid_Given2OccupationsWithDifferentsDates_ShouldBeOk() {
        Occupation occupation = new Occupation();
        occupation.setDateValidite(new Date("01.10.1995"));
        occupation.setTaux(new Taux(70));
        Occupation occupation2 = new Occupation();
        occupation2.setDateValidite(new Date("01.09.2006"));
        occupation2.setTaux(new Taux(65));

        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setOccupations(Arrays.asList(occupation, occupation2));
        posteTravailOccupationsSansDoublonsSpecification.isValid(posteTravail);
    }
}
