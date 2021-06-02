package ch.globaz.vulpecula.businessimpl.services.postetravail;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;
import ch.globaz.naos.business.model.TauxAssuranceSimpleModel;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.prestations.Beneficiaire;
import ch.globaz.vulpecula.domain.repositories.caissemaladie.AffiliationCaisseMaladieRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.AdhesionCotisationPosteTravailRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.PosteTravailRepository;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.services.CotisationService;

public class PosteTravailServiceImplTest {
    private PosteTravailServiceImpl posteTravailService;

    private PosteTravailRepository posteTravailRepository;
    private AdhesionCotisationPosteTravailRepository adhesionCotisationRepository;
    private CotisationService cotisationService;
    private AffiliationCaisseMaladieRepository affiliationCaisseMaladieRepository;

    // @Before
    public void setUp() {
        posteTravailRepository = mock(PosteTravailRepository.class);
        cotisationService = mock(CotisationService.class);
        affiliationCaisseMaladieRepository = mock(AffiliationCaisseMaladieRepository.class);
        mock(AFTauxAssurance.class);

        posteTravailService = spy(new PosteTravailServiceImpl(posteTravailRepository, adhesionCotisationRepository,
                cotisationService, affiliationCaisseMaladieRepository));

    }

    @Ignore
    @Test
    public void getCotisationsForAJ_WithNull_ShouldThrowNullPointerException() {
        try {
            posteTravailService.getCotisationsForAJ("", Date.now());
            fail();
        } catch (NullPointerException ex) {
            assertTrue(true);
        }
    }

    @Ignore
    @Test
    public void getCotisationsForAJ_WithPosteTravailThatHaveEmployeurWithoutCotisations_ShouldReturnAnEmptyList() {
        PosteTravail posteTravail = mock(PosteTravail.class);
        List<Cotisation> cotisations = new ArrayList<Cotisation>();
        doReturn(cotisations).when(posteTravailService).getCotisationFor(Matchers.any(PosteTravail.class),
                Matchers.any(List.class), Matchers.any(Date.class));

        List<Cotisation> cotisationsForAJ = posteTravailService.getCotisationsForAJ(posteTravail, Date.now());

        assertThat(cotisationsForAJ.size(), is(0));
    }

    @Ignore
    @Test
    public void getCotisationsForAJ_WithPosteTravailThatHaveEmployeurWithOneCotisationAVS_ShouldReturn1Cotisation() {
        PosteTravail posteTravail = mock(PosteTravail.class);
        List<Cotisation> cotisations = new ArrayList<Cotisation>();
        cotisations.add(createCotisation(TypeAssurance.COTISATION_AVS_AI, "5.05"));
        doReturn(cotisations).when(posteTravailService).getCotisationFor(Matchers.any(PosteTravail.class),
                Matchers.any(List.class), Matchers.any(Date.class));

        List<Cotisation> cotisationsForAJ = posteTravailService.getCotisationsForAJ(posteTravail, Date.now());

        assertThat(cotisationsForAJ.size(), is(1));
    }

    @Ignore
    @Test
    public void getCotisationsForAJ_WithPosteTravailThatHaveEmployeurWithTwoCotisationOfAVSAndNonAVS_ShouldReturn2Cotisations() {
        PosteTravail posteTravail = mock(PosteTravail.class);
        List<Cotisation> cotisations = new ArrayList<Cotisation>();
        cotisations.add(createCotisation(TypeAssurance.COTISATION_AVS_AI, "5.05"));
        cotisations.add(createCotisation(TypeAssurance.COTISATION_AF, "1.00"));
        doReturn(cotisations).when(posteTravailService).getCotisationFor(Matchers.any(PosteTravail.class),
                Matchers.any(List.class), Matchers.any(Date.class));

        List<Cotisation> cotisationsForAJ = posteTravailService.getCotisationsForAJ(posteTravail, Date.now());

        assertThat(cotisationsForAJ.size(), is(2));
    }

    @Ignore
    @Test
    public void getNombreHeureParMois_RandomPosteTravailWithCaisseMetier10_ShouldReturn177_70() {
        doReturn(new Taux(100)).when(posteTravailService).findTauxOccupation(anyString(), Matchers.any(Date.class));
        doReturn(10).when(posteTravailService).getNumeroCaissePrincipale(anyString());
        double nombreHeure = posteTravailService.getNombreHeuresParMois("1", Date.now());
        assertEquals(177.70, nombreHeure, 0.0);
    }

    @Ignore
    @Test
    public void getNombreHeureParMois_PosteTravailWithTauxOf80PercentWithCaisseMetier10_ShouldReturn177_70() {
        doReturn(new Taux(80)).when(posteTravailService).findTauxOccupation(anyString(), Matchers.any(Date.class));
        doReturn(10).when(posteTravailService).getNumeroCaissePrincipale(anyString());
        double nombreHeure = posteTravailService.getNombreHeuresParMois("1", Date.now());
        assertEquals(142.16, nombreHeure, 0.0);
    }

