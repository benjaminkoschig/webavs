package ch.globaz.pegasus.business.domaine.donneeFinanciere.capitalLpp;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.common.domaine.Taux;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;

public class CapitalLppTest {
    private static final CapitalLpp df = new CapitalLpp(new Montant(1000), new Montant(5), new Montant(20), new Part(1,
            2), ProprieteType.PROPRIETAIRE, true, BuilderDf.createDF());

    @Test
    public void testCapitalLpp() throws Exception {
        assertTrue(df.getMontant().isAnnuel());
        assertTrue(df.getFrais().isAnnuel());
        assertTrue(df.getInteret().isAnnuel());
    }

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.CAPITAL_LPP, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testcomputeFortuneProprietaire() throws Exception {
        assertEquals(Montant.newAnnuel(500), df.computeFortune());
    }

    @Test
    public void testcomputeFortuneUsufruitier() throws Exception {
        CapitalLpp df = new CapitalLpp(new Montant(1000), new Montant(100), new Montant(50), new Part(1, 2),
                ProprieteType.USUFRUITIER, false, BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(500), df.computeFortune());
    }

    @Test
    public void testcomputeFortuneNuProprietaire() throws Exception {
        CapitalLpp df = new CapitalLpp(new Montant(1000), new Montant(100), new Montant(50), new Part(1, 2),
                ProprieteType.NU_PROPRIETAIRE, false, BuilderDf.createDF());
        assertEquals(Montant.ZERO_ANNUEL, df.computeFortune());
    }

    @Test
    public void testComputeFortuneBrute() throws Exception {
        assertEquals(Montant.newAnnuel(1000), df.computeFortuneBrut());
    }

    @Test
    public void testComputeRevenuAnnuelBrut() throws Exception {
        assertEquals(Montant.newAnnuel(20), df.computeRevenuAnnuelBrut());
    }

    @Test
    public void testComputeFortunePartPropriete() throws Exception {
        assertEquals(Montant.newAnnuel(500), df.computeFortunePartPropriete());
    }

    @Test
    public void testComputeInteret() throws Exception {
        assertEquals(Montant.newAnnuel(20), df.computeInteret(new Taux(0.5)));
    }

    @Test
    public void testComputeInteretTrueWihtOutInteret() throws Exception {
        CapitalLpp df1 = new CapitalLpp(new Montant(1000), new Montant(100), new Montant(0), new Part(1, 2),
                ProprieteType.NU_PROPRIETAIRE, true, BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(0), df1.computeInteret(new Taux(0.5)));
    }

    @Test
    public void testComputeInteretFalseWihtInteret() throws Exception {
        CapitalLpp df1 = new CapitalLpp(new Montant(1000), new Montant(100), new Montant(10), new Part(1, 2),
                ProprieteType.NU_PROPRIETAIRE, false, BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(10), df1.computeInteret(new Taux(0.5)));
    }

    @Test
    public void testComputeInteretFalseWihtOutInteret() throws Exception {
        CapitalLpp df1 = new CapitalLpp(new Montant(1000), new Montant(100), new Montant(0), new Part(1, 2),
                ProprieteType.NU_PROPRIETAIRE, false, BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(5), df1.computeInteret(new Taux(0.5)));
    }

}
