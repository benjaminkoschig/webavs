package ch.globaz.pegasus.business.domaine.donneeFinanciere.cotisationPsal;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class CotisationPsalTest {
    private static CotisationPsal df = new CotisationPsal(new Montant(500), BuilderDf.createDF());

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.COTISATION_PSAL, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testCotisationPsal() throws Exception {
        assertTrue(df.getMontant().isAnnuel());
    }

    @Test
    public void testComputeDepense() throws Exception {
        assertEquals(Montant.newAnnuel(500), df.computeDepense());
    }
}