    @Ignore
    @Test
    public void findAAnnoncer_GivenNoPoste_ShouldReturnAnEmptyArray() {
        when(posteTravailRepository.findAAnnoncer(Matchers.any(Date.class), eq(false))).thenReturn(
                new ArrayList<PosteTravail>());
        assertEquals(0, posteTravailService.findAAnnoncer(Date.now()).size());
    }

    @Ignore
    @Test
    public void findAAnnoncer_GivenTwoDifferentPosteWithSameTravailleur_ShouldReturnOneElement() {
        List<PosteTravail> postes = Arrays.asList(createPosteTravail("1", true, true),
                createPosteTravail("1", true, true));
        when(posteTravailRepository.findAAnnoncer(Matchers.any(Date.class), eq(false))).thenReturn(postes);
        assertEquals(1, posteTravailService.findAAnnoncer(Date.now()).size());
    }

    @Ignore
    @Test
    public void getNombreHeuresParMois() {
        doReturn(10).when(posteTravailService).getNumeroCaissePrincipale(anyString());
        doReturn(new Taux(100)).when(posteTravailService).findTauxOccupation(anyString(), Matchers.any(Date.class));
        assertEquals(177.7, posteTravailService.getNombreHeuresParMois("1", new Date("01.2014"), new Date("01.2014")),
                0.0);
    }

    @Ignore
    @Test
    public void getNombreHeuresParMois2() {
        doReturn(10).when(posteTravailService).getNumeroCaissePrincipale(anyString());
        doReturn(new Taux(50)).when(posteTravailService).findTauxOccupation(anyString(), Matchers.any(Date.class));
        assertEquals(177.7, posteTravailService.getNombreHeuresParMois("1", new Date("01.2014"), new Date("02.2014")),
                0.0);
    }

    @Ignore
    @Test
    public void getNombreHeuresParJourForCaisseMetier10_ShouldBe8_2() {
        doReturn(10).when(posteTravailService).getNumeroCaissePrincipale(anyString());
        doReturn(new Taux(50)).when(posteTravailService).findTauxOccupation(anyString(), Matchers.any(Date.class));
        assertEquals(8.2, posteTravailService.getNombreHeuresParJour("1", new Date("01.01.2015")), 0);
    }

    @Ignore
    @Test
    public void getPostesTravailsWithDroitsAJ_WithTwoPostesWithActifFirst_ShouldReturnActifFirst() {
        PosteTravail p1 = createPosteForAJ("01.01.2014", null);
        PosteTravail p2 = createPosteForAJ("01.01.1975", "31.01.1975");
        List<PosteTravail> postes = Arrays.asList(p1, p2);
        doReturn(10).when(posteTravailService).getNumeroCaissePrincipale(anyString());
        doReturn(postes).when(posteTravailRepository).findByIdTravailleurWithDependencies(anyString());
        assertThat(posteTravailService.getPostesTravailsWithDroitsAJ("1"), contains(p1, p2));
    }

    @Ignore
    @Test
    public void getPostesTravailsWithDroitsAJ_WithTwoPostesWithInactifFirst_ShouldReturnActifFirst() {
        PosteTravail p1 = createPosteForAJ("01.01.2014", null);
        PosteTravail p2 = createPosteForAJ("01.01.1975", "31.01.1975");
        List<PosteTravail> postes = Arrays.asList(p2, p1);
        doReturn(10).when(posteTravailService).getNumeroCaissePrincipale(anyString());
        doReturn(postes).when(posteTravailRepository).findByIdTravailleurWithDependencies(anyString());
        assertThat(posteTravailService.getPostesTravailsWithDroitsAJ("1"), contains(p1, p2));
    }

    @Ignore
    @Test
    public void findPlusAncienPosteActif_WithTwoPostesActifsSansDateFin_ShouldReturnTheOldest() {
        PosteTravail poste1 = new PosteTravail();
        PosteTravail poste2 = new PosteTravail();

        poste1.setPeriodeActivite(new Periode("01.01.2014", null));
        poste2.setPeriodeActivite(new Periode("01.01.2015", null));

        doReturn(Arrays.asList(poste1, poste2)).when(posteTravailRepository).findByIdTravailleur(anyString());

        assertEquals(poste1, posteTravailService.findPlusAncienPosteActif("1", new Date("01.01.2015")));
    }

