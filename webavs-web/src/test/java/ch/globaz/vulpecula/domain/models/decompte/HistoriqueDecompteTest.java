package ch.globaz.vulpecula.domain.models.decompte;

import static ch.globaz.vulpecula.domain.models.decompte.EtatDecompte.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.common.Date;

public class HistoriqueDecompteTest {
    @Test
    public void compareTo() {
        HistoriqueDecompte h1 = create("01.01.2015", EtatDecompte.RECEPTIONNE);
        HistoriqueDecompte h2 = create("01.01.2016", EtatDecompte.VALIDE);
        assertEquals(h1.compareTo(h2), -1);
    }

    @Test
    public void compareTo1() {
        HistoriqueDecompte h1 = create("01.01.2016", EtatDecompte.RECEPTIONNE);
        HistoriqueDecompte h2 = create("01.01.2015", EtatDecompte.VALIDE);
        assertEquals(h1.compareTo(h2), 1);
    }

    @Test
    public void compareTo2() {
        HistoriqueDecompte h1 = create("01.01.2015", EtatDecompte.VALIDE);
        HistoriqueDecompte h2 = create("01.01.2015", EtatDecompte.VALIDE);
        assertEquals(h1.compareTo(h2), 0);
    }

    @Test
    public void compareTo3() {
        HistoriqueDecompte h1 = create("01.01.2015", EtatDecompte.RECEPTIONNE);
        HistoriqueDecompte h2 = create("01.01.2015", EtatDecompte.RECTIFIE);
        assertThat(h1.compareTo(h2), lessThanOrEqualTo(-1));
    }

    @Test
    public void compareTo4() {
        HistoriqueDecompte h1 = create("01.01.2015", EtatDecompte.RECTIFIE);
        HistoriqueDecompte h2 = create("01.01.2015", EtatDecompte.RECEPTIONNE);
        assertEquals(h1.compareTo(h2), 1);
    }

    @Test
    public void compareTo5() {
        HistoriqueDecompte h1 = create("01.01.2015", EtatDecompte.VALIDE);
        HistoriqueDecompte h2 = create("01.01.2015", EtatDecompte.RECEPTIONNE);
        assertEquals(h1.compareTo(h2), 1);
    }

    @Test
    public void compareTo6() {
        List<HistoriqueDecompte> hs = Arrays.asList(create("01.01.2015", RECEPTIONNE), create("01.01.2015", VALIDE));
        Collections.sort(hs, Collections.reverseOrder());

        assertThat(hs.get(0).getEtat(), is(VALIDE));
        assertThat(hs.get(1).getEtat(), is(RECEPTIONNE));

    }

    @Test
    public void compareTo7() {
        List<HistoriqueDecompte> hs = Arrays.asList(create("01.01.2015", VALIDE), create("01.01.2015", RECEPTIONNE));
        Collections.sort(hs, Collections.reverseOrder());

        assertThat(hs.get(0).getEtat(), is(VALIDE));
        assertThat(hs.get(1).getEtat(), is(RECEPTIONNE));

    }

    private HistoriqueDecompte create(String date, EtatDecompte etat) {
        HistoriqueDecompte historiqueDecompte = new HistoriqueDecompte();
        historiqueDecompte.setDate(new Date(date));
        historiqueDecompte.setEtat(etat);
        return historiqueDecompte;
    }
}
