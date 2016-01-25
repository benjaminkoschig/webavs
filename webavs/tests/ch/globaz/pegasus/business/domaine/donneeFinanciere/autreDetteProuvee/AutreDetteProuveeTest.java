package ch.globaz.pegasus.business.domaine.donneeFinanciere.autreDetteProuvee;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class AutreDetteProuveeTest {
    private static AutreDetteProuvee df = new AutreDetteProuvee(new Montant(50000), mock(DonneeFinanciere.class));

    @Test
    public void testAutreDetteProuvee() throws Exception {
        assertTrue(df.getMontant().isAnnuel());
    }

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.AUTRE_DETTE_PROUVEE, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testComputeDette() throws Exception {
        assertEquals(Montant.newAnnuel(50000), df.computeDette());
    }

    @Test
    public void testComputeDetteBrut() throws Exception {
        assertEquals(Montant.newAnnuel(50000), df.computeDette());
    }

}