    @Ignore
    @Test
    public void findPlusAncienPosteActif_WithTwoPostesActifsAvecDateFin_ShouldReturnTheOldest() {
        PosteTravail poste1 = new PosteTravail();
        PosteTravail poste2 = new PosteTravail();

        poste1.setPeriodeActivite(new Periode("01.01.2014", "31.01.2014"));
        poste2.setPeriodeActivite(new Periode("02.01.2014", "10.01.2014"));

        doReturn(Arrays.asList(poste2, poste1)).when(posteTravailRepository).findByIdTravailleur(anyString());

        assertEquals(poste1, posteTravailService.findPlusAncienPosteActif("1", new Date("05.01.2014")));
    }

    @Ignore
    @Test
    public void getTauxSelonBeneficaire_GivenBeneficiaireEmployeurAndTypeSalaireMois_ShouldBeValeurEmployeur() {
        TauxAssuranceSimpleModel tauxAssuranceSimpleModel = createTauxAssurance("10", "20");

        when(cotisationService.findTauxForAssurance(anyString(), Matchers.any(Date.class))).thenReturn(
                tauxAssuranceSimpleModel);
        Taux taux = posteTravailService.getTauxSelonBeneficaire(Date.now(), Beneficiaire.EMPLOYEUR, TypeSalaire.MOIS,
                new Cotisation());
        assertEquals(new Taux(20), taux);
    }

    @Ignore
    @Test
    public void getTauxSelonBeneficaire_GivenBeneficiaireTravailleurAndTypeSalaireMois_ShouldBeValeurEmploye() {
        TauxAssuranceSimpleModel tauxAssuranceSimpleModel = createTauxAssurance("10", "20");

        when(cotisationService.findTauxForAssurance(anyString(), Matchers.any(Date.class))).thenReturn(
                tauxAssuranceSimpleModel);
        Taux taux = posteTravailService.getTauxSelonBeneficaire(Date.now(), Beneficiaire.TRAVAILLEUR, TypeSalaire.MOIS,
                new Cotisation());
        assertEquals(new Taux(10), taux);
    }

    @Ignore
    @Test
    public void getTauxSelonBeneficaire_GivenBeneficiaireNoteDeCreditAndTypeSalaireMois_ShouldBeValeurEmployeur() {
        TauxAssuranceSimpleModel tauxAssuranceSimpleModel = createTauxAssurance("10", "20");

        when(cotisationService.findTauxForAssurance(anyString(), Matchers.any(Date.class))).thenReturn(
                tauxAssuranceSimpleModel);
        Taux taux = posteTravailService.getTauxSelonBeneficaire(Date.now(), Beneficiaire.NOTE_CREDIT, TypeSalaire.MOIS,
                new Cotisation());
        assertEquals(new Taux(20), taux);
    }

    @Ignore
    @Test
    public void getTauxSelonBeneficaire_GivenBeneficiaireEmployeurAndTypeSalaireHeures_ShouldBeValeurEmploye() {
        TauxAssuranceSimpleModel tauxAssuranceSimpleModel = createTauxAssurance("10", "20");

        when(cotisationService.findTauxForAssurance(anyString(), Matchers.any(Date.class))).thenReturn(
                tauxAssuranceSimpleModel);
        Taux taux = posteTravailService.getTauxSelonBeneficaire(Date.now(), Beneficiaire.EMPLOYEUR, TypeSalaire.HEURES,
                new Cotisation());
        assertEquals(new Taux(10), taux);
    }

    @Ignore
    @Test
    public void getTauxSelonBeneficaire_GivenBeneficiaireTravailleurAndTypeSalaireHeures_ShouldBeValeurEmploye() {
        TauxAssuranceSimpleModel tauxAssuranceSimpleModel = createTauxAssurance("10", "20");

        when(cotisationService.findTauxForAssurance(anyString(), Matchers.any(Date.class))).thenReturn(
                tauxAssuranceSimpleModel);
        Taux taux = posteTravailService.getTauxSelonBeneficaire(Date.now(), Beneficiaire.TRAVAILLEUR,
                TypeSalaire.HEURES, new Cotisation());
        assertEquals(new Taux(10), taux);
    }

    @Ignore
    @Test
    public void getTauxSelonBeneficaire_GivenBeneficiaireNoteDeCreditAndTypeSalaireHeures_ShouldBeValeurEmploye() {
        TauxAssuranceSimpleModel tauxAssuranceSimpleModel = createTauxAssurance("10", "20");

        when(cotisationService.findTauxForAssurance(anyString(), Matchers.any(Date.class))).thenReturn(
                tauxAssuranceSimpleModel);
        Taux taux = posteTravailService.getTauxSelonBeneficaire(Date.now(), Beneficiaire.NOTE_CREDIT,
                TypeSalaire.HEURES, new Cotisation());
        assertEquals(new Taux(10), taux);
    }

