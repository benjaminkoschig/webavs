package ch.globaz.pegasus.business.domaine.donneeFinanciere.loyer1;

import static org.junit.Assert.*;
import org.junit.Test;

public class LoyerTypeTest {

    @Test
    public void testIsNetAvecChargeTrue() throws Exception {
        assertTrue(LoyerType.NET_AVEC_CHARGE.isNetAvecCharge());
    }

    @Test
    public void testIsNetAvecChargeForfaitairesTrue() throws Exception {
        assertTrue(LoyerType.NET_AVEC_CHARGE_FORFAITAIRES.isNetAvecChargeForfaitaires());
    }

    @Test
    public void testIsNetSansChargeTrue() throws Exception {
        assertTrue(LoyerType.NET_SANS_CHARGE.isNetSansCharge());
    }

    @Test
    public void testIsBrutChargesComprisesTrue() throws Exception {
        assertTrue(LoyerType.BRUT_CHARGES_COMPRISES.isBrutChargesComprises());
    }

    @Test
    public void testIsValeurLocativeChezProprietaireTrue() throws Exception {
        assertTrue(LoyerType.VALEUR_LOCATIVE_CHEZ_PROPRIETAIRE.isValeurLocativeChezProprietaire());
    }

    @Test
    public void testIsPensionsNonRecounnueTrue() throws Exception {
        assertTrue(LoyerType.PENSION_NON_RECONNUE.isPensionsNonRecounnue());
    }

    @Test
    public void testIsNetAvecChargeFalse() throws Exception {
        assertFalse(LoyerType.NET_AVEC_CHARGE_FORFAITAIRES.isNetAvecCharge());
    }

    @Test
    public void testIsNetAvecChargeForfaitairesFalse() throws Exception {
        assertFalse(LoyerType.NET_AVEC_CHARGE.isNetAvecChargeForfaitaires());
    }

    @Test
    public void testIsNetSansChargeFalse() throws Exception {
        assertFalse(LoyerType.NET_AVEC_CHARGE.isNetSansCharge());
    }

    @Test
    public void testIsBrutChargesComprisesFalse() throws Exception {
        assertFalse(LoyerType.NET_AVEC_CHARGE.isBrutChargesComprises());
    }

    @Test
    public void testIsValeurLocativeChezProprietaireFalse() throws Exception {
        assertFalse(LoyerType.NET_AVEC_CHARGE.isValeurLocativeChezProprietaire());
    }

    @Test
    public void testIsPensionsNonRecounnueFalse() throws Exception {
        assertFalse(LoyerType.NET_AVEC_CHARGE.isPensionsNonRecounnue());
    }

}
