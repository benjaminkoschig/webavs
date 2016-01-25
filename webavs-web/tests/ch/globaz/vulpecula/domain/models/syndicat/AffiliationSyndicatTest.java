package ch.globaz.vulpecula.domain.models.syndicat;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.common.Montant;

public class AffiliationSyndicatTest {
    private AffiliationSyndicat affiliationSyndicat;

    @Before
    public void setUp() {
        affiliationSyndicat = new AffiliationSyndicat();
    }

    @Test(expected = IllegalStateException.class)
    public void cumulSalaire_GivenAffiliationWithCumulNotLoaded_ShouldThrowException() {
        affiliationSyndicat.getCumulSalaires();
    }

    @Test
    public void cumulSalaire_GivenAffiliationWithCumul_ShouldReturnCumul() {
        affiliationSyndicat.setCumulSalaires(new Montant(1000));
        assertEquals(new Montant(1000), affiliationSyndicat.getCumulSalaires());
    }
}
