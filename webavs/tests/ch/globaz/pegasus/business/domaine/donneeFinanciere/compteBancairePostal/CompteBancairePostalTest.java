package ch.globaz.pegasus.business.domaine.donneeFinanciere.compteBancairePostal;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.common.domaine.Taux;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;

public class CompteBancairePostalTest {
    private CompteBancairePostal df = new CompteBancairePostal(new Montant(10000), new Montant(5), new Montant(20),
            new Part(1, 2), ProprieteType.PROPRIETAIRE, true, BuilderDf.createDF());

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.COMPTE_BANCAIRE_POSTAL, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testCompteBancairePostal() throws Exception {
        assertTrue(df.getMontant().isAnnuel());
        assertTrue(df.getFrais().isAnnuel());
        assertTrue(df.getInteret().isAnnuel());
    }

    @Test
    public void testcomputeFortuneProprietaire() throws Exception {
        assertEquals(Montant.newAnnuel(5000), df.computeFortune());
    }

    @Test
    public void testcomputeFortuneUsufruitier() throws Exception {
        CompteBancairePostal df = new CompteBancairePostal(new Montant(10000), new Montant(100), new Montant(200),
                new Part(1, 2), ProprieteType.USUFRUITIER, false, BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(5000), df.computeFortune());
    }

    @Test
    public void testcomputeFortuneNuProprietaire() throws Exception {
        CompteBancairePostal df = new CompteBancairePostal(new Montant(10000), new Montant(100), new Montant(200),
                new Part(1, 2), ProprieteType.NU_PROPRIETAIRE, false, BuilderDf.createDF());
        assertEquals(Montant.ZERO_ANNUEL, df.computeFortune());
    }

    @Test
    public void testComputeFortuneBrut() throws Exception {
        assertEquals(Montant.newAnnuel(10000), df.computeFortuneBrut());
    }

    @Test
    public void testComputeRevenuAnnuel() throws Exception {
        assertEquals(Montant.newAnnuel(15), df.computeRevenuAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuelBrut() throws Exception {
        assertEquals(Montant.newAnnuel(20), df.computeRevenuAnnuelBrut());
    }

    @Test
    public void testComputeFortunePartPropriete() throws Exception {
        assertEquals(Montant.newAnnuel(5000), df.computeFortune());
    }

    @Test
    public void testComputeInteret() throws Exception {
        assertEquals(Montant.newAnnuel(20), df.computeInteret(new Taux(0)));
    }

    @Test
    public void testComputeInteretTrueWihtOutInteret() throws Exception {
        CompteBancairePostal df1 = new CompteBancairePostal(new Montant(1000), new Montant(10), new Montant(0),
                new Part(1, 2), ProprieteType.NU_PROPRIETAIRE, true, BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(0), df1.computeInteret(new Taux(0.5)));
    }

    @Test
    public void testComputeInteretFalseWihtOutInteret() throws Exception {
        CompteBancairePostal df1 = new CompteBancairePostal(new Montant(1000), new Montant(10), new Montant(20),
                new Part(1, 2), ProprieteType.NU_PROPRIETAIRE, false, BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(20), df1.computeInteret(new Taux(0.5)));
    }

    @Test
    public void testComputeInteretFalseWihtInteret() throws Exception {
        CompteBancairePostal df1 = new CompteBancairePostal(new Montant(1000), new Montant(10), new Montant(0),
                new Part(1, 2), ProprieteType.NU_PROPRIETAIRE, false, BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(5), df1.computeInteret(new Taux(0.5)));
    }
}
