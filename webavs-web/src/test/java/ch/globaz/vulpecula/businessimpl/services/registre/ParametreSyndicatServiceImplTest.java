package ch.globaz.vulpecula.businessimpl.services.registre;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.registre.ParametreSyndicatService;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.syndicat.ParametreSyndicat;
import ch.globaz.vulpecula.domain.repositories.syndicat.ParametreSyndicatRepository;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class ParametreSyndicatServiceImplTest {
    private ParametreSyndicatService parametreSyndicatService;
    private ParametreSyndicatRepository parametreSyndicatRepository;

    @Before
    public void setUp() {
        parametreSyndicatRepository = mock(ParametreSyndicatRepository.class);
        parametreSyndicatService = new ParametreSyndicatServiceImpl(parametreSyndicatRepository);
    }

    @Test
    public void create_GivenParametreSyndicatSansParametresExistants_ShouldBeOk()
            throws UnsatisfiedSpecificationException {
        ParametreSyndicat parametreSyndicat = createValidParametreSyndicat();
        parametreSyndicatService.create(parametreSyndicat);
    }

    @Test
    public void create_GivenParametreSyndicatAvecParametreNonChevauchant_ShouldBeOk()
            throws UnsatisfiedSpecificationException {
        ParametreSyndicat parametreSyndicat = createValidParametreSyndicat();
        when(parametreSyndicatRepository.findByIdSyndicat(anyString(), anyString())).thenReturn(
                Arrays.asList(createValidParametreSyndicat("01.01.2013", "31.01.2013")));
        parametreSyndicatService.create(parametreSyndicat);
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void create_GivenParametreSyndicatAvecParametreChevauchant_ShouldThrowUnsatisfiedSpecificationException()
            throws UnsatisfiedSpecificationException {
        ParametreSyndicat parametreSyndicat = createValidParametreSyndicat();
        when(parametreSyndicatRepository.findByIdSyndicat(anyString(), anyString())).thenReturn(
                Arrays.asList(createValidParametreSyndicat("01.01.2013", "31.01.2014")));
        parametreSyndicatService.create(parametreSyndicat);
    }

    private ParametreSyndicat createValidParametreSyndicat(String dateDebut, String dateFin) {
        ParametreSyndicat parametreSyndicat = new ParametreSyndicat();
        Administration caisseMetier = new Administration();
        caisseMetier.setId("1");
        Administration syndicat = new Administration();
        syndicat.setId("1");
        parametreSyndicat.setId("1");
        parametreSyndicat.setCaisseMetier(caisseMetier);
        parametreSyndicat.setDateDebut(new Date(dateDebut));
        parametreSyndicat.setDateFin(new Date(dateFin));
        parametreSyndicat.setSyndicat(syndicat);
        parametreSyndicat.setMontantParTravailleur(new Montant(10));
        parametreSyndicat.setPourcentage(new Taux(10));
        return parametreSyndicat;
    }

    private ParametreSyndicat createValidParametreSyndicat() {
        return createValidParametreSyndicat("01.01.2014", "31.01.2014");
    }
}
