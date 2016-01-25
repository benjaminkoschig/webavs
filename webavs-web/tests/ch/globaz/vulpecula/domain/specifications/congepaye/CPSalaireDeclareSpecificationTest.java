package ch.globaz.vulpecula.domain.specifications.congepaye;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;

public class CPSalaireDeclareSpecificationTest {
    private CPSalaireDeclareSpecification cpSalaireDeclareSpecification = new CPSalaireDeclareSpecification();
    private CongePaye congePaye;

    @Before
    public void setUp() throws Exception {
        congePaye = new CongePaye();
    }

    @Test
    public void satisfiedBy_EmptyCongePaye_ShouldBeTrue() throws UnsatisfiedSpecificationException {
        boolean isValid = cpSalaireDeclareSpecification.isSatisfiedBy(congePaye);

        assertThat(isValid, is(true));
    }

    @Test
    public void satisfiedBy_CongePayeWithSalaireDeclareWithoutDate_ShouldBeFalseWith1Message()
            throws UnsatisfiedSpecificationException {
        congePaye.setSalaireNonDeclare(new Montant(1000));

        try {
            cpSalaireDeclareSpecification.isSatisfiedBy(congePaye);
            fail();
        } catch (UnsatisfiedSpecificationException ex) {

        }
        assertThat(cpSalaireDeclareSpecification.getNbMessages(), is(1));
    }

    @Test
    public void satisfiedBy_CongePayeWithDateSalaireDeclaireWithoutMontant_ShouldBeFalseWith1Message() {
        congePaye.setDateSalaireNonDeclare(new Date("01.01.2014"));

        try {
            cpSalaireDeclareSpecification.isSatisfiedBy(congePaye);
            fail();
        } catch (UnsatisfiedSpecificationException ex) {

        }
        assertThat(cpSalaireDeclareSpecification.getNbMessages(), is(1));
    }

    @Test
    public void satisfiedBy_CongePayeWithDateSalaireAndMontant_ShouldBeOk() throws UnsatisfiedSpecificationException {
        congePaye.setDateSalaireNonDeclare(new Date("01.01.2014"));
        congePaye.setSalaireNonDeclare(new Montant(1000));

        boolean isValid = cpSalaireDeclareSpecification.isSatisfiedBy(congePaye);

        assertThat(isValid, is(true));
    }
}
