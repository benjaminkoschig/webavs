package ch.globaz.pegasus.business.domaine.donneeFinanciere.assuranceRenteViagere;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class AssuranceRenteViagereTest {
    private static AssuranceRenteViagere df = new AssuranceRenteViagere(new Montant(55), new Montant(5), new Montant(
            1000), false, false, BuilderDf.createDF());

    @Test
    public void testComputeRevenuAnnuel() throws Exception {
        assertEquals(Montant.newAnnuel(60), df.computeRevenuAnnuel());
    }

    @Test
    public void testComputeFortune() throws Exception {
        assertEquals(Montant.newAnnuel(1000), df.computeFortune());
    }

    @Test
    public void testAssuranceRenteViagere() throws Exception {
        assertTrue(df.getMontant().isAnnuel());
        assertTrue(df.getValeurDeRachat().isAnnuel());
        assertTrue(df.getExcedant().isAnnuel());
    }

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.ASSURANCE_RENTE_VIAGERE, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testComputeFortuneBrut() throws Exception {
        assertEquals(Montant.newAnnuel(1000), df.computeFortuneBrut());
    }

    @Test
    public void testComputeRevenuAnnuelBrut() throws Exception {
        assertEquals(Montant.newAnnuel(1000), df.computeFortuneBrut());
    }

}
