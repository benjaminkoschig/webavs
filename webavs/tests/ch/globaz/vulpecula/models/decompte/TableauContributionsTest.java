package ch.globaz.vulpecula.models.decompte;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import ch.globaz.vulpecula.business.services.decompte.DecompteSalaireService;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;

public class TableauContributionsTest {
    private DecompteSalaireService decompteSalaireService;

    @Before
    public void setUp() {
        decompteSalaireService = mock(DecompteSalaireService.class);
        when(decompteSalaireService.isPosteTravailRentier(Matchers.any(DecompteSalaire.class))).thenReturn(true);
    }

    @Test
    public void test1() {
        TableauContributions tab = new TableauContributions(new Decompte());
        assertEquals(null, tab.getEntreesAF());
    }

    @Test
    public void test2() {
        TableauContributions tab = new TableauContributions(new Decompte());
        assertEquals(null, tab.getEntreesAVS());
    }

    @Test
    public void test3() {
        TableauContributions tab = new TableauContributions(new Decompte());
        assertEquals(null, tab.getEntreesAC());
    }

    @Test
    public void test4() {
        TableauContributions tab = new TableauContributions(new Decompte());
        assertEquals(null, tab.getEntreesAC2());
    }

    private Map<Taux, Montant> createTaux(Taux[] taux, Montant[] montant) {
        Map<Taux, Montant> map = new HashMap<Taux, Montant>();
        for (int i = 0; i < taux.length; i++) {
            map.put(taux[i], montant[i]);
        }
        return map;
    }
}
