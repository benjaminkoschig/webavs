package ch.globaz.vulpecula.businessimpl.services.decompte;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.vulpecula.business.services.properties.PropertiesService;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.PeriodeMensuelle;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.CotisationDecompte;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.postetravail.Occupation;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteRepository;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteSalaireRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.PosteTravailRepository;
import ch.globaz.vulpecula.external.models.affiliation.Assurance;

public class DecompteSalaireServiceImplTest {
    private DecompteSalaireServiceImpl decompteSalaireService;
    private DecompteSalaireRepository decompteSalaireRepository;
    private DecompteRepository decompteRepository;
    private PosteTravailRepository posteTravailRepository;
    private PropertiesService propertiesService;
    private DecompteSalaire decompteSalaire;
    private CotisationDecompte cotisationDecompteAC;
    private CotisationDecompte cotisationDecompteAC2;

    @Before
    public void setUp() {
        decompteSalaireRepository = mock(DecompteSalaireRepository.class);
        decompteRepository = mock(DecompteRepository.class);
        posteTravailRepository = mock(PosteTravailRepository.class);
        propertiesService = mock(PropertiesService.class);
        when(propertiesService.getAnneeProduction()).thenReturn(new Annee(2000));
        decompteSalaireService = spy(new DecompteSalaireServiceImpl(decompteSalaireRepository, decompteRepository,
                posteTravailRepository, propertiesService));
    }

    @Test
    public void handleAC_GivenDecompteSalaireWithoutCotisation_ShouldDoNothing() {
        init();
        DecompteSalaire decompteSalaire = mock(DecompteSalaire.class);
        when(decompteSalaire.getAnnee()).thenReturn(new Annee(2014));
        decompteSalaireService.handleAC(decompteSalaire);
    }

    @Test
    public void handleAC_GivenDecompteSalaireWithControleAC2_CasStandard_ShouldAddACButNotAC2() {
        init();
        when(decompteSalaire.getSalaireTotal()).thenReturn(new Montant(1500));
        when(decompteSalaire.getPeriodeFinDecompte()).thenReturn(new Date("01.01.2014"));
        Decompte decompte = new Decompte();
        decompte.setType(TypeDecompte.COMPLEMENTAIRE);
        when(decompteSalaire.getDecompte()).thenReturn(decompte);

        decompteSalaireService.handleAC(decompteSalaire);

        assertEquals(cotisationDecompteAC.getMasse(), new Montant(1500));
        assertEquals(cotisationDecompteAC2.getMasse(), new Montant(0));
    }

    @Test
    public void handleAC_GivenDecompteSalaireOf12000WithControleAC2_ShouldAddAC10500ACAnd1500AC2() {
        init();
        when(decompteSalaire.getSalaireTotal()).thenReturn(new Montant(12000));
        when(decompteSalaire.getPeriodeFinDecompte()).thenReturn(new Date("01.01.2014"));
        Decompte decompte = new Decompte();
        decompte.setType(TypeDecompte.COMPLEMENTAIRE);
        when(decompteSalaire.getDecompte()).thenReturn(decompte);

        decompteSalaireService.handleAC(decompteSalaire);

        assertEquals(cotisationDecompteAC.getMasse(), new Montant(10500));
        assertEquals(cotisationDecompteAC2.getMasse(), new Montant(1500));
    }

    @Test
    @Ignore("Suite à la monter en version de mockito ce test ne passe plus")
    public void handleAC_GivenDecompteSalaireOf12000WithControleAC2WithAlready11000AC_ShouldRemove500AC_AndAdd12500AC2() {
        init();
        doReturn(new Montant(11000)).when(decompteSalaireService).findMasseACFor(any(String.class), any(Annee.class));
        when(decompteSalaire.getSalaireTotal()).thenReturn(new Montant(12000));
        when(decompteSalaire.getPeriodeFinDecompte()).thenReturn(new Date("01.01.2014"));
        Decompte decompte = new Decompte();
        decompte.setType(TypeDecompte.COMPLEMENTAIRE);
        when(decompteSalaire.getDecompte()).thenReturn(decompte);

        decompteSalaireService.handleAC(decompteSalaire);

        assertEquals(cotisationDecompteAC.getMasse(), new Montant(-500));
        assertEquals(cotisationDecompteAC2.getMasse(), new Montant(12500));
    }

