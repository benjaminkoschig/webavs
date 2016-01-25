package ch.globaz.vulpecula.domain.models.congepaye;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;

public class CongePayeTest {
    private CongePaye congePaye;

    @Before
    public void setUp() {
        congePaye = new CongePaye();
    }

    @Test
    public void getPeriodeDebutAsSwissValue_GivenPeriodeOf2014_ShouldBe01_01_2014() {
        congePaye.setAnneeDebut(new Annee(2014));

        String actual = congePaye.getPeriodeDebutAsSwissValue();

        assertEquals("01.01.2014", actual);
    }

    @Test
    public void getPeriodeDebutAsSwissValue_GivenPeriodeOf2014_ShouldBe31_12_2014() {
        congePaye.setAnneeFin(new Annee(2014));

        String actual = congePaye.getPeriodeFinAsSwissValue();

        assertEquals("31.12.2014", actual);
    }

    @Test
    public void getSalaires_GivenSalaireDeclare1000AndSalaireNonDeclare100_ShouldBe1100() {
        congePaye.setSalaireDeclare(new Montant(1000));
        congePaye.setSalaireNonDeclare(new Montant(100));
        assertEquals(congePaye.getSalaires(), new Montant(1100));
    }

    @Test
    public void getMontantAVS_GivenEmptyCongePaye_ShouldBe0() {
        assertEquals(congePaye.getMontantAVS(), Montant.ZERO);
    }

    @Test
    public void getMontantBrut_GivenTotalSalaire1000AndTauxCP13_50ShouldBe135() {
        congePaye.setSalaireDeclare(new Montant(1000));
        congePaye.setSalaireNonDeclare(new Montant(0));
        congePaye.setTauxCP(new Taux(13.5));
        assertEquals(congePaye.getMontantBrut(), new Montant(135));
    }

    @Test
    public void getMontantAVS_GivenCongeWithTotalSalaireOf1000AndTauxAVSOf10_ShouldBe100() {
        congePaye.setSalaireDeclare(new Montant(1000));
        congePaye.setSalaireNonDeclare(new Montant(0));
        congePaye.setTauxCP(new Taux(13.5));

        TauxCongePaye tauxCongePaye = mock(TauxCongePaye.class);
        when(tauxCongePaye.getTypeAssurance()).thenReturn(TypeAssurance.COTISATION_AVS_AI);
        when(tauxCongePaye.getTaux()).thenReturn(new Taux(10));
        congePaye.setTauxCongePayes(Arrays.asList(tauxCongePaye));

        assertEquals(congePaye.getMontantAVS(), new Montant(13.5));
    }

    @Test(expected = NullPointerException.class)
    public void getPeriode_GivenEmptyCongePaye_ShouldThrowNullPointerException() {
        congePaye.getPeriode();
    }

    @Test
    public void getPeriode_GivenCongePayeWithDebutAnneeOf2014_ShouldReturnAPeriode() {
        congePaye.setAnneeDebut(new Annee(2014));
        assertEquals(congePaye.getPeriode(), new Periode("01.01.2014", null));
    }

    @Test
    public void getMontantBrut_GivenCongePayeWithMontantOf_ShouldBe899_45() {
        congePaye = spy(congePaye);
        congePaye.setSalaireDeclare(new Montant(4290.00));
        congePaye.setSalaireNonDeclare(new Montant(2000));
        doReturn(new Taux(14.3)).when(congePaye).getTauxCP();

        assertEquals(new Montant(899.45), congePaye.getMontantBrut());
    }
}
