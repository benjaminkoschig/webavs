package ch.globaz.vulpecula.domain.models.taxationoffice;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.common.Montant;

public class LigneTaxationTest {
    LigneTaxation ligneTaxation;

    @Before
    public void setUp() {
        ligneTaxation = new LigneTaxation();
    }

    @Test
    public void calculerMontant_GivenEmptyLigneTaxation_ShouldBe0() {
        assertEquals("0.00", ligneTaxation.getMontantValue());
    }

    @Test
    public void calculerMontant_GivenTauxOf50AndMontantOf1000_ShouldBe500() {
        ligneTaxation.setMontant(new Montant(1000));
        assertEquals("1000.00", ligneTaxation.getMontantValue());
    }
}
