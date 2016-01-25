package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierServantHbitationPrincipale;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.BienImmobilierHabitableType;

public class BienImmobilierServantHabitationPrincipaleTest {
    private BienImmobilierServantHabitationPrincipale df = new BienImmobilierServantHabitationPrincipale(new Montant(
            300000), new Montant(45), new Montant(20), new Montant(1500), new Montant(15), new Montant(100000), 2,
            BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.PROPRIETAIRE, BuilderDf.createDF());

    @Test
    public void testBienImmobilierServantHbitationPrincipale() throws Exception {
        assertTrue(df.getDette().isAnnuel());
        assertTrue(df.getInteretHypothecaire().isAnnuel());
        assertTrue(df.getLoyerEncaisse().isAnnuel());
        assertTrue(df.getSousLocation().isAnnuel());
        assertTrue(df.getValeurFiscale().isAnnuel());
        assertTrue(df.getValeurLocative().isAnnuel());
    }

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testComputeDette() throws Exception {
        assertEquals(Montant.newAnnuel(50000), df.computeDette());
    }

    @Test
    public void testComputeDetteDroitHabitation() throws Exception {
        BienImmobilierServantHabitationPrincipale df1 = new BienImmobilierServantHabitationPrincipale(new Montant(
                300000), new Montant(45), new Montant(20), new Montant(1500), new Montant(15), new Montant(100000), 2,
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.DROIT_HABITATION,
                BuilderDf.createDF());
        assertEquals(Montant.ZERO_ANNUEL, df1.computeDette());
    }

    @Test
    public void testComputeDetteNuProprietaire() throws Exception {
        BienImmobilierServantHabitationPrincipale df1 = new BienImmobilierServantHabitationPrincipale(new Montant(
                300000), new Montant(45), new Montant(20), new Montant(1500), new Montant(15), new Montant(100000), 2,
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.NU_PROPRIETAIRE,
                BuilderDf.createDF());
        assertEquals(Montant.ZERO_ANNUEL, df1.computeDette());
    }

    @Test
    public void testComputeDetteusufruitier() throws Exception {
        BienImmobilierServantHabitationPrincipale df1 = new BienImmobilierServantHabitationPrincipale(new Montant(
                300000), new Montant(45), new Montant(20), new Montant(1500), new Montant(15), new Montant(100000), 2,
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.USUFRUITIER,
                BuilderDf.createDF());
        assertEquals(Montant.ZERO_ANNUEL, df1.computeDette());
    }

    @Test
    public void testComputeFortuneBrute() throws Exception {
        assertEquals(Montant.newAnnuel(300000), df.computeFortuneBrut());
    }

    @Test
    public void testComputeDetteBrut() throws Exception {
        assertEquals(Montant.newAnnuel(100000), df.computeDetteBrut());
    }

    @Test
    public void testComputeFortuneProprietaire() throws Exception {
        assertEquals(Montant.newAnnuel(150000), df.computeFortune());
    }

    @Test
    public void testComputeFortuneProprietaireDroitHabitation() throws Exception {
        BienImmobilierServantHabitationPrincipale df1 = new BienImmobilierServantHabitationPrincipale(new Montant(
                300000), new Montant(45), new Montant(20), new Montant(1500), new Montant(15), new Montant(100000), 2,
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.DROIT_HABITATION,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(0), df1.computeFortune());
    }

    @Test
    public void testComputeFortuneProprietaireUsuFruitier() throws Exception {
        BienImmobilierServantHabitationPrincipale df1 = new BienImmobilierServantHabitationPrincipale(new Montant(
                300000), new Montant(45), new Montant(20), new Montant(1500), new Montant(15), new Montant(100000), 2,
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.USUFRUITIER,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(0), df1.computeFortune());
    }

    @Test
    public void testComputeFortuneProprietaireNuproprietaire() throws Exception {
        BienImmobilierServantHabitationPrincipale df1 = new BienImmobilierServantHabitationPrincipale(new Montant(
                300000), new Montant(45), new Montant(20), new Montant(1500), new Montant(15), new Montant(100000), 2,
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.NU_PROPRIETAIRE,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(0), df1.computeFortune());
    }

    @Test
    public void testComputeDepenseNuproprietaire() throws Exception {
        BienImmobilierServantHabitationPrincipale df1 = new BienImmobilierServantHabitationPrincipale(new Montant(
                300000), new Montant(10), new Montant(20), new Montant(1500), new Montant(15), new Montant(100000), 2,
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.NU_PROPRIETAIRE,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(0), df1.computeDepense());
    }

