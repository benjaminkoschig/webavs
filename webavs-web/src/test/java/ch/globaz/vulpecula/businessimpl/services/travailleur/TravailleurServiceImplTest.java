package ch.globaz.vulpecula.businessimpl.services.travailleur;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.exceptions.GlobazBusinessException;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.postetravail.PosteTravailService;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.ebusiness.SynchronisationTravailleur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.repositories.ebusiness.SynchronisationTravailleurEbuRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.PosteTravailRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.TravailleurRepository;

/**
 * @author Arnaud Geiser (AGE) | Créé le 22 avr. 2014
 * 
 */
public class TravailleurServiceImplTest {
    private TravailleurServiceImpl travailleurService;

    private TravailleurRepository travailleurRepository;
    private PosteTravailRepository posteTravailRepository;
    private PosteTravailService posteTravailService;
    private SynchronisationTravailleurEbuRepository synchroRepository;

    private Travailleur travailleur;

    @Before
    public void setUp() {
        travailleurRepository = mock(TravailleurRepository.class);
        posteTravailRepository = mock(PosteTravailRepository.class);
        posteTravailService = mock(PosteTravailService.class);
        synchroRepository = mock(SynchronisationTravailleurEbuRepository.class);

        travailleurService = spy(new TravailleurServiceImpl(travailleurRepository, posteTravailRepository,
                posteTravailService, synchroRepository));
        travailleur = new Travailleur();
    }

    @Test
    public void givenValidTravailleurWhenCreateShouldBeValid() throws GlobazBusinessException {
        travailleur.setIdTiers("1");
        travailleurService.create(travailleur);

        assertTrue(true);
    }

    @Test
    public void givenTravailleurWithoutIdtiersWhenCreateShouldThrowException() throws GlobazBusinessException {
        try {
            travailleurService.create(travailleur);
            fail("Un travailleur sans id ne peut être inséré");
        } catch (GlobazBusinessException ex) {

        }
    }

    @Test
    public void givenTravailleurWithEmptyIdtiersWhenCreateShouldThrowException()
            throws UnsatisfiedSpecificationException {
        try {
            travailleur.setIdTiers("");
            travailleurService.create(travailleur);
            fail("Un travailleur sans id ne peut être inséré");
        } catch (GlobazBusinessException ex) {

        }
    }

    @Test
    public void givenAnAlreadyExistantTravailleurWhenCreateShouldThrowException() {
        try {
            String id = "1";

            travailleur.setIdTiers(id);
            when(travailleurRepository.findByIdTiers(any(String.class))).thenReturn(new Travailleur());

            travailleurService.create(travailleur);

            fail("Un travailleur existant ne peut être réinséré");
        } catch (GlobazBusinessException ex) {

        }
    }

    @Test
    public void givenTravailleurWithoutPosteTravailWhenDeleteShouldPass() throws GlobazBusinessException {
        travailleurService.delete(travailleur);
    }

    @Test
    public void givenTravailleurWithPosteTravailWhenDeleteShouldThrowException() {
        travailleur.setId("1");

        when(posteTravailRepository.findByIdTravailleur(anyString())).thenReturn(Arrays.asList(new PosteTravail()));

        try {
            travailleurService.delete(travailleur);
            fail("Le travailleur ne peut être supprimé car il contient un poste de travail");
        } catch (GlobazBusinessException e) {
        }
    }

    @Test
    public void isRentier_Given65SameMonth_ShouldBeFalse() throws Exception {
        Travailleur travailleur = mock(Travailleur.class);
        when(travailleur.getDateNaissance()).thenReturn("01.01.2000");
        when(travailleur.getSexe()).thenReturn("516001");
        when(travailleur.getNumAvsActuel()).thenReturn("756.3740.9933.13");
        assertFalse(travailleurService.isRentier(travailleur, new Date("01.01.2065")));
    }

    @Test
    public void isRentier_Given65NextMonth_ShouldBeTrue() throws Exception {
        Travailleur travailleur = mock(Travailleur.class);
        when(travailleur.getDateNaissance()).thenReturn("01.01.2000");
        when(travailleur.getSexe()).thenReturn("516001");
        when(travailleur.getNumAvsActuel()).thenReturn("756.3740.9933.13");
        assertTrue(travailleurService.isRentier(travailleur, new Date("01.02.2065")));
    }

    @Test
    public void isRentier_Given65NextMonth2_ShouldBeTrue() throws Exception {
        Travailleur travailleur = mock(Travailleur.class);
        when(travailleur.getDateNaissance()).thenReturn("31.12.2000");
        when(travailleur.getSexe()).thenReturn("516001");
        when(travailleur.getNumAvsActuel()).thenReturn("756.3740.9933.13");
        assertTrue(travailleurService.isRentier(travailleur, new Date("01.01.2066")));
    }

    @Test
    public void isRentier_Given65SameMonth2_ShouldBeFalse() throws Exception {
        Travailleur travailleur = mock(Travailleur.class);
        when(travailleur.getDateNaissance()).thenReturn("31.12.2000");
        when(travailleur.getSexe()).thenReturn("516001");
        when(travailleur.getNumAvsActuel()).thenReturn("756.3740.9933.13");
        assertFalse(travailleurService.isRentier(travailleur, new Date("31.12.2065")));
    }

    @Test
    public void isRentier_Given64_ShouldBeFalse() throws Exception {
        Travailleur travailleur = mock(Travailleur.class);
        when(travailleur.getDateNaissance()).thenReturn("01.01.2000");
        when(travailleur.getSexe()).thenReturn("516001");
        when(travailleur.getNumAvsActuel()).thenReturn("756.3740.9933.13");
        assertFalse(travailleurService.isRentier(travailleur, new Date("01.02.2064")));
    }

    @Test
    public void giveDateRentier() throws Exception {
        assertEquals(travailleurService.giveDateRentier("01.01.2000", "516001"), new Date("01.02.2065"));
    }

    @Test
    @Ignore
    public void ackSyncTravailleurs_givenListIds() {
        SynchronisationTravailleur synchro1 = new SynchronisationTravailleur();
        synchro1.setId("1");
        SynchronisationTravailleur synchro2 = new SynchronisationTravailleur();
        synchro1.setId("2");
        SynchronisationTravailleur synchro3 = new SynchronisationTravailleur();
        synchro1.setId("3");

        when(synchroRepository.findById("1")).thenReturn(synchro1);
        when(synchroRepository.findById("2")).thenReturn(synchro2);
        when(synchroRepository.findById("3")).thenReturn(synchro3);
        when(synchroRepository.update(any(SynchronisationTravailleur.class))).thenReturn(
                new SynchronisationTravailleur());

        List<String> listeIds = new ArrayList<String>();
        listeIds.add("1");
        listeIds.add("2");
        listeIds.add("3");

        assertNull(synchro1.getDateSynchronisation());
        assertNull(synchro2.getDateSynchronisation());
        assertNull(synchro3.getDateSynchronisation());
        travailleurService.ackSyncTravailleurs(listeIds);
        assertNotNull(synchro1.getDateSynchronisation());
        assertEquals(synchro1.getDateSynchronisation(), Date.now());
        assertNotNull(synchro2.getDateSynchronisation());
        assertEquals(synchro2.getDateSynchronisation(), Date.now());
        assertNotNull(synchro3.getDateSynchronisation());
        assertEquals(synchro3.getDateSynchronisation(), Date.now());
    }
}
