package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonHabitable;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonHabitable.BienImmobilierNonHabitable;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonHabitable.BienImmobilierNonHabitableType;

public class BienImmobilierNonHabitableTest {
    private static final BienImmobilierNonHabitable df = new BienImmobilierNonHabitable(new Montant(10), new Montant(
            6000), new Montant(20), new Montant(1000), BienImmobilierNonHabitableType.AISANCE, new Part(1, 2),
            ProprieteType.PROPRIETAIRE, BuilderDf.createDF());

    @Test
    public void testBienImmobilierNonHabitable() throws Exception {
        assertTrue(df.getDette().isAnnuel());
        assertTrue(df.getInteretHypothecaire().isAnnuel());
        assertTrue(df.getRendement().isAnnuel());
        assertTrue(df.getValeurVenale().isAnnuel());
    }

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.BIEN_IMMOBILIER_NON_HABITABLE, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testComputeFortune() throws Exception {
        assertEquals(Montant.newAnnuel(3000), df.computeFortune());
    }

    @Test
    public void testComputeFortuneNuProrietaire() throws Exception {
        BienImmobilierNonHabitable df1 = new BienImmobilierNonHabitable(new Montant(10), new Montant(6000),
                new Montant(20), new Montant(1000), BienImmobilierNonHabitableType.AISANCE, new Part(1, 2),
                ProprieteType.NU_PROPRIETAIRE, BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(0), df1.computeFortune());
    }

    @Test
    public void testComputeRevenuAnnuel() throws Exception {
        assertEquals(Montant.newAnnuel(5), df.computeRevenuAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuelNuprorietaire() throws Exception {
        BienImmobilierNonHabitable df1 = new BienImmobilierNonHabitable(new Montant(10), new Montant(6000),
                new Montant(20), new Montant(1000), BienImmobilierNonHabitableType.AISANCE, new Part(1, 2),
                ProprieteType.NU_PROPRIETAIRE, BuilderDf.createDF());
        assertEquals(Montant.ZERO_ANNUEL, df1.computeRevenuAnnuel());
    }

    @Test
    public void testComputeDepenseProprietaire() throws Exception {
        assertEquals(Montant.newAnnuel(20), df.computeDepense());
    }

    @Test
    public void testComputeDepenseNuProprietaire() throws Exception {
        BienImmobilierNonHabitable df1 = new BienImmobilierNonHabitable(new Montant(10), new Montant(6000),
                new Montant(20), new Montant(1000), BienImmobilierNonHabitableType.AISANCE, new Part(1, 2),
                ProprieteType.NU_PROPRIETAIRE, BuilderDf.createDF());
        assertEquals(Montant.ZERO_ANNUEL, df1.computeDepense());
    }

    @Test
    public void testComputeDette() throws Exception {
        assertEquals(Montant.newAnnuel(500), df.computeDette());
    }

    @Test
    public void testComputeDetteUsufruit() throws Exception {
        BienImmobilierNonHabitable df1 = new BienImmobilierNonHabitable(new Montant(10), new Montant(6000),
                new Montant(20), new Montant(1000), BienImmobilierNonHabitableType.AISANCE, new Part(1, 2),
                ProprieteType.USUFRUITIER, BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(500), df.computeDette());
        assertEquals(Montant.ZERO_ANNUEL, df1.computeDette());
    }

    @Test
    public void testComputeFortuneBrut() throws Exception {
        assertEquals(Montant.newAnnuel(6000), df.computeFortuneBrut());
    }

    @Test
    public void testComputeDetteBrut() throws Exception {
        assertEquals(Montant.newAnnuel(1000), df.computeDetteBrut());
    }

    @Test
    public void testComputeRevenuAnnuelBrut() throws Exception {
        assertEquals(Montant.newAnnuel(10), df.computeRevenuAnnuelBrut());
    }

    @Test
    public void testComputeInteretProprietaire() throws Exception {
        assertEquals(Montant.newAnnuel(10), df.computeInteret());
    }

    @Test
    public void testComputeRendementPartProprieteProprietaire() throws Exception {
        BienImmobilierNonHabitable df1 = new BienImmobilierNonHabitable(new Montant(10), new Montant(6000),
                new Montant(55), new Montant(1000), BienImmobilierNonHabitableType.AISANCE, new Part(1, 2),
                ProprieteType.PROPRIETAIRE, BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(5), df1.computeRendementPartPropriete());
    }

    @Test
    public void testComputeRendementPartProprieteUsufruitier() throws Exception {
        BienImmobilierNonHabitable df1 = new BienImmobilierNonHabitable(new Montant(10), new Montant(6000),
                new Montant(55), new Montant(1000), BienImmobilierNonHabitableType.AISANCE, new Part(1, 2),
                ProprieteType.USUFRUITIER, BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(5), df1.computeRendementPartPropriete());
    }

    @Test
    public void testComputeRendementPartProprieteNuProprietaire() throws Exception {
        BienImmobilierNonHabitable df1 = new BienImmobilierNonHabitable(new Montant(10), new Montant(6000),
                new Montant(55), new Montant(1000), BienImmobilierNonHabitableType.AISANCE, new Part(1, 2),
                ProprieteType.NU_PROPRIETAIRE, BuilderDf.createDF());
        assertEquals(Montant.ZERO_ANNUEL, df1.computeRendementPartPropriete());
    }

}
