package ch.globaz.vulpecula.domain.models.registre;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Assert;
import org.junit.Test;

public class ConventionTest {
    @Test
    public void givenEmptyConventionWhenGetQualificationsThenReturnSize0() {
        Convention convention = new Convention();

        Assert.assertEquals(0, convention.getQualifications().size());
    }

    @Test
    public void compareTo_TwoEmptyConvention_ShouldBe0() {
        Convention convention = new Convention();
        Convention convention2 = new Convention();

        assertThat(convention.compareTo(convention2), is(0));
    }

    @Test
    public void compareTo_Convention1And5_ShouldBeMinus1() {
        Convention convention = new Convention();
        convention.setCode("1");
        Convention convention2 = new Convention();
        convention2.setCode("5");

        assertThat(convention.compareTo(convention2), lessThan(-1));
    }

}
