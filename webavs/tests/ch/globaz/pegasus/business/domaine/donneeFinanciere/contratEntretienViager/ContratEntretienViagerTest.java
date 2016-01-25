package ch.globaz.pegasus.business.domaine.donneeFinanciere.contratEntretienViager;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class ContratEntretienViagerTest {
    private ContratEntretienViager df = new ContratEntretienViager(new Montant(1000), BuilderDf.createDF());

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.CONTRAT_ENTRETIEN_VIAGER, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testContratEntretienViager() throws Exception {
        assertTrue(df.getMontant().isAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuel() throws Exception {
        assertEquals(Montant.newAnnuel(1000), df.computeRevenuAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuelBrut() throws Exception {
        assertEquals(Montant.newAnnuel(1000), df.computeRevenuAnnuel());
    }

}
