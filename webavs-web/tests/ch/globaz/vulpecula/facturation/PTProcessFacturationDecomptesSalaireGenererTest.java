package ch.globaz.vulpecula.facturation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.decompte.CotisationCalculee;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;

public class PTProcessFacturationDecomptesSalaireGenererTest {
    private PTProcessFacturationDecomptesSalaireGenerer process;

    @Before
    public void before() {
        process = new PTProcessFacturationDecomptesSalaireGenerer();
    }

    @Test(expected = NullPointerException.class)
    public void findIdRubriqueForEcart_GivenEmptyList_ShouldThrowNPE() {
        process.findIdRubriqueForEcart(new ArrayList<CotisationCalculee>());
    }

    @Test
    public void findIdrubriqueForEcart_GivenCotisationAF_ShouldReturnRubriqueAF() {
        assertEquals("1", process.findIdRubriqueForEcart(Arrays.asList(create(TypeAssurance.COTISATION_AF, "1"))));
    }

    @Test
    public void findIdrubriqueForEcart_GivenCotisationAFAndAVS_ShouldReturnRubriqueAF() {
        assertEquals(
                "1",
                process.findIdRubriqueForEcart(Arrays.asList(create(TypeAssurance.COTISATION_AF, "1"),
                        create(TypeAssurance.COTISATION_AVS_AI, "2"))));
    }

    @Test
    public void findIdrubriqueForEcart_GivenCotisationAFAndAVS_ShouldReturnRubriqueAutres() {
        assertEquals(
                "3",
                process.findIdRubriqueForEcart(Arrays.asList(create(TypeAssurance.COTISATION_FFPP_MASSE, "1"),
                        create(TypeAssurance.AUTRES, "2"), create(TypeAssurance.COTISATION_FFPP_CAPITATION, "3"))));
    }

    private CotisationCalculee create(TypeAssurance typeAssurance, String idRubrique) {
        CotisationCalculee cotisationCalculee = mock(CotisationCalculee.class);
        when(cotisationCalculee.getIdRubrique()).thenReturn(idRubrique);
        when(cotisationCalculee.getTypeAssurance()).thenReturn(typeAssurance);
        return cotisationCalculee;
    }
}
