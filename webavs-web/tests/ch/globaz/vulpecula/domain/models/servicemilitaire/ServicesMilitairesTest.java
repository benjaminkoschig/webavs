package ch.globaz.vulpecula.domain.models.servicemilitaire;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.Map;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.registre.Convention;

public class ServicesMilitairesTest {
    @Test
    public void test() {
        Convention convention = new Convention();
        convention.setCode("04");
        convention.setId("53626");

        Convention convention2 = new Convention();
        convention2.setCode("01");
        convention2.setId("53622");

        ServiceMilitaire sm = mock(ServiceMilitaire.class);
        doReturn(convention).when(sm).getConventionEmployeur();
        doReturn(GenreSM.ECOLE_DE_RECRUE).when(sm).getGenre();
        ServiceMilitaire sm2 = mock(ServiceMilitaire.class);
        doReturn(convention2).when(sm2).getConventionEmployeur();
        doReturn(GenreSM.ECOLE_DE_RECRUE).when(sm2).getGenre();

        Map<Convention, SMsParType> groupByGenreSMByConvention = ServicesMilitaires.groupByGenreSMByConvention(Arrays
                .asList(sm2, sm));

        Object[] conventions = groupByGenreSMByConvention.keySet().toArray();

        assertThat((Convention) conventions[0], is(convention2));
        assertThat((Convention) conventions[1], is(convention));
    }
}
