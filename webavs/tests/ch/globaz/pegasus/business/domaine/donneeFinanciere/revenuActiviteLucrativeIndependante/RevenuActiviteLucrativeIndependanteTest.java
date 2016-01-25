package ch.globaz.pegasus.business.domaine.donneeFinanciere.revenuActiviteLucrativeIndependante;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class RevenuActiviteLucrativeIndependanteTest {
    private static final RevenuActiviteLucrativeIndependante df = new RevenuActiviteLucrativeIndependante(new Montant(
            1000), RevenuActiviteLucrativeIndependanteGenreRevenu.GENRE_REVENU_ACT_LUCR_AGRICOLE_FORESTIER,
            BuilderDf.createDF());

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.REVENU_ACTIVITE_LUCRATIVE_INDEPENDANTE, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testRevenuActiviteLucrativeIndependante() throws Exception {
        assertTrue(df.getMontant().isAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuel() throws Exception {
        assertEquals(Montant.newAnnuel(1000), df.computeRevenuAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuelBrut() throws Exception {
        assertEquals(Montant.newAnnuel(1000), df.computeRevenuAnnuelBrut());
    }
}
