package ch.globaz.pegasus.business.domaine.donneeFinanciere.marchandiseStock;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;

public class MarchandiseStockTest {
    private final static MarchandiseStock df = new MarchandiseStock(new Montant(1000), ProprieteType.PROPRIETAIRE,
            new Part(1, 2), BuilderDf.createDF());

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.MARCHANDISE_STOCK, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testMarchandiseStock() throws Exception {
        assertTrue(df.getMontant().isAnnuel());
    }

    @Test
    public void testcomputeFortuneProrietaire() throws Exception {
        assertEquals(Montant.newAnnuel(500), df.computeFortune());
    }

    @Test
    public void testcomputeFortuneNuProprietaire() throws Exception {
        MarchandiseStock df = new MarchandiseStock(new Montant(1000), ProprieteType.NU_PROPRIETAIRE, new Part(1, 2),
                BuilderDf.createDF());
        assertEquals(Montant.ZERO_ANNUEL, df.computeFortune());
    }

    @Test
    public void testcomputeFortuneUsufruit() throws Exception {
        MarchandiseStock df = new MarchandiseStock(new Montant(1000), ProprieteType.USUFRUITIER, new Part(1, 2),
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(500), df.computeFortune());
    }

    @Test
    public void testComputeFortuneBrut() throws Exception {
        assertEquals(Montant.newAnnuel(1000), df.computeFortuneBrut());
    }

    @Test
    public void testComputeFortunePartPropriete() throws Exception {
        assertEquals(Montant.newAnnuel(500), df.computeFortunePartPropriete());
    }

}