    @Test
    public void testComputeDepenseProprietaire() throws Exception {
        BienImmobilierServantHabitationPrincipale df1 = new BienImmobilierServantHabitationPrincipale(new Montant(
                300000), new Montant(10), new Montant(20), new Montant(1500), new Montant(15), new Montant(100000), 2,
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.PROPRIETAIRE,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(5), df1.computeDepense());
    }

    @Test
    public void testComputeDepenseUsufruitier() throws Exception {
        BienImmobilierServantHabitationPrincipale df1 = new BienImmobilierServantHabitationPrincipale(new Montant(
                300000), new Montant(10), new Montant(20), new Montant(1500), new Montant(15), new Montant(100000), 2,
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.USUFRUITIER,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(5), df1.computeDepense());
    }

    @Test
    public void testComputeDepenseDroitHabitation() throws Exception {
        BienImmobilierServantHabitationPrincipale df1 = new BienImmobilierServantHabitationPrincipale(new Montant(
                300000), new Montant(10), new Montant(20), new Montant(1500), new Montant(15), new Montant(100000), 2,
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.DROIT_HABITATION,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(5), df1.computeDepense());
    }

    @Test
    public void testComputeInteretProprietaire() throws Exception {
        assertEquals(Montant.newAnnuel(10), df.computeInteret());
    }

    @Test
    public void testComputeInteretUsufruitier() throws Exception {
        BienImmobilierServantHabitationPrincipale df1 = new BienImmobilierServantHabitationPrincipale(new Montant(
                300000), new Montant(10), new Montant(20), new Montant(1500), new Montant(15), new Montant(100000), 2,
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.USUFRUITIER,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(10), df1.computeInteret());
    }

    @Test
    public void testComputeInteretDroitHabitation() throws Exception {
        BienImmobilierServantHabitationPrincipale df1 = new BienImmobilierServantHabitationPrincipale(new Montant(
                300000), new Montant(10), new Montant(20), new Montant(1500), new Montant(15), new Montant(100000), 2,
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.DROIT_HABITATION,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(10), df1.computeInteret());
    }

    @Test
    public void testComputeInteretNuproprietaire() throws Exception {
        BienImmobilierServantHabitationPrincipale df1 = new BienImmobilierServantHabitationPrincipale(new Montant(
                300000), new Montant(10), new Montant(20), new Montant(1500), new Montant(15), new Montant(100000), 2,
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.NU_PROPRIETAIRE,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(10), df1.computeInteret());
    }

    @Test
    public void testComputeValLocativePartProprieteProprietaire() throws Exception {
        BienImmobilierServantHabitationPrincipale df1 = new BienImmobilierServantHabitationPrincipale(new Montant(
                300000), new Montant(10), new Montant(20), new Montant(1500), new Montant(15), new Montant(100000), 2,
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.PROPRIETAIRE,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(5), df1.computeValLocativePartPropriete());
    }

    @Test
    public void testComputeValLocativePartProprieteUsufruitier() throws Exception {
        BienImmobilierServantHabitationPrincipale df1 = new BienImmobilierServantHabitationPrincipale(new Montant(
                300000), new Montant(10), new Montant(20), new Montant(1500), new Montant(15), new Montant(100000), 2,
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.USUFRUITIER,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(5), df1.computeValLocativePartPropriete());
    }

    @Test
    public void testComputeValLocativePartProprieteNuProprietaire() throws Exception {
        BienImmobilierServantHabitationPrincipale df1 = new BienImmobilierServantHabitationPrincipale(new Montant(
                300000), new Montant(10), new Montant(20), new Montant(1500), new Montant(15), new Montant(100000), 2,
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.NU_PROPRIETAIRE,
                BuilderDf.createDF());
        assertEquals(Montant.ZERO_ANNUEL, df1.computeValLocativePartPropriete());
    }

    @Test
    public void testComputeRevenuAnnuelBrut() throws Exception {
        BienImmobilierServantHabitationPrincipale df1 = new BienImmobilierServantHabitationPrincipale(new Montant(
                300000), new Montant(100), new Montant(1500), new Montant(100), new Montant(100), new Montant(100000),
                2, BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.NU_PROPRIETAIRE,
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(300), df1.computeRevenuAnnuelBrut());
    }

}
