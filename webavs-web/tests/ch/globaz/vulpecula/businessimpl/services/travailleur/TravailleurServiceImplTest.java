package ch.globaz.vulpecula.businessimpl.services.travailleur;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.exceptions.GlobazBusinessException;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.postetravail.PosteTravailService;
import ch.globaz.vulpecula.business.services.travailleur.TravailleurService;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.repositories.postetravail.PosteTravailRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.TravailleurRepository;

/**
 * @author Arnaud Geiser (AGE) | Créé le 22 avr. 2014
 * 
 */
public class TravailleurServiceImplTest {
    private TravailleurService travailleurService;

    private TravailleurRepository travailleurRepository;
    private PosteTravailRepository posteTravailRepository;
    private PosteTravailService posteTravailService;

    private Travailleur travailleur;

    @Before
    public void setUp() {
        travailleurRepository = mock(TravailleurRepository.class);
        posteTravailRepository = mock(PosteTravailRepository.class);
        posteTravailService = mock(PosteTravailService.class);
        travailleurService = new TravailleurServiceImpl(travailleurRepository, posteTravailRepository,
                posteTravailService);
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
        when(posteTravailRepository.findByIdTravailleur(anyString())).thenReturn(Arrays.asList(new PosteTravail()));

        try {
            travailleurService.delete(travailleur);
            fail("Le travailleur ne peut être supprimé car il contient un poste de travail");
        } catch (GlobazBusinessException e) {
        }
    }
}