    @Test
    @Ignore("Suite à la monter en version de mockito ce test ne passe plus")
    public void handleAC_GivenDecompteSalaireWithControleAC2Of2000WithAlready20000AC_ShouldAdd1000ACAnd1000AC2() {
        init();
        doReturn(new Montant(20000)).when(decompteSalaireService).findMasseACFor(any(String.class), any(Annee.class));
        when(decompteSalaire.getSalaireTotal()).thenReturn(new Montant(2000));
        when(decompteSalaire.getPeriodeFinDecompte()).thenReturn(new Date("01.02.2014"));
        Decompte decompte = new Decompte();
        decompte.setType(TypeDecompte.COMPLEMENTAIRE);
        when(decompteSalaire.getDecompte()).thenReturn(decompte);

        decompteSalaireService.handleAC(decompteSalaire);

        assertEquals(cotisationDecompteAC.getMasse(), new Montant(1000));
        assertEquals(cotisationDecompteAC2.getMasse(), new Montant(1000));
    }

    @Test
    public void handleAC_GivenDecompteSalaireOf2000WithControleAC2WithAlready10000ACAnd2000AC2_ShouldAdd4000ACAndRemove2000AC2() {
        init();
        doReturn(new Montant(10000)).when(decompteSalaireService).findMasseACFor(any(String.class), any(Annee.class));
        doReturn(new Montant(2000)).when(decompteSalaireService).findMasseAC2For(any(DecompteSalaire.class));
        when(decompteSalaire.getSalaireTotal()).thenReturn(new Montant(2000));
        when(decompteSalaire.getPeriodeFinDecompte()).thenReturn(new Date("01.02.2014"));
        Decompte decompte = new Decompte();
        decompte.setType(TypeDecompte.COMPLEMENTAIRE);
        when(decompteSalaire.getDecompte()).thenReturn(decompte);

        decompteSalaireService.handleAC(decompteSalaire);

        assertEquals(cotisationDecompteAC.getMasse(), new Montant(4000));
        assertEquals(cotisationDecompteAC2.getMasse(), new Montant(-2000));
    }

    @Test
    @Ignore("Suite à la monter en version de mockito ce test ne passe plus")
    public void handleAC_GivenDecompteSalaireOf500WithControleAC2WithAlready20000ACAnd2000AC2_ShouldAdd1000ACAndRemove500AC2() {
        init();
        doReturn(new Montant(20000)).when(decompteSalaireService).findMasseACFor(any(String.class), any(Annee.class));
        doReturn(new Montant(2000)).when(decompteSalaireService).findMasseAC2For(any(DecompteSalaire.class));
        when(decompteSalaire.getSalaireTotal()).thenReturn(new Montant(500));
        when(decompteSalaire.getPeriodeFinDecompte()).thenReturn(new Date("01.02.2014"));
        Decompte decompte = new Decompte();
        decompte.setType(TypeDecompte.COMPLEMENTAIRE);
        when(decompteSalaire.getDecompte()).thenReturn(decompte);

        decompteSalaireService.handleAC(decompteSalaire);

        assertEquals(cotisationDecompteAC.getMasse(), new Montant(1000));
        assertEquals(cotisationDecompteAC2.getMasse(), new Montant(-500));
    }

    @Test
    public void handleAC_GivenDecompteSalaireOf5000WithControleAC2WithAlready22000ACAnd9000AC2_ShouldAdd14000ACAndRemove9000AC2() {
        init();
        doReturn(new Montant(22000)).when(decompteSalaireService).findMasseACFor(any(String.class), any(Annee.class));
        doReturn(new Montant(9000)).when(decompteSalaireService).findMasseAC2For(any(DecompteSalaire.class));
        when(decompteSalaire.getSalaireTotal()).thenReturn(new Montant(5000));
        when(decompteSalaire.getPeriodeFinDecompte()).thenReturn(new Date("01.04.2014"));
        Decompte decompte = new Decompte();
        decompte.setType(TypeDecompte.COMPLEMENTAIRE);
        when(decompteSalaire.getDecompte()).thenReturn(decompte);

        decompteSalaireService.handleAC(decompteSalaire);

        assertEquals(cotisationDecompteAC.getMasse(), new Montant(14000));
        assertEquals(cotisationDecompteAC2.getMasse(), new Montant(-9000));
    }

