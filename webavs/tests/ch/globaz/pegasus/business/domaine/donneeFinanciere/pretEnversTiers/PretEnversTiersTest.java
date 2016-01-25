package ch.globaz.pegasus.business.domaine.donneeFinanciere.pretEnversTiers;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.common.domaine.Taux;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;

public class PretEnversTiersTest {
    public final static PretEnversTiers df = new PretEnversTiers(new Montant(5000), new Montant(50), new Part(1, 2),
            ProprieteType.PROPRIETAIRE, true, BuilderDf.createDF());

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.PRET_ENVERS_TIERS, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testPretEnversTiers() throws Exception {
        assertTrue(df.getMontant().isAnnuel());
        assertTrue(df.getInteret().isAnnuel());
    }

    @Test
    public void testcomputeFortuneProrietaire() throws Exception {
        assertEquals(Montant.newAnnuel(2500), df.computeFortune());
    }

    @Test
    public void testcomputeFortuneUsufruitier() throws Exception {
        PretEnversTiers df = new PretEnversTiers(new Montant(5000), new Montant(50), new Part(1, 2),
                ProprieteType.USUFRUITIER, false, BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(2500), df.computeFortune());
    }

    @Test
    public void testcomputeFortuneNuProrietaire() throws Exception {
        PretEnversTiers df = new PretEnversTiers(new Montant(5000), new Montant(50), new Part(1, 2),
                ProprieteType.NU_PROPRIETAIRE, false, BuilderDf.createDF());
        assertEquals(Montant.ZERO_ANNUEL, df.computeFortune());
    }

    @Test
    public void testComputeFortuneBrut() throws Exception {
        assertEquals(Montant.newAnnuel(5000), df.computeFortuneBrut());
    }

    @Test
    public void testComputeRevenuAnnuel() throws Exception {
        assertEquals(Montant.newAnnuel(50), df.computeRevenuAnnuel());
    }

    @Test
    public void testComputeFortunePartPropriete() throws Exception {
        assertEquals(Montant.newAnnuel(2500), df.computeFortunePartPropriete());
    }

    @Test
    public void testComputeInteret() throws Exception {
        assertEquals(Montant.newAnnuel(50), df.computeInteret(new Taux(0.5)));
    }

    @Test
    public void testComputeInteretTrueWihtOutInteret() throws Exception {
        PretEnversTiers df1 = new PretEnversTiers(new Montant(1000), new Montant(0), new Part(1, 2),
                ProprieteType.NU_PROPRIETAIRE, true, BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(0), df1.computeInteret(new Taux(0.5)));
    }

    @Test
    public void testComputeInteretFalseWithInteret() throws Exception {
        PretEnversTiers df1 = new PretEnversTiers(new Montant(1000), new Montant(10), new Part(1, 2),
                ProprieteType.NU_PROPRIETAIRE, false, BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(10), df1.computeInteret(new Taux(0.5)));
    }

    @Test
    public void testComputeInteretFalseWithIOutInteret() throws Exception {
        PretEnversTiers df1 = new PretEnversTiers(new Montant(1000), new Montant(0), new Part(1, 2),
                ProprieteType.NU_PROPRIETAIRE, false, BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(5), df1.computeInteret(new Taux(0.5)));
    }
}
