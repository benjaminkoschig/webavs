package ch.globaz.pegasus.business.domaine.donneeFinanciere.dessaisissementFortune;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Taux;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class DessaisissementFortuneTest {
    private static DessaisissementFortune df = new DessaisissementFortune(new Montant(5000), new Montant(1000),
            BuilderDf.createDF());

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.DESSAISISSEMENT_FORTUNE, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testDessaisissementFortune() throws Exception {
        assertTrue(df.getMontant().isAnnuel());
        assertTrue(df.getDeduction().isAnnuel());
    }

    @Test
    public void testComputeFortuneBrut() throws Exception {
        assertEquals(Montant.newAnnuel(5000), df.computeFortuneBrut());
    }

    @Test
    public void testComputeFortune() throws Exception {
        assertEquals(Montant.newAnnuel(4000), df.computeFortune());
    }

    @Test
    public void testComputeInteret() throws Exception {
        assertEquals(Montant.newAnnuel(20), df.computeInteret(new Taux(0.5)));
    }

}
