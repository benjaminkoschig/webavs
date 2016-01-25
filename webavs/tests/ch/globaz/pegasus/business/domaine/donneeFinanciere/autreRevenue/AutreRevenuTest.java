package ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRevenue;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class AutreRevenuTest {

    private static AutreRevenu df = new AutreRevenu(new Montant(400), "test", BuilderDf.createDF());

    @Test
    public void testAutreRevenue() throws Exception {
        assertTrue(df.getMontant().isAnnuel());
    }

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.AUTRE_REVENU, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testComputeRevenuAnnuel() throws Exception {
        assertEquals(Montant.newAnnuel(400), df.computeRevenuAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuelBrut() throws Exception {
        assertEquals(Montant.newAnnuel(400), df.computeRevenuAnnuelBrut());
    }

}
