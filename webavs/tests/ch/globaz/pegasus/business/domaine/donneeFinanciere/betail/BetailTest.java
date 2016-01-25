package ch.globaz.pegasus.business.domaine.donneeFinanciere.betail;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresBase;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;

public class BetailTest extends DonneesFinancieresBase {
    private static Betail df = new Betail(M_1000, PROPRIETAIRE, PART_1_2, build());

    @Test
    public void testBetail() throws Exception {
        assertTrue(df.getMontant().isAnnuel());
    }

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.BETAIL, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testComputeFortuneProprietaire() throws Exception {
        assertEquals(Montant.newAnnuel(500), df.computeFortune());
    }

    @Test
    public void testComputeFortuneUsufruitier() throws Exception {
        Betail df = new Betail(M_1000, ProprieteType.USUFRUITIER, PART_1_2, build());
        assertEquals(Montant.newAnnuel(500), df.computeFortune());
    }

    @Test
    public void testComputeFortuneNuProprietaire() throws Exception {
        Betail df = new Betail(M_1000, ProprieteType.NU_PROPRIETAIRE, PART_1_2, build());
        assertEquals(Montant.ZERO_ANNUEL, df.computeFortune());
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
