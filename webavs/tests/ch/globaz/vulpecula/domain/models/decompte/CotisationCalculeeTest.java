package ch.globaz.vulpecula.domain.models.decompte;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;

public class CotisationCalculeeTest {

    @Test
    public void hasSameAssuranceAndTaux_givenTwoCotisationsWithSameTauxAndSameAssurance_ShouldBeTrue() {
        CotisationCalculee cotisation1 = createCotisationCalculee("1", new Montant(100), new Taux(2.0));
        CotisationCalculee cotisation2 = createCotisationCalculee("1", new Montant(500), new Taux(2.0));

        assertTrue(cotisation1.hasSameAssuranceAndTaux(cotisation2));
    }

    @Test
    public void givenTwoCotisationsWithDifferentsTauxAndSameAssuranceWhenHasSameAssuranceAndTauxShouldReturnTrue() {
        CotisationCalculee cotisation1 = createCotisationCalculee("1", new Montant(100), new Taux(1.0));
        CotisationCalculee cotisation2 = createCotisationCalculee("1", new Montant(500), new Taux(2.0));

        assertFalse(cotisation1.hasSameAssuranceAndTaux(cotisation2));
    }

    @Test
    public void givenTwoCotisationsWithDifferentsAssuranceAndSameTauxWhenHasSameAssuranceAndTauxShouldReturnTrue() {
        CotisationCalculee cotisation1 = createCotisationCalculee("1", new Montant(100), new Taux(1.0));
        CotisationCalculee cotisation2 = createCotisationCalculee("2", new Montant(500), new Taux(1.0));

        assertFalse(cotisation1.hasSameAssuranceAndTaux(cotisation2));
    }

    private CotisationCalculee createCotisationCalculee(final String id, final Montant montant, final Taux taux) {
        Cotisation cotisation = mock(Cotisation.class);
        when(cotisation.getAssuranceId()).thenReturn(id);

        CotisationCalculee cotisationCalculee = new CotisationCalculee(cotisation, montant, taux,
                Annee.getCurrentYear());

        return cotisationCalculee;
    }
}
