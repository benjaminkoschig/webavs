package ch.globaz.pegasus.business.domaine.donneeFinanciere.vehicule;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;

public class VehiculeTest {
    private static final Vehicule df = new Vehicule(new Montant(10000), ProprieteType.PROPRIETAIRE, new Part(1, 2),
            BuilderDf.createDF());

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.VEHICULE, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testVehicule() throws Exception {
        assertTrue(df.getMontant().isAnnuel());
    }

    @Test
    public void testcomputeFortuneProprietaire() throws Exception {
        assertEquals(Montant.newAnnuel(5000), df.computeFortune());
    }

    @Test
    public void testcomputeFortuneUsufruitier() throws Exception {
        Vehicule df = new Vehicule(new Montant(10000), ProprieteType.USUFRUITIER, new Part(1, 2), BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(5000), df.computeFortune());
    }

    @Test
    public void testcomputeFortuneNuProprietaire() throws Exception {
        Vehicule df = new Vehicule(new Montant(10000), ProprieteType.NU_PROPRIETAIRE, new Part(1, 2),
                BuilderDf.createDF());
        assertEquals(Montant.ZERO_ANNUEL, df.computeFortune());
    }

    @Test
    public void testComputeFortuneBrut() throws Exception {
        assertEquals(Montant.newAnnuel(10000), df.computeFortuneBrut());
    }

    @Test
    public void testComputeFortunePartPropriete() throws Exception {
        assertEquals(Montant.newAnnuel(5000), df.computeFortunePartPropriete());
    }

}
