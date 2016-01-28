package ch.globaz.vulpecula.domain.specifications.congepaye;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;

public class CPPeriodeAnnuelleRequiseTest {
    private CPPeriodeAnnuelleRequise cpPeriodeAnnuelleRequise = new CPPeriodeAnnuelleRequise();
    private CongePaye congePaye;

    @Before
    public void setUp() {
        congePaye = new CongePaye();
    }

    @Test
    public void satisfiedBy_GivenEmptyCP_ShouldBeFalseAnd2Messages() {
        try {
            cpPeriodeAnnuelleRequise.isSatisfiedBy(congePaye);
            fail();
        } catch (UnsatisfiedSpecificationException ex) {
        }

        assertThat(cpPeriodeAnnuelleRequise.getNbMessages(), is(2));
    }

    @Test
    public void satisfiedBy_GivenCPWithInvalidPeriodeAnnuelle_ShouldBeFalseAnd1Message() {
        congePaye.setAnneeDebut(new Annee(2014));
        congePaye.setAnneeFin(new Annee(2013));

        try {
            cpPeriodeAnnuelleRequise.isSatisfiedBy(congePaye);
            fail();
        } catch (UnsatisfiedSpecificationException ex) {

        }

        assertThat(cpPeriodeAnnuelleRequise.getNbMessages(), is(1));
    }

    @Test
    public void satisfiedBy_GivenCPWithPeriodeAndSalaireNonDeclaree_ShouldBeTrue()
            throws UnsatisfiedSpecificationException {
        congePaye.setAnneeDebut(new Annee(2013));
        congePaye.setAnneeFin(new Annee(2014));

        boolean valid = cpPeriodeAnnuelleRequise.isSatisfiedBy(congePaye);

        assertThat(valid, is(true));
        assertThat(cpPeriodeAnnuelleRequise.getNbMessages(), is(0));
    }

}
