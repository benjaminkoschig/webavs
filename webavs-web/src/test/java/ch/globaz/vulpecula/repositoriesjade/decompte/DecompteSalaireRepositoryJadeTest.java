package ch.globaz.vulpecula.repositoriesjade.decompte;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.repositoriesjade.decompte.DecompteSalaireRepositoryJade.LoadOptions;

@SuppressWarnings("unchecked")
public class DecompteSalaireRepositoryJadeTest {
    private DecompteSalaireRepositoryJade decompteSalaireRepositoryJade;

    @Before
    public void setUp() {
        decompteSalaireRepositoryJade = spy(new DecompteSalaireRepositoryJade());
        doNothing().when(decompteSalaireRepositoryJade).loadAbsences(anyList(), anyList());
        doNothing().when(decompteSalaireRepositoryJade).loadCotisations(anyList(), anyList());
    }

    @Test
    public void loadDependenciesWithNoLoadOptions_verifyThatLoadAbsencesAndLoadCotisationsAreCalled() {
        decompteSalaireRepositoryJade.loadDependencies(new ArrayList<DecompteSalaire>());
        verify(decompteSalaireRepositoryJade).loadAbsences(anyList(), anyList());
        verify(decompteSalaireRepositoryJade).loadCotisations(anyList(), anyList());
    }

    @Test
    public void loadDependenciesWithAbsences_verifyThatOnlyLoadAbsencesIsCalled() {
        decompteSalaireRepositoryJade.loadDependencies(new ArrayList<DecompteSalaire>(), LoadOptions.ABSENCES);
        verify(decompteSalaireRepositoryJade).loadAbsences(anyList(), anyList());
        verify(decompteSalaireRepositoryJade, never()).loadCotisations(anyList(), anyList());
    }

    @Test
    public void loadDependenciesWithCotisations_verifyThatOnlyLoadCotisationsIsCalled() {
        decompteSalaireRepositoryJade.loadDependencies(new ArrayList<DecompteSalaire>(), LoadOptions.COTISATIONS);
        verify(decompteSalaireRepositoryJade, never()).loadAbsences(anyList(), anyList());
        verify(decompteSalaireRepositoryJade).loadCotisations(anyList(), anyList());
    }

    @Test
    public void loadDependenciesWithCotisationsAndAbsences_verifyThatLoadAbsencesAndLoadCotisationsAreCalled() {
        decompteSalaireRepositoryJade.loadDependencies(new ArrayList<DecompteSalaire>(), LoadOptions.COTISATIONS,
                LoadOptions.ABSENCES);
        verify(decompteSalaireRepositoryJade).loadAbsences(anyList(), anyList());
        verify(decompteSalaireRepositoryJade).loadCotisations(anyList(), anyList());
    }

    @Test
    public void testLoadOptions() {
        List<LoadOptions> loadOptions = loadOptions();
        assertNotNull(loadOptions);
        assertEquals(loadOptions.isEmpty(), true);
    }

    public List<LoadOptions> loadOptions(LoadOptions... loadOptions) {
        return Arrays.asList(loadOptions);
    }
}
