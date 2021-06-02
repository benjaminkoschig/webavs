package ch.globaz.corvus.process.dnra;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ch.globaz.pyxis.domaine.Pays;
import ch.globaz.pyxis.loader.PaysLoader;

@RunWith(MockitoJUnitRunner.class)
public class MutationsContainerTest {
    @Mock
    PaysLoader paysLoader;

    @Test
    public void testExtractNss() throws Exception {
       // when(paysLoader.resolveByCodeCentrale(any(String.class))).thenReturn(new Pays());

        MutationsContainer container = new MutationsContainer(paysLoader);
        Mutation mutation = new Mutation();
        mutation.setNss("756.0000.0000.00");
        mutation.setTypeMutation(TypeMutation.QUOTIDIEN);
        mutation.setValide(true);
        container.add(mutation);
        assertThat(container.extractNssActifEtInactif()).hasSize(1);
    }
}
