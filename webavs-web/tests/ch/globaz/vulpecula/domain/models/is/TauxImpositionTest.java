package ch.globaz.vulpecula.domain.models.is;

import org.junit.Before;
import org.junit.Test;
import ch.globaz.specifications.UnsatisfiedSpecificationException;

public class TauxImpositionTest {
    private TauxImposition tauxImposition;

    @Before
    public void setUp() {
        tauxImposition = new TauxImposition();
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void validate_WithoutCantonImposition_ShouldThrowException() throws UnsatisfiedSpecificationException {
        tauxImposition.validate();
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void validate_WithoutEmptyCantonImposition_ShouldThrowException() throws UnsatisfiedSpecificationException {
        tauxImposition.setCanton("");
        tauxImposition.validate();
    }

    @Test
    public void validate_WithCantonImposition_ShouldBeOk() throws UnsatisfiedSpecificationException {
        tauxImposition.setCanton("1");
        tauxImposition.validate();
    }
}