    @Ignore
    @Test
    public void cloturerCaissesMaladies_GivenNoAffiliation_ShouldNotUpdateAnything() {
        PosteTravail posteTravail = mock(PosteTravail.class);
        when(posteTravail.getFinActivite()).thenReturn(null);
        posteTravailService.cloturerCaissesMaladies(posteTravail);

        verify(affiliationCaisseMaladieRepository).findByIdPosteTravail(anyString());
        verify(affiliationCaisseMaladieRepository, times(0)).update(Matchers.any(AffiliationCaisseMaladie.class));
    }

    @Ignore
    @Test
    public void cloturerCaissesMaladies_GivenOneAffiliationWithoutDateFin_ShouldBeUpdated() {
        PosteTravail posteTravail = mock(PosteTravail.class);
        when(posteTravail.getFinActivite()).thenReturn(null);
        when(affiliationCaisseMaladieRepository.findByIdPosteTravail(anyString())).thenReturn(
                Arrays.asList(new AffiliationCaisseMaladie()));
        posteTravailService.cloturerCaissesMaladies(posteTravail);

        verify(affiliationCaisseMaladieRepository).findByIdPosteTravail(anyString());
        verify(affiliationCaisseMaladieRepository, times(1)).update(Matchers.any(AffiliationCaisseMaladie.class));
    }

    @Ignore
    @Test
    public void cloturerCaissesMaladies_GivenTwoAffiliationsWithoutDateFin_ShouldBeUpdated() {
        PosteTravail posteTravail = mock(PosteTravail.class);
        when(posteTravail.getFinActivite()).thenReturn(null);
        when(affiliationCaisseMaladieRepository.findByIdPosteTravail(anyString())).thenReturn(
                Arrays.asList(new AffiliationCaisseMaladie(), new AffiliationCaisseMaladie()));
        posteTravailService.cloturerCaissesMaladies(posteTravail);

        verify(affiliationCaisseMaladieRepository).findByIdPosteTravail(anyString());
        verify(affiliationCaisseMaladieRepository, times(2)).update(Matchers.any(AffiliationCaisseMaladie.class));
    }

    @Ignore
    @Test
    public void cloturerCaissesMaladies_GivenTwoAffiliationsOneWithDateFinAndOneWithoutDateFin_ShouldBeOnlyOneUpdate() {
        AffiliationCaisseMaladie affiliationAvecDateFin = new AffiliationCaisseMaladie();
        affiliationAvecDateFin.setMoisFin(new Date("01.01.2014"));

        PosteTravail posteTravail = mock(PosteTravail.class);
        when(posteTravail.getFinActivite()).thenReturn(null);
        when(affiliationCaisseMaladieRepository.findByIdPosteTravail(anyString())).thenReturn(
                Arrays.asList(new AffiliationCaisseMaladie(), affiliationAvecDateFin));
        posteTravailService.cloturerCaissesMaladies(posteTravail);

        verify(affiliationCaisseMaladieRepository).findByIdPosteTravail(anyString());
        verify(affiliationCaisseMaladieRepository, times(1)).update(Matchers.any(AffiliationCaisseMaladie.class));
    }

    private TauxAssuranceSimpleModel createTauxAssurance(String valeurEmploye, String valeurEmployeur) {
        TauxAssuranceSimpleModel tauxAssuranceSimpleModel = new TauxAssuranceSimpleModel();
        tauxAssuranceSimpleModel.setValeurEmploye("10");
        tauxAssuranceSimpleModel.setValeurEmployeur("20");
        return tauxAssuranceSimpleModel;
    }

    private PosteTravail createPosteForAJ(String dateDebut, String dateFin) {
        PosteTravail posteTravail = spy(new PosteTravail());
        doReturn(true).when(posteTravail).hasDroitAJ(anyInt());
        posteTravail.setPeriodeActivite(new Periode(dateDebut, dateFin));
        return posteTravail;
    }

    private PosteTravail createPosteTravail(String idTravailleur, boolean cotiseAVS, boolean hasMoreThan18Ans) {
        PosteTravail posteTravail = mock(PosteTravail.class);
        when(posteTravail.getIdTravailleur()).thenReturn(idTravailleur);
        when(posteTravail.hasMoreThanOrEquals18Ans(Matchers.any(Date.class))).thenReturn(true);
        when(posteTravail.cotiseAVS(Matchers.any(Date.class))).thenReturn(true);
        when(posteTravail.getDebutActivite()).thenReturn(Date.now());
        return posteTravail;
    }

    private Cotisation createCotisation(TypeAssurance typeAssurance, String taux) {
        Cotisation cotisation = mock(Cotisation.class);
        when(cotisation.getTypeAssurance()).thenReturn(typeAssurance);
        when(cotisation.getTaux()).thenReturn(new Taux(taux));
        return cotisation;
    }
}
