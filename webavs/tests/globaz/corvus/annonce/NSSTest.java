package globaz.corvus.annonce;

import static org.junit.Assert.*;
import org.junit.Test;

public class NSSTest {

    @Test
    public void convertFormattedNSS() {
        String nss1 = "756.1254.3954.21";
        try {
            RENSS nss = RENSS.convertFormattedNSS(nss1);
            assertTrue(nss1.equals(nss.getFormatedNSS()));
        } catch (REIllegalNSSFormatException e) {
            assertTrue(false);
        }

        String nss2 = "756.0000.0000.00";
        try {
            RENSS nss = RENSS.convertFormattedNSS(nss2);
            assertTrue(nss2.equals(nss.getFormatedNSS()));
        } catch (REIllegalNSSFormatException e) {
            assertTrue(false);
        }

        String nss3 = "756.9999.9999.99";
        try {
            RENSS nss = RENSS.convertFormattedNSS(nss3);
            assertTrue(nss3.equals(nss.getFormatedNSS()));
        } catch (REIllegalNSSFormatException e) {
            assertTrue(false);
        }

        try {
            RENSS.convertFormattedNSS("756.2358.7642.1");
            assertTrue(false);
        } catch (REIllegalNSSFormatException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testValeurMinimum() {

        // Test des valeurs minimum
        try {
            new RENSS(0, 0, 0);
            assertTrue(true);
        } catch (IllegalArgumentException e) {
            assertTrue("Aucune exception new doit être lancée avec les valeurs [0, 0, 0]", false);
        }

        // Test d'une valeur négative en 1ère position
        try {
            new RENSS(-1, 2789, 63);
            assertTrue("Une exception doit être lancée car la valeur [-1] n'est pas authorisée en 1ère position", false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        // Test d'une valeur négative en 2ème position
        try {
            new RENSS(2789, -1, 63);
            assertTrue("Une exception doit être lancée car la valeur [-1] n'est pas authorisée en 2ème position", false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        // Test d'une valeur négative en 3ème position
        try {
            new RENSS(1254, 2789, -1);
            assertTrue("Une exception doit être lancée car la valeur [-1] n'est pas authorisée en 3ème position", false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testValeurMaximum() {

        // Test des valeurs minimum
        try {
            new RENSS(9999, 9999, 99);
            assertTrue(true);
        } catch (IllegalArgumentException e) {
            assertTrue("Aucune exception ne doit être lancée avec les valeurs [9999, 9999, 99]", false);
        }

        // Test d'une valeur trop grande en 1ère position
        try {
            new RENSS(10000, 2789, 63);
            assertTrue("Une exception doit être lancée car la valeur [10000] n'est pas authorisée en 1ère position",
                    false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        // Test d'une valeur trop grande en 2ème position
        try {
            new RENSS(6854, 10000, 63);
            assertTrue("Une exception doit être lancée car la valeur [10000] n'est pas authorisée en 2ème position",
                    false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        // Test d'une valeur trop grande en 3ème position
        try {
            new RENSS(8642, 2789, 100);
            assertTrue("Une exception doit être lancée car la valeur [100] n'est pas authorisée en 3ème position",
                    false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

    }

    @Test
    public void testCreationNSSBlanc() {
        try {
            assertTrue(RENSS.convertFormattedNSS(RENSS.EMPTY_FORMATED_NSS_WITH_PREFIX) != null);
            assertTrue(true);
        } catch (REIllegalNSSFormatException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testFormattage() {

        try {
            RENSS nss = new RENSS(0, 0, 0);
            assertTrue("7560000000000".equals(nss.getUnformatedNSS()));
            assertTrue("756.0000.0000.00".equals(nss.getFormatedNSS()));
        } catch (IllegalArgumentException e) {
            assertTrue("Aucune exception new doit être lancée avec les valeurs [0, 0, 0]", false);
        }

        try {
            RENSS nss = new RENSS(675, 9642, 12);
            assertTrue("7560675964212".equals(nss.getUnformatedNSS()));
            assertTrue("756.0675.9642.12".equals(nss.getFormatedNSS()));
        } catch (IllegalArgumentException e) {
            assertTrue("Aucune exception ne doit être lancée avec les valeurs [9999, 9999, 99]", false);
        }

        try {
            RENSS nss = new RENSS(9999, 9999, 99);
            assertTrue("7569999999999".equals(nss.getUnformatedNSS()));
            assertTrue("756.9999.9999.99".equals(nss.getFormatedNSS()));
        } catch (IllegalArgumentException e) {
            assertTrue("Aucune exception ne doit être lancée avec les valeurs [9999, 9999, 99]", false);
        }
    }
}
