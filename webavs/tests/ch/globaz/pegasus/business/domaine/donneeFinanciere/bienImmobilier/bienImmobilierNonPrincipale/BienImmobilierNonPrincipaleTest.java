package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonPrincipale;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.BienImmobilierHabitableType;

public class BienImmobilierNonPrincipaleTest {
    private BienImmobilierNonPrincipale df = new BienImmobilierNonPrincipale(new Montant(5000), new Montant(20),
            new Montant(30), new Montant(70), new Montant(100), new Montant(1000),
            BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.PROPRIETAIRE, BuilderDf.createDF());

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testBienImmobilierNonPrincipale() throws Exception {
        assertTrue(df.getDette().isAnnuel());
        assertTrue(df.getInteretHypothecaire().isAnnuel());
        assertTrue(df.getLoyerEncaisse().isAnnuel());
        assertTrue(df.getSousLocation().isAnnuel());
        assertTrue(df.getValeurLocative().isAnnuel());
        assertTrue(df.getValeurVenale().isAnnuel());
    }

    @Test
    public void testComputeDette() throws Exception {
        assertEquals(Montant.newAnnuel(500), df.computeDette());
    }

    @Test
    public void testComputeDetteUsusFruitier() throws Exception {
        BienImmobilierNonPrincipale df1 = new BienImmobilierNonPrincipale(new Montant(5000), new Montant(50),
                new Montant(40), new Montant(70), new Montant(100), new Montant(100000),
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.USUFRUITIER,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(500), df.computeDette());
        assertEquals(Montant.ZERO_ANNUEL, df1.computeDette());
    }

    @Test
    public void testComputeDetteDroitHabitation() throws Exception {
        BienImmobilierNonPrincipale df1 = new BienImmobilierNonPrincipale(new Montant(5000), new Montant(50),
                new Montant(40), new Montant(70), new Montant(100), new Montant(100000),
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.DROIT_HABITATION,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(500), df.computeDette());
        assertEquals(Montant.ZERO_ANNUEL, df1.computeDette());
    }

    @Test
    public void testComputeDetteNuproprietaire() throws Exception {
        BienImmobilierNonPrincipale df1 = new BienImmobilierNonPrincipale(new Montant(5000), new Montant(50),
                new Montant(40), new Montant(70), new Montant(100), new Montant(100000),
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.NU_PROPRIETAIRE,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(500), df.computeDette());
        assertEquals(Montant.ZERO_ANNUEL, df1.computeDette());
    }

    @Test
    public void testComputeDetteBrut() throws Exception {
        assertEquals(Montant.newAnnuel(1000), df.computeDetteBrut());
    }

    @Test
    public void testComputeFortune() throws Exception {
        assertEquals(Montant.newAnnuel(2500), df.computeFortune());
    }

    @Test
    public void testComputeFortuneUsufruit() throws Exception {
        BienImmobilierNonPrincipale df1 = new BienImmobilierNonPrincipale(new Montant(5000), new Montant(50),
                new Montant(40), new Montant(70), new Montant(100), new Montant(100000),
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.USUFRUITIER,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(0), df1.computeFortune());
    }

    @Test
    public void testComputeFortuneDroiHabitation() throws Exception {
        BienImmobilierNonPrincipale df1 = new BienImmobilierNonPrincipale(new Montant(5000), new Montant(50),
                new Montant(40), new Montant(70), new Montant(100), new Montant(100000),
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.DROIT_HABITATION,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(0), df1.computeFortune());
    }

    @Test
    public void testComputeFortuneUsufruitNuProprietaire() throws Exception {
        BienImmobilierNonPrincipale df1 = new BienImmobilierNonPrincipale(new Montant(5000), new Montant(50),
                new Montant(40), new Montant(70), new Montant(100), new Montant(100000),
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.NU_PROPRIETAIRE,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(0), df1.computeFortune());
    }

    @Test
    public void testComputeRevenuAnnuelUsufruit() throws Exception {
        BienImmobilierNonPrincipale df1 = new BienImmobilierNonPrincipale(new Montant(5000), new Montant(50),
                new Montant(30), new Montant(70), new Montant(100), new Montant(100000),
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.USUFRUITIER,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(100), df1.computeRevenuAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuelDroiHabitation() throws Exception {
        BienImmobilierNonPrincipale df1 = new BienImmobilierNonPrincipale(new Montant(5000), new Montant(50),
                new Montant(30), new Montant(70), new Montant(100), new Montant(100000),
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.DROIT_HABITATION,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(100), df1.computeRevenuAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuelNuProprietaire() throws Exception {
        BienImmobilierNonPrincipale df1 = new BienImmobilierNonPrincipale(new Montant(5000), new Montant(50),
                new Montant(30), new Montant(70), new Montant(100), new Montant(100000),
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.NU_PROPRIETAIRE,
                BuilderDf.createDF());
        assertEquals(Montant.ZERO_ANNUEL, df1.computeRevenuAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuelProprietaire() throws Exception {
        assertEquals(Montant.newAnnuel(100), df.computeRevenuAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuelBrut() throws Exception {
        assertEquals(Montant.newAnnuel(200), df.computeRevenuAnnuelBrut());
    }

    @Test
    public void testComputeValLocativePartProprieteProprietaire() throws Exception {
        assertEquals(Montant.newAnnuel(50), df.computeValLocativePartPropriete());
    }

    @Test
    public void testComputeValLocativePartProprieteUsufruitier() throws Exception {
        BienImmobilierNonPrincipale df1 = new BienImmobilierNonPrincipale(new Montant(5000), new Montant(50),
                new Montant(40), new Montant(70), new Montant(100), new Montant(100000),
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.USUFRUITIER,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(50), df1.computeValLocativePartPropriete());
    }

    @Test
    public void testComputeValLocativePartProprieteNuProprietaire() throws Exception {
        BienImmobilierNonPrincipale df1 = new BienImmobilierNonPrincipale(new Montant(5000), new Montant(50),
                new Montant(40), new Montant(70), new Montant(100), new Montant(100000),
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.NU_PROPRIETAIRE,
                BuilderDf.createDF());
        assertEquals(Montant.ZERO_ANNUEL, df1.computeValLocativePartPropriete());
    }

    @Test
    public void testComputeInteretProprietaire() throws Exception {
        assertEquals(Montant.newAnnuel(10), df.computeInteret());
    }

}
