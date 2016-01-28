package ch.globaz.vulpecula.domain.specifications.decompte;

import org.junit.Before;
import org.junit.Test;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;

public class DecompteMotifProlongationRequisSpecificationTest {
    private DecompteMotifProlongationRequisSpecification decompteMotifProlongationRequisSpecification;

    @Before
    public void setUp() {
        Decompte decompte = new Decompte();
        decompteMotifProlongationRequisSpecification = new DecompteMotifProlongationRequisSpecification(decompte);
    }

    @Test
    public void isSatisfiedBy_GivenNoDateRappel_ShouldBeOk() throws UnsatisfiedSpecificationException {
        Decompte decompte = new Decompte();
        decompteMotifProlongationRequisSpecification.isSatisfiedBy(decompte);
    }
}
