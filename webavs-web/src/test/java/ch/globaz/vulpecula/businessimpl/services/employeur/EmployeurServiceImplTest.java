package ch.globaz.vulpecula.businessimpl.services.employeur;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.vulpecula.business.services.postetravail.PosteTravailService;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.repositories.postetravail.EmployeurRepository;
import ch.globaz.vulpecula.external.models.affiliation.Particularite;

public class EmployeurServiceImplTest {
    private Employeur employeur;

    private EmployeurRepository employeurRepository;

    private PosteTravailService posteTravailService;
    private EmployeurServiceImpl employeurService;

    @Before
    public void setUp() {
        employeur = new Employeur();
        employeurRepository = mock(EmployeurRepository.class);
        posteTravailService = mock(PosteTravailService.class);
        employeurService = spy(new EmployeurServiceImpl(employeurRepository, posteTravailService));
    }

    @Ignore
    @Test
    public void givenEmployeurWithoutPosteActifWhenHasPosteTravailActifThenReturnFalse() {
        when(posteTravailService.findPostesActifsByIdAffilie(anyString(), any(Date.class))).thenReturn(
                new ArrayList<PosteTravail>());

        boolean excepted = employeurService.hasPosteTravailActifs(employeur, new Date());

        assertFalse(excepted);
    }

    @Test
    public void test() {
        Employeur employeur = new Employeur();
        employeur.setDateDebut("01.01.2017");
        when(employeurRepository.findEmployeursWithEdition()).thenReturn(Arrays.asList(employeur));
        doReturn(false).when(employeurService).hasPostesActifs(any(Employeur.class), any(Date.class), any(Date.class));
        List<Employeur> employeurs = employeurService.findEmployeursSansPostesAvecEdition(new Date("01.01.2016"),
                new Date("31.12.2016"));
        assertTrue(employeurs.isEmpty());
    }

    @Test
    public void test2() {
        Employeur employeur = new Employeur();
        employeur.setDateDebut("01.01.2016");
        when(employeurRepository.findEmployeursWithEdition()).thenReturn(Arrays.asList(employeur));
        doReturn(false).when(employeurService).hasPostesActifs(any(Employeur.class), any(Date.class), any(Date.class));
        List<Employeur> employeurs = employeurService.findEmployeursSansPostesAvecEdition(new Date("01.01.2016"),
                new Date("31.12.2016"));
        assertFalse(employeurs.isEmpty());
    }

    @Test
    public void test3() {
        Employeur employeur = new Employeur();
        employeur.setDateDebut("01.01.2016");
        employeur.setDateFin("31.12.2016");
        when(employeurRepository.findEmployeursWithEdition()).thenReturn(Arrays.asList(employeur));
        doReturn(true).when(employeurService).hasPostesActifs(any(Employeur.class), any(Date.class), any(Date.class));
        List<Employeur> employeurs = employeurService.findEmployeursSansPostesAvecEdition(new Date("01.01.2016"),
                new Date("31.12.2016"));
        assertTrue(employeurs.isEmpty());
    }

    @Test
    public void test4() {
        Employeur employeur = new Employeur();
        employeur.setDateDebut("01.01.2016");
        employeur.setDateFin("31.12.2016");
        when(employeurRepository.findEmployeursWithEdition()).thenReturn(Arrays.asList(employeur));
        doReturn(false).when(employeurService).hasPostesActifs(any(Employeur.class), any(Date.class), any(Date.class));
        List<Employeur> employeurs = employeurService.findEmployeursSansPostesAvecEdition(new Date("01.01.2016"),
                new Date("31.12.2016"));
        assertFalse(employeurs.isEmpty());
    }

    @Ignore
    @Test
    public void givenEmployeurWithPosteActifWhenHasPosteTravailActifThenReturnTrue() {
        PosteTravail posteActif = new PosteTravail();
        posteActif.setPeriodeActivite(new Periode("01.01.2014", "04.04.2014"));
        when(posteTravailService.findPostesActifsByIdAffilie(anyString(), any(Date.class))).thenReturn(
                Arrays.asList(posteActif));

        boolean excepted = employeurService.hasPosteTravailActifs(employeur, new Date("01.01.2014"));

        assertTrue(excepted);
    }

    @Ignore
    @Test
    public void findEmployeursSansPostesAvecEdition_GivenNoEmployeur_ShouldBeEmpty() {
        List<Employeur> employeurs = employeurService.findEmployeursSansPostesAvecEdition(new Date("01.01.2015"),
                new Date("31.12.2015"));

        assertEquals(0, employeurs.size());
    }

    @Ignore
    @Test
    public void hasParticulariteSansPersonnel_GivenRandomEmployeurAnd01_01_2014_ShouldBeFalse() {
        List<Particularite> particularites = new ArrayList<Particularite>();
        doReturn(particularites).when(employeurService).findParticularites(anyString(), any(Date.class));
        assertFalse(employeurService.hasParticulariteSansPersonnel(employeur, new Date("01.01.2014")));
    }

    @Ignore
    @Test
    public void hasParticulariteSansPersonnel_GivenRandomEmployeurAnd01_01_2014WithParticulariteSansDateFin_ShouldBeTrue() {
        List<Particularite> particularites = new ArrayList<Particularite>();
        particularites.add(createParticularite("01.01.2014"));
        doReturn(particularites).when(employeurService).findParticularites(anyString(), any(Date.class));
        assertTrue(employeurService.hasParticulariteSansPersonnel(employeur, new Date("01.01.2014")));
    }

    @Ignore
    @Test
    public void hasParticulariteSansPersonnel_GivenRandomEmployeurAnd01_01_2014WithParticulariteAvecDateFin_ShouldBeFalse() {
        List<Particularite> particularites = new ArrayList<Particularite>();
        particularites.add(createParticularite("01.01.2013", "31.12.2013"));
        doReturn(particularites).when(employeurService).findParticularites(anyString(), any(Date.class));
        assertFalse(employeurService.hasParticulariteSansPersonnel(employeur, new Date("01.01.2014")));
    }

    @Ignore
    @Test
    public void getEmployeursSansParticularite_WithParticularite_ShouldBeFalse() {
        List<Employeur> employeurs = new ArrayList<Employeur>();
        employeurs.add(new Employeur());

        List<Particularite> particularites = new ArrayList<Particularite>();
        particularites.add(createParticularite("01.01.2013"));

        doReturn(particularites).when(employeurService).findParticularites(anyString(), any(Date.class));

        assertEquals(0, employeurService.getEmployeursSansParticularite(employeurs, new Date("01.01.2015")).size());
    }

    private Particularite createParticularite(String dateDebut) {
        return createParticularite(dateDebut, null);
    }

    private Particularite createParticularite(String dateDebut, String dateFin) {
        Particularite particularite = new Particularite();
        particularite.setDateDebut(new Date(dateDebut));
        if (dateFin != null) {
            particularite.setDateFin(new Date(dateFin));
        }
        return particularite;
    }

    private Employeur createEmployeur(String dateDebut, String dateFin) {
        Employeur employeur = new Employeur();
        employeur.setDateDebut(dateDebut);
        employeur.setDateFin(dateFin);
        return employeur;
    }
}
