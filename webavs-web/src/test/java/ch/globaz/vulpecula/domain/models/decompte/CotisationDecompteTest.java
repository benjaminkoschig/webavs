package ch.globaz.vulpecula.domain.models.decompte;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;

public class CotisationDecompteTest {

    private CotisationDecompte cotisationDecompte;

    @Before
    public void setUp() {
        cotisationDecompte = new CotisationDecompte();
    }

    @Test
    public void getCotisationAsValue_GivenCotisationDecompteWithSalaireTotalOf1500AndTauxOf1_ShouldBe15() {
        Montant montant = new Montant(1500);
        cotisationDecompte.setTaux(new Taux(1));

        assertEquals("15.00", cotisationDecompte.getCotisationAsValue(montant));
    }

    @Test
    public void getCotisationAsValue_GivenCotisationDecompteWithSalaireTotalOf1433AndTauxOf1_ShouldBe14_35() {
        Montant montant = new Montant(1433);
        cotisationDecompte.setTaux(new Taux(1));

        assertEquals("14.35", cotisationDecompte.getCotisationAsValue(montant));
    }

    @Test
    public void isAssuranceWithReductionRentier_GivenCotisationDecompteWithAssuranceAVS_ShouldBeTrue() {
        setCotisation(TypeAssurance.COTISATION_AVS_AI);
        assertThat(cotisationDecompte.isAssuranceWithReductionRentier(), is(true));
    }

    @Test
    public void isAssuranceWithReductionRentier_GivenCotisationDecompteWithAssuranceAF_ShouldBeTrue() {
        setCotisation(TypeAssurance.COTISATION_AF);
        assertThat(cotisationDecompte.isAssuranceWithReductionRentier(), is(true));
    }

    @Test
    public void isAssuranceWithReductionRentier_GivenCotisationDecompteWithAssuranceAC2_ShouldBeFalse() {
        setCotisation(TypeAssurance.COTISATION_AC2);
        assertThat(cotisationDecompte.isAssuranceWithReductionRentier(), is(false));
    }

    private void setCotisation(TypeAssurance typeAssurance) {
        Cotisation cotisation = mock(Cotisation.class);
        when(cotisation.getTypeAssurance()).thenReturn(typeAssurance);
        cotisationDecompte.setCotisation(cotisation);
    }
}
