package ch.globaz.pegasus.business.domaine.donneeFinanciere.loyer1;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class LoyerTest {
    private static Loyer df = new Loyer(new Montant(1000), new Montant(50), new Montant(30), new Montant(5),
            LoyerType.NET_AVEC_CHARGE, 1, false, false, BuilderDf.createDF());

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.LOYER, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testLoyer() throws Exception {
        assertTrue(df.getMontant().isMensuel());
        assertTrue(df.getCharge().isMensuel());
        assertTrue(df.getSousLocation().isMensuel());
        assertTrue(df.getTaxeJournalierePensionNonReconnue().isJouranlier());
    }

    @Test
    public void testComputeCharge() throws Exception {
        assertEquals(Montant.newAnnuel(600), df.computeCharge());
    }

    @Test
    public void testComputeChargeBrut() throws Exception {
        Loyer df1 = new Loyer(new Montant(1000), new Montant(50), new Montant(30), new Montant(5),
                LoyerType.BRUT_CHARGES_COMPRISES, 1, false, false, BuilderDf.createDF());
        assertEquals(Montant.ZERO_ANNUEL, df1.computeCharge());
    }

    @Test
    public void testComputeChargeForfaitaire() throws Exception {
        Loyer df1 = new Loyer(new Montant(1000), new Montant(50), new Montant(30), new Montant(5),
                LoyerType.NET_AVEC_CHARGE_FORFAITAIRES, 1, false, false, BuilderDf.createDF());
        assertEquals(Montant.ZERO_ANNUEL, df1.computeCharge());
    }

    @Test
    public void testComputeChargeSansCharge() throws Exception {
        Loyer df1 = new Loyer(new Montant(1000), new Montant(50), new Montant(30), new Montant(5),
                LoyerType.NET_SANS_CHARGE, 1, false, false, BuilderDf.createDF());
        assertEquals(Montant.ZERO_ANNUEL, df1.computeCharge());
    }

    @Test
    public void testComputeChargePensionNonRecounue() throws Exception {
        Loyer df1 = new Loyer(new Montant(1000), new Montant(50), new Montant(30), new Montant(5),
                LoyerType.PENSION_NON_RECONNUE, 1, false, false, BuilderDf.createDF());
        assertEquals(Montant.ZERO_ANNUEL, df1.computeCharge());
    }

    @Test
    public void testComputeChargeValeurLocativeChezProprietaire() throws Exception {
        Loyer df1 = new Loyer(new Montant(1000), new Montant(50), new Montant(30), new Montant(5),
                LoyerType.VALEUR_LOCATIVE_CHEZ_PROPRIETAIRE, 1, false, false, BuilderDf.createDF());
        assertEquals(Montant.ZERO_ANNUEL, df1.computeCharge());
    }

}
