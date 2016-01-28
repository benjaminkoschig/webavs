package ch.globaz.pegasus.business.domaine.donneeFinanciere.allocationFamilliale;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresBase;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class AllocationFamillialeTest extends DonneesFinancieresBase {
    private static AllocationFamilliale df = new AllocationFamilliale(M_50, build());

    @Test
    public void testComputeRevenuAnnuel() throws Exception {
        assertEquals(Montant.newAnnuel(600), df.computeRevenuAnnuel());
    }

    @Test
    public void testAllocationFamilliale() throws Exception {
        assertTrue(df.getMontant().isMensuel());
    }

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.ALLOCATION_FAMILIALLE, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testComputeRevenuAnnuelBrut() throws Exception {
        assertEquals(Montant.newAnnuel(600), df.computeRevenuAnnuelBrut());
    }

}
