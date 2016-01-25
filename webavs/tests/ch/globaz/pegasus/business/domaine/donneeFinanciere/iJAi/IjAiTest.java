package ch.globaz.pegasus.business.domaine.donneeFinanciere.iJAi;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class IjAiTest {
    private static IjAi df = new IjAi(new Montant(20), 5, BuilderDf.createDF());

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.IJAI, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testIjAi() throws Exception {
        assertTrue(df.getMontant().isJouranlier());
    }

    @Test
    public void testComputeRevenuAnnuel() throws Exception {
        assertEquals(Montant.newAnnuel(100), df.computeRevenuAnnuel());
    }

}