    @Test
    public void handleAC_GivenDecompteSalaireOf200000OutsideOfPeriodeDecompte_ShouldAdd126000ACAnd740000AC2() {
        init();
        doReturn(new Montant(0)).when(decompteSalaireService).findMasseACFor(any(String.class), any(Annee.class));
        doReturn(new Montant(0)).when(decompteSalaireService).findMasseAC2For(any(DecompteSalaire.class));
        when(decompteSalaire.getSalaireTotal()).thenReturn(new Montant(200000));
        when(decompteSalaire.controleAC2()).thenReturn(false);
        when(decompteSalaire.isMemeAnneeDecompte()).thenReturn(false);
        when(decompteSalaire.getPeriodeDebutDecompte()).thenReturn(new Date("01.04.2014"));
        doReturn(new Montant(126000)).when(decompteSalaireService).findMontantMaximumForAC(any(DecompteSalaire.class),
                any(Annee.class));
        Decompte decompte = mock(Decompte.class);
        doReturn(decompte).when(decompteSalaireService).findDecompte(any(String.class));
        when(decompte.getMontantAC(any(DecompteSalaire.class))).thenReturn(new Montant(0));

        decompte.setType(TypeDecompte.COMPLEMENTAIRE);
        decompte.setPeriode(new PeriodeMensuelle(new Date("01.04.2014"), new Date("30.04.2014")));
        when(decompteSalaire.getDecompte()).thenReturn(decompte);
        doReturn(decompte).when(decompteSalaireService).findDecompte(any(String.class));
        decompteSalaireService.handleAC(decompteSalaire);

        assertEquals(cotisationDecompteAC.getMasse(), new Montant(126000));
        assertEquals(cotisationDecompteAC2.getMasse(), new Montant(74000));
    }

    @Test
    public void handleAC_GivenDecompteSalaireOf20000WithOneDecompteSalaireOf26000_ShouldBe100_000ACAnd100_000AC2() {
        init();
        doReturn(new Montant(0)).when(decompteSalaireService).findMasseACFor(any(String.class), any(Annee.class));
        doReturn(new Montant(0)).when(decompteSalaireService).findMasseAC2For(any(DecompteSalaire.class));
        when(decompteSalaire.getSalaireTotal()).thenReturn(new Montant(200000));
        when(decompteSalaire.controleAC2()).thenReturn(false);
        when(decompteSalaire.isMemeAnneeDecompte()).thenReturn(false);
        when(decompteSalaire.getPeriodeDebutDecompte()).thenReturn(new Date("01.04.2014"));
        doReturn(new Montant(126000)).when(decompteSalaireService).findMontantMaximumForAC(any(DecompteSalaire.class),
                any(Annee.class));
        Decompte decompte = mock(Decompte.class);
        when(decompte.getMontantAC(any(DecompteSalaire.class))).thenReturn(new Montant(26000));
        doReturn(decompte).when(decompteSalaireService).findDecompte(any(String.class));

        decompte.setType(TypeDecompte.COMPLEMENTAIRE);
        decompte.setPeriode(new PeriodeMensuelle(new Date("01.04.2014"), new Date("30.04.2015")));
        when(decompteSalaire.getDecompte()).thenReturn(decompte);

        decompteSalaireService.handleAC(decompteSalaire);

        assertEquals(cotisationDecompteAC.getMasse(), new Montant(100000));
        assertEquals(cotisationDecompteAC2.getMasse(), new Montant(100000));
    }

    @Test
    public void handleAC_GivenDecompteSalaireOf20000WithOneDecompteSalaireOf26000_ShouldBe100_000ACAnd100_000AC2_MemeDecompte() {
        init();
        doReturn(new Montant(0)).when(decompteSalaireService).findMasseACFor(any(String.class), any(Annee.class));
        doReturn(new Montant(0)).when(decompteSalaireService).findMasseAC2For(any(DecompteSalaire.class));
        when(decompteSalaire.getSalaireTotal()).thenReturn(new Montant(200000));
        when(decompteSalaire.controleAC2()).thenReturn(false);
        when(decompteSalaire.isMemeAnneeDecompte()).thenReturn(true);
        when(decompteSalaire.getPeriodeDebutDecompte()).thenReturn(new Date("01.04.2014"));
        when(decompteSalaire.getPeriodeFinDecompte()).thenReturn(new Date("30.04.2014"));
        doReturn(new Montant(126000)).when(decompteSalaireService).findMontantMaximumForAC(any(DecompteSalaire.class),
                any(Annee.class));
        Decompte decompte = mock(Decompte.class);
        when(decompte.getMontantAC(any(DecompteSalaire.class))).thenReturn(new Montant(0));
        decompte.setType(TypeDecompte.COMPLEMENTAIRE);
        decompte.setPeriode(new PeriodeMensuelle(new Date("01.04.2014"), new Date("30.04.2014")));
        when(decompteSalaire.getDecompte()).thenReturn(decompte);
        doReturn(decompte).when(decompteSalaireService).findDecompte(any(String.class));

        decompteSalaireService.handleAC(decompteSalaire);

        assertEquals(cotisationDecompteAC.getMasse(), new Montant(42000));
        assertEquals(cotisationDecompteAC2.getMasse(), new Montant(158000));
    }

