package ch.globaz.pegasus.business.domaine.donneeFinanciere.autreFortuneMobiliere;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresBase;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;

public class AutreFortuneMobiliereTest extends DonneesFinancieresBase {
    private static AutreFortuneMobiliere df = new AutreFortuneMobiliere(M_1000, PROPRIETAIRE, PART_1_2,
            AutreFortuneMobiliereTypeDeFortune.AUTRES, build());

    @Test
    public void testAutreFortuneMobiliere() throws Exception {
        assertTrue(df.getMontant().isAnnuel());
    }

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.AUTRE_FORTUNE_MOBILIERE, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testcomputeFortuneProprietaire() throws Exception {
        assertEquals(Montant.newAnnuel(500), df.computeFortune());
    }

    @Test
    public void testcomputeFortuneUsufruitier() throws Exception {
        AutreFortuneMobiliere df = new AutreFortuneMobiliere(M_1000, ProprieteType.USUFRUITIER, PART_1_2,
                AutreFortuneMobiliereTypeDeFortune.AUTRES, build());
        assertEquals(Montant.newAnnuel(500), df.computeFortune());
    }

    @Test
    public void testcomputeFortuneNuProprietaire() throws Exception {
        AutreFortuneMobiliere df = new AutreFortuneMobiliere(M_1000, ProprieteType.NU_PROPRIETAIRE, PART_1_2,
                AutreFortuneMobiliereTypeDeFortune.AUTRES, build());
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
