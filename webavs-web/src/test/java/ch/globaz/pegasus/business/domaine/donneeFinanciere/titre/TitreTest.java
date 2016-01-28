package ch.globaz.pegasus.business.domaine.donneeFinanciere.titre;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.common.domaine.Taux;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;

public class TitreTest {
    private static final Titre df = new Titre(new Montant(1000), new Montant(100), new Montant(20),
            ProprieteType.PROPRIETAIRE, new Part(1, 2), true, BuilderDf.createDF());

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.TITRE, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testTitre() throws Exception {
        assertTrue(df.getMontant().isAnnuel());
        assertTrue(df.getDroitGarde().isAnnuel());
        assertTrue(df.getRendement().isAnnuel());
    }

    @Test
    public void testcomputeFortune() throws Exception {
        assertEquals(Montant.newAnnuel(500), df.computeFortune());
    }

    @Test
    public void testComputeFortuneBrutProprietaire() throws Exception {
        assertEquals(Montant.newAnnuel(500), df.computeFortune());
    }

    @Test
    public void testComputeFortuneBrutUsufruitier() throws Exception {
        Titre df1 = new Titre(new Montant(1000), new Montant(100), new Montant(200), ProprieteType.USUFRUITIER,
                new Part(1, 2), false, BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(500), df1.computeFortune());
    }

    @Test
    public void testComputeFortuneBrutNuProprietaire() throws Exception {
        Titre df1 = new Titre(new Montant(1000), new Montant(100), new Montant(200), ProprieteType.NU_PROPRIETAIRE,
                new Part(1, 2), false, BuilderDf.createDF());
        assertEquals(Montant.ZERO_ANNUEL, df1.computeFortune());
    }

    @Test
    public void testComputeFortuneBrut() throws Exception {
        assertEquals(Montant.newAnnuel(1000), df.computeFortuneBrut());
    }

    @Test
    public void testComputeRevenuAnnuel() throws Exception {
        assertEquals(Montant.newAnnuel(20), df.computeRevenuAnnuel());
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
        Titre df1 = new Titre(new Montant(1000), new Montant(100), new Montant(0), ProprieteType.NU_PROPRIETAIRE,
                new Part(1, 2), true, BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(0), df1.computeInteret(new Taux(0.5)));
    }

    @Test
    public void testComputeInteretFalseWithInteret() throws Exception {
        Titre df1 = new Titre(new Montant(1000), new Montant(100), new Montant(10), ProprieteType.NU_PROPRIETAIRE,
                new Part(1, 2), false, BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(10), df1.computeInteret(new Taux(0.5)));
    }

    @Test
    public void testComputeInteretFalseWithOutInteret() throws Exception {
        Titre df1 = new Titre(new Montant(1000), new Montant(100), new Montant(0), ProprieteType.NU_PROPRIETAIRE,
                new Part(1, 2), false, BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(5), df1.computeInteret(new Taux(0.5)));
    }
}