    private void init() {
        cotisationDecompteAC = spy(new CotisationDecompte());
        cotisationDecompteAC2 = spy(new CotisationDecompte());
        doReturn(new Assurance()).when(cotisationDecompteAC).getAssurance();
        doReturn(new Assurance()).when(cotisationDecompteAC2).getAssurance();
        when(cotisationDecompteAC2.getAssurance()).thenReturn(new Assurance());
        decompteSalaire = mock(DecompteSalaire.class);
        when(decompteSalaire.getCotisationAC()).thenReturn(cotisationDecompteAC);
        when(decompteSalaire.getCotisationAC2()).thenReturn(cotisationDecompteAC2);
        when(decompteSalaire.controleAC2()).thenReturn(true);
        when(decompteSalaire.getPeriode()).thenReturn(new Periode("01.2014", "01.2014"));
        when(decompteSalaire.getAnnee()).thenReturn(new Annee(2014));
        when(decompteSalaire.isMemeAnneeDecompte()).thenReturn(true);

        Occupation occupation = new Occupation();
        occupation.setDateValidite(new Date("01.01.1990"));
        occupation.setTaux(new Taux(100));
        List<Occupation> occupations = Arrays.asList(occupation);

        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setOccupations(occupations);
        doReturn(occupations).when(decompteSalaireService).findOccupations(any(PosteTravail.class));
        when(decompteSalaire.getPosteTravail()).thenReturn(posteTravail);
        doReturn(new Montant(10500)).when(decompteSalaireService).getPlafondParMois(any(Assurance.class),
                any(Date.class));
        doReturn(new Decompte()).when(decompteSalaireService).findDecompte(any(String.class));
        doNothing().when(decompteSalaireService).findLignesMemeDecompte(any(DecompteSalaire.class));
    }

    @Test
    public void findMasseACFor_WithNoDecompteSalaire_ShouldReturn0() {
        DecompteSalaire decompteSalaire = mock(DecompteSalaire.class);
        when(decompteSalaire.getPeriodeDebutDecompte()).thenReturn(new Date("01.01.2014"));
        when(decompteSalaireRepository.findForYear(any(String.class), any(Annee.class))).thenReturn(
                new ArrayList<DecompteSalaire>());

        Montant montant = decompteSalaireService.findMasseACFor(any(String.class), any(Annee.class));
        assertEquals(montant, Montant.ZERO);
    }

    public DecompteSalaire createDecompteSalaire(Montant montant, boolean isComptabilise) {
        DecompteSalaire decompteSalaire = mock(DecompteSalaire.class);
        when(decompteSalaire.getSalaireTotal()).thenReturn(montant);
        when(decompteSalaire.isComptabilise()).thenReturn(isComptabilise);
        when(decompteSalaire.getCotisationAC()).thenReturn(new CotisationDecompte());
        return decompteSalaire;
    }

    @Test
    public void findMontantMaximumForAC_GivenMarchAndPlafond10500_ShouldReturn31500() {
        init();
        doReturn(new Montant(10500)).when(decompteSalaireService).getPlafondParMois(any(Assurance.class),
                any(Date.class));
        Montant montant = decompteSalaireService.findMontantMaximumForAC(decompteSalaire, new Date("01.03.2014"));
        assertEquals(montant, new Montant(31500));
    }

    @Test
    public void cumulSalaire_GivenNothing_ShouldReturn0() {
        Montant montant = decompteSalaireService.cumulSalaire("1", "1", new Date("01.01.2014"), new Date("31.01.2014"));
        assertEquals(montant, Montant.ZERO);
    }

    @Test
    public void cumulSalaire_GivenDecompteSalaireOf2000WithCaisse1_ShouldReturn2000() {
        DecompteSalaire decompteSalaire = new DecompteSalaire();
        decompteSalaire.setSalaireTotal(new Montant(2000));
        decompteSalaire.setPeriode(new Periode("01.01.2014", "01.01.2014"));
        when(decompteSalaireRepository.findByIdAndPeriode(anyString(), any(Date.class), any(Date.class))).thenReturn(
                Arrays.asList(decompteSalaire));
        mockFindCaisseMetierForDecompteSalaireToReturn("1");
        Montant montant = decompteSalaireService.cumulSalaire("1", "1", new Date("01.01.2014"), new Date("31.01.2014"));
        assertEquals(montant, new Montant(2000));
    }

