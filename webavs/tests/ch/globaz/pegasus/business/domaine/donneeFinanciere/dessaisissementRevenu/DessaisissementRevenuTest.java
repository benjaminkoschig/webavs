package ch.globaz.pegasus.business.domaine.donneeFinanciere.dessaisissementRevenu;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class DessaisissementRevenuTest {
    private static DessaisissementRevenu df = new DessaisissementRevenu(new Montant(5000), new Montant(1000),
            BuilderDf.createDF());

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.DESSAISISSEMENT_REVENU, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testDessaisissementRevenu() throws Exception {
        assertTrue(df.getMontant().isAnnuel());
        assertTrue(df.getDeduction().isAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuelBrut() throws Exception {
        assertEquals(Montant.newAnnuel(5000), df.computeRevenuAnnuelBrut());
    }

    @Test
    public void testComputeRevenuAnnuel() throws Exception {
        assertEquals(Montant.newAnnuel(4000), df.computeRevenuAnnuel());
    }

}
