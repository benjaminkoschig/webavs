package ch.globaz.pegasus.business.domaine.donneeFinanciere.revenueActiviteLucrativeDependante;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class RevenuActiviteLucrativeDependanteTest {
    private static final RevenuActiviteLucrativeDependante df = new RevenuActiviteLucrativeDependante(
            new Montant(1000), new Montant(50), new Montant(5),new Montant(5), new Montant(15), BuilderDf.createDF());

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.REVENU_ACTIVITE_LUCRATIVE_DEPENDANTE, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testRevenueActiviteLucrativeDependante() throws Exception {
        assertTrue(df.getMontant().isAnnuel());
        assertTrue(df.getDeductionLpp().isAnnuel());
        assertTrue(df.getDeductionSociale().isAnnuel());
        assertTrue(df.getFraisDeGarde().isAnnuel());
        assertTrue(df.getFrais().isAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuel() throws Exception {
        assertEquals(Montant.newAnnuel(930), df.computeRevenuAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuelBrut() throws Exception {
        assertEquals(Montant.newAnnuel(1000), df.computeRevenuAnnuelBrut());
    }

}
