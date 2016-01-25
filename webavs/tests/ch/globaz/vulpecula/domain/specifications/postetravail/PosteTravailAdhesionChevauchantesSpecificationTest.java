package ch.globaz.vulpecula.domain.specifications.postetravail;

import static org.junit.Assert.*;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;

public class PosteTravailAdhesionChevauchantesSpecificationTest {
    private PosteTravailAdhesionChevauchantesSpecification adhesionChevauchantesSpecification;

    @Before
    public void setUp() {
        adhesionChevauchantesSpecification = new PosteTravailAdhesionChevauchantesSpecification();
    }

    @Test
    public void isSatisfiedBy_GivenEmptyPosteTravail_ShouldBeTrue() throws UnsatisfiedSpecificationException {
        PosteTravail posteTravail = new PosteTravail();
        assertTrue(adhesionChevauchantesSpecification.isSatisfiedBy(posteTravail));
    }

    @Test
    public void isSatisfiedBy_GivenPosteTravailWithOneCotisation_ShouldBeTrue()
            throws UnsatisfiedSpecificationException {
        PosteTravail posteTravail = new PosteTravail();
        AdhesionCotisationPosteTravail adh = createCotisation("1", new Date("01.01.2014"), null);
        posteTravail.setAdhesionsCotisations(Arrays.asList(adh));
        assertTrue(adhesionChevauchantesSpecification.isSatisfiedBy(posteTravail));
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void isSatisfiedBy_GivenPosteTravailWithTwoCotisationsChevauchantes_ShouldBeThrowException()
            throws UnsatisfiedSpecificationException {
        PosteTravail posteTravail = new PosteTravail();
        AdhesionCotisationPosteTravail adh = createCotisation("1", new Date("01.01.2014"), null);
        AdhesionCotisationPosteTravail adh2 = createCotisation("1", new Date("01.01.2015"), null);
        posteTravail.setAdhesionsCotisations(Arrays.asList(adh, adh2));
        assertTrue(adhesionChevauchantesSpecification.isSatisfiedBy(posteTravail));
    }

    @Test
    public void isSatisfiedBy_GivenPosteTravailWithTwoCotisationsNonChevauchantes_ShouldBeTrue()
            throws UnsatisfiedSpecificationException {
        PosteTravail posteTravail = new PosteTravail();
        AdhesionCotisationPosteTravail adh = createCotisation("1", new Date("01.01.2014"), new Date("31.01.2014"));
        AdhesionCotisationPosteTravail adh2 = createCotisation("1", new Date("01.01.2015"), null);
        posteTravail.setAdhesionsCotisations(Arrays.asList(adh, adh2));
        assertTrue(adhesionChevauchantesSpecification.isSatisfiedBy(posteTravail));
    }

    @Test
    public void isSatisfiedBy_GivenPosteTravailWithTwoCotisationsDifferentesChevauchantes_ShouldBeTrue()
            throws UnsatisfiedSpecificationException {
        PosteTravail posteTravail = new PosteTravail();
        AdhesionCotisationPosteTravail adh = createCotisation("1", new Date("01.01.2014"), null);
        AdhesionCotisationPosteTravail adh2 = createCotisation("2", new Date("01.01.2014"), null);
        posteTravail.setAdhesionsCotisations(Arrays.asList(adh, adh2));
        assertTrue(adhesionChevauchantesSpecification.isSatisfiedBy(posteTravail));
    }

    private AdhesionCotisationPosteTravail createCotisation(String idCotisation, Date dateDebut, Date dateFin) {
        Cotisation cotisation = new Cotisation();
        cotisation.setId(idCotisation);
        AdhesionCotisationPosteTravail adh = new AdhesionCotisationPosteTravail();
        adh.setPeriode(new Periode(dateDebut, dateFin));
        adh.setCotisation(cotisation);
        return adh;
    }
}
