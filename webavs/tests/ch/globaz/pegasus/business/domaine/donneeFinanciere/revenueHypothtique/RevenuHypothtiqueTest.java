package ch.globaz.pegasus.business.domaine.donneeFinanciere.revenueHypothtique;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class RevenuHypothtiqueTest {
    private static final RevenuHypothtique df = new RevenuHypothtique(Montant.ZERO_ANNUEL, new Montant(2000),
            new Montant(200), new Montant(50), new Montant(30), RevenuHypothtiqueMotif.MOTIF_REVENU_HYPO_14A_OPC,
            BuilderDf.createDF());

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.REVENU_HYPOTHETIQUE, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testRevenueHypothtique() throws Exception {
        assertTrue(df.getRevenuBrut().isAnnuel());
        assertTrue(df.getRevenuNet().isAnnuel());
        assertTrue(df.getFraisGarde().isAnnuel());
        assertTrue(df.getDeductionLpp().isAnnuel());
        assertTrue(df.getDeductionSocial().isAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuelNetNet() throws Exception {
        assertEquals(Montant.newAnnuel(1720), df.computeRevenuAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuelBrut() throws Exception {
        RevenuHypothtique df = new RevenuHypothtique(new Montant(2000), Montant.ZERO_ANNUEL, new Montant(200),
                new Montant(50), new Montant(30), RevenuHypothtiqueMotif.MOTIF_REVENU_HYPO_14A_OPC,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(2000), df.computeRevenuAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuelBrutNet() throws Exception {
        assertEquals(Montant.newAnnuel(2000), df.computeRevenuAnnuelBrut());
    }

    @Test
    public void testComputeRevenuAnnuelBrutBrut() throws Exception {
        RevenuHypothtique df = new RevenuHypothtique(new Montant(2000), Montant.ZERO_ANNUEL, new Montant(200),
                new Montant(50), new Montant(30), RevenuHypothtiqueMotif.MOTIF_REVENU_HYPO_14A_OPC,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(2000), df.computeRevenuAnnuelBrut());
    }

}
