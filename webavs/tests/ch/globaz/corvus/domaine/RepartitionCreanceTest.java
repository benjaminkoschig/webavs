package ch.globaz.corvus.domaine;

import org.junit.Test;
import ch.globaz.corvus.TestUnitaireAvecGenerateurIDUnique;

public class RepartitionCreanceTest extends TestUnitaireAvecGenerateurIDUnique {

    @Test
    public void testSetMontantReparti() throws Exception {
        RenteAccordee renteAccordee = new RenteAccordee();
        renteAccordee.setId(genererUnIdUnique());

    }
}
