package ch.globaz.pegasus.business.domaine.donneeFinanciere.numeraire;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.common.domaine.Taux;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;

public class NumeraireTest {
    private static Numeraire df = new Numeraire(new Montant(1000), new Montant(20), new Part(1, 2),
            ProprieteType.PROPRIETAIRE, true, BuilderDf.createDF());

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.NUMERAIRE, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testNumeraire() throws Exception {
        assertTrue(df.getMontant().isAnnuel());
        assertTrue(df.getInteret().isAnnuel());
    }

    @Test
    public void testcomputeFortuneProrietaire() throws Exception {
        assertEquals(Montant.newAnnuel(500), df.computeFortune());
    }

    @Test
    public void testcomputeFortuneNuProprietaire() throws Exception {
        Numeraire df = new Numeraire(new Montant(1000), new Montant(50), new Part(1, 2), ProprieteType.NU_PROPRIETAIRE,
                false, BuilderDf.createDfRequerant());
        assertEquals(Montant.ZERO_ANNUEL, df.computeFortune());
    }

    @Test
    public void testcomputeFortuneUsufruit() throws Exception {
        Numeraire df = new Numeraire(new Montant(1000), new Montant(50), new Part(1, 2), ProprieteType.USUFRUITIER,
                false, BuilderDf.createDfRequerant());
        assertEquals(Montant.newAnnuel(500), df.computeFortune());
    }

    @Test
    public void testComputeFortuneBrut() throws Exception {
        assertEquals(Montant.newAnnuel(1000), df.computeFortuneBrut());
    }

    @Test
    public void testComputeFortunePartPropriete() throws Exception {
        assertEquals(Montant.newAnnuel(500), df.computeFortunePartPropriete());
    }

    @Test
    public void testComputeInteret() throws Exception {
        assertEquals(Montant.newAnnuel(20), df.computeInteret(new Taux(0)));
    }

    @Test
    public void testComputeInteretTrueWihtOutInteret() throws Exception {
        Numeraire df = new Numeraire(new Montant(1000), new Montant(0), new Part(1, 2), ProprieteType.USUFRUITIER,
                true, BuilderDf.createDfRequerant());
        assertEquals(Montant.newAnnuel(0), df.computeInteret(new Taux(0.5)));
    }

    @Test
    public void testComputeInteretFalseWihtInteret() throws Exception {
        Numeraire df = new Numeraire(new Montant(1000), new Montant(10), new Part(1, 2), ProprieteType.USUFRUITIER,
                false, BuilderDf.createDfRequerant());
        assertEquals(Montant.newAnnuel(10), df.computeInteret(new Taux(0.5)));
    }

    @Test
    public void testComputeInteretFalseWihtOutInteret() throws Exception {
        Numeraire df = new Numeraire(new Montant(1000), new Montant(0), new Part(1, 2), ProprieteType.USUFRUITIER,
                false, BuilderDf.createDfRequerant());
        assertEquals(Montant.newAnnuel(5), df.computeInteret(new Taux(0.5)));
    }
}