    @Test
    public void cumulSalaire_GivenDecompteSalaireOf2000WithCaisse2_ShouldReturn0() {
        DecompteSalaire decompteSalaire = new DecompteSalaire();
        decompteSalaire.setSalaireTotal(new Montant(2000));
        decompteSalaire.setPeriode(new Periode("01.01.2014", "01.01.2014"));
        when(decompteSalaireRepository.findByIdAndPeriode(anyString(), any(Date.class), any(Date.class))).thenReturn(
                Arrays.asList(decompteSalaire));
        mockFindCaisseMetierForDecompteSalaireToReturn("2");
        Montant montant = decompteSalaireService.cumulSalaire("1", "1", new Date("01.01.2014"), new Date("31.01.2014"));
        assertEquals(montant, Montant.ZERO);
    }

    @Test
    public void findMontantMaximumForACWithTauxOf10PDuring2MonthAndTauxOf100PDuring1Month_ShouldReturn31500() {
        init();
        Occupation occupation = new Occupation();
        occupation.setDateValidite(new Date("01.01.2014"));
        occupation.setTaux(new Taux(10));
        Occupation occupation2 = new Occupation();
        occupation2.setDateValidite(new Date("01.03.2014"));
        occupation2.setTaux(new Taux(100));
        List<Occupation> occupations = Arrays.asList(occupation, occupation2);

        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setOccupations(occupations);
        doReturn(occupations).when(decompteSalaireService).findOccupations(any(PosteTravail.class));
        when(decompteSalaire.getPosteTravail()).thenReturn(posteTravail);

        doReturn(new Montant(10500)).when(decompteSalaireService).getPlafondParMois(any(Assurance.class),
                any(Date.class));
        Montant montant = decompteSalaireService.findMontantMaximumForAC(decompteSalaire, new Date("01.03.2014"));
        assertEquals(montant, new Montant(31500));
    }

    @Test
    public void deductionFranchise_GivenMasseSalarialeOf1000AndMontantFranchiseOf1400_ShouldBe0() {
        Montant masseSalariale = new Montant(1000);
        Montant montantFranchise = new Montant(1400);

        assertEquals(new Montant(0), decompteSalaireService.deductionFranchise(masseSalariale, montantFranchise));
    }

    @Test
    public void deductionFranchise_GivenMasseSalarialeOf1600AndMontantFranchiseOf1400_ShouldBe200() {
        Montant masseSalariale = new Montant(1600);
        Montant montantFranchise = new Montant(1400);

        assertEquals(new Montant(200), decompteSalaireService.deductionFranchise(masseSalariale, montantFranchise));
    }

    @Test
    public void deductionFranchise_GivenMasseSalarialeOf1600AndNullAsMontantFranchise_ShouldBe1600() {
        Montant masseSalariale = new Montant(1600);
        Montant montantFranchise = null;

        assertEquals(new Montant(1600), decompteSalaireService.deductionFranchise(masseSalariale, montantFranchise));
    }

    @Test
    public void deductionFranchise_GivenMasseSalarialeOfMinus245_50AndNullAsMontantFranchise_ShouldBeMinus245_50() {
        Montant masseSalariale = new Montant(-245.50);
        Montant montantFranchise = null;

        assertEquals(new Montant(-245.50), decompteSalaireService.deductionFranchise(masseSalariale, montantFranchise));
    }

    @Test
    public void deductionFranchise_GivenMasseSalarialeOfMinus245_50AndMontantFranchiseOf0_ShouldBeMinus245_50() {
        Montant masseSalariale = new Montant(-245.50);
        Montant montantFranchise = new Montant(0);

        assertEquals(new Montant(-245.50), decompteSalaireService.deductionFranchise(masseSalariale, montantFranchise));
    }

    @Test
    public void deductionFranchise_GivenMasseSalarialeOfMinus245_50AndMontantFranchiseOf1000_ShouldBeMinus1245_50() {
        Montant masseSalariale = new Montant(-245.50);
        Montant montantFranchise = new Montant(1000);

        assertEquals(new Montant(-245.50), decompteSalaireService.deductionFranchise(masseSalariale, montantFranchise));
    }

    private void mockFindCaisseMetierForDecompteSalaireToReturn(String value) {
        doReturn(value).when(decompteSalaireService).findCaisseMetierForDecompteSalaire(any(DecompteSalaire.class));
    }
}
