package ch.globaz.vulpecula.businessimpl.services.decompte;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.business.services.decompte.DecompteService;
import ch.globaz.vulpecula.business.services.postetravail.PosteTravailService;
import ch.globaz.vulpecula.business.services.properties.PropertiesService;
import ch.globaz.vulpecula.business.services.taxationoffice.TaxationOfficeService;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.HistoriqueDecompte;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteRepository;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteSalaireRepository;
import ch.globaz.vulpecula.domain.repositories.decompte.HistoriqueDecompteRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.AdhesionCotisationPosteTravailRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.EmployeurRepository;

public class DecompteServiceImplTest {
    private DecompteService decompteService;

    private DecompteRepository decompteRepository;
    private HistoriqueDecompteRepository historiqueDecompteRepostory;
    private DecompteSalaireRepository decompteSalaireRepository;
    private AdhesionCotisationPosteTravailRepository adhesionCotisationPosteTravailRepository;
    private EmployeurRepository employeurRepository;
    private TaxationOfficeService taxationOfficeService;

    private PosteTravailService posteTravailService;
    private PropertiesService propertiesService;

    @Before
    public void setUp() {
        decompteRepository = mock(DecompteRepository.class);
        historiqueDecompteRepostory = mock(HistoriqueDecompteRepository.class);
        decompteSalaireRepository = mock(DecompteSalaireRepository.class);
        adhesionCotisationPosteTravailRepository = mock(AdhesionCotisationPosteTravailRepository.class);
        employeurRepository = mock(EmployeurRepository.class);

        posteTravailService = mock(PosteTravailService.class);
        propertiesService = mock(PropertiesService.class);
        taxationOfficeService = mock(TaxationOfficeService.class);

        decompteService = new DecompteServiceImpl(decompteRepository, historiqueDecompteRepostory,
                decompteSalaireRepository, adhesionCotisationPosteTravailRepository, employeurRepository,
                posteTravailService, propertiesService, taxationOfficeService);
    }

    @Test
    public void devalider_GivenADecompte_ShouldBeCorrect() {
        Decompte decompte = mock(Decompte.class);

        decompteService.devaliderDecompte(decompte);

        verify(decompte).setEtat(EtatDecompte.OUVERT);
        verify(decompteRepository).update(decompte);
        verify(historiqueDecompteRepostory).create(any(HistoriqueDecompte.class));
    }

    @Test
    public void findAdhesionsForDecompteSalaire_ShouldUsePeriodeFinDecompteSalaire() {
        DecompteSalaire decompteSalaire = mock(DecompteSalaire.class);
        decompteService.findAdhesionsForDecompteSalaire(decompteSalaire);

        verify(decompteSalaire).getPeriode();
    }

    @Test
    public void updateDecompte_Delete_DateReception_Should_Set_Etat_Ouvert_And_Delete_DateReception() {
        Date dateReception = new Date("01.01.2016");
        Decompte decompte = mock(Decompte.class);
        when(decompte.getDateReception()).thenReturn(dateReception);
        when(decompte.getEtat()).thenReturn(EtatDecompte.RECEPTIONNE);

        decompteService.reinitialiseDecompte(decompte);

        when(decompte.getDateReception()).thenCallRealMethod();
        verify(decompte).setEtat(EtatDecompte.OUVERT);
        verify(decompte).setDateReception(null);
        verify(decompteRepository).update(decompte);
    }

    @Test
    public void updateDecompte_Etat_Comptabilise_Delete_DateReception_Should_Do_Nothing() {
        Date dateReception = new Date("01.01.2016");
        Decompte decompte = mock(Decompte.class);
        when(decompte.getDateReception()).thenReturn(dateReception);
        when(decompte.getEtat()).thenReturn(EtatDecompte.COMPTABILISE);

        decompteService.reinitialiseDecompte(decompte);

        verify(decompte, never()).setEtat(EtatDecompte.GENERE);
        verify(decompte, never()).setDateReception(null);
        verify(decompteRepository, never()).update(decompte);
    }
}
