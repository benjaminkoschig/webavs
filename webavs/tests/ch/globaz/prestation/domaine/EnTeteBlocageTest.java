package ch.globaz.prestation.domaine;

import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

public class EnTeteBlocageTest {

    @Test
    public void testAjouterUnBlocagePourLeMois() throws Exception {
        EnTeteBlocage blocage = new EnTeteBlocage();

        try {
            blocage.ajouterUnBlocagePourLeMois(null, BigDecimal.ZERO);
            Assert.fail("doit lever une exception");
        } catch (NullPointerException ex) {
            // ok
        }
        try {
            blocage.ajouterUnBlocagePourLeMois("01.2010", null);
            Assert.fail("doit lever une exception");
        } catch (NullPointerException ex) {
            // ok
        }
        try {
            blocage.ajouterUnBlocagePourLeMois("", BigDecimal.ZERO);
            Assert.fail("doit lever une exception");
        } catch (IllegalArgumentException ex) {
            // ok
        }
        try {
            blocage.ajouterUnBlocagePourLeMois("ab.cdefg", BigDecimal.ZERO);
            Assert.fail("doit lever une exception");
        } catch (IllegalArgumentException ex) {
            // ok
        }
        try {
            blocage.ajouterUnBlocagePourLeMois("201401", BigDecimal.ZERO);
            Assert.fail("doit lever une exception");
        } catch (IllegalArgumentException ex) {
            // ok
        }

        blocage.ajouterUnBlocagePourLeMois("01.2014", BigDecimal.ONE);
        try {
            blocage.ajouterUnBlocagePourLeMois("01.2014", BigDecimal.ONE);
            Assert.fail("doit lever une exception");
        } catch (IllegalArgumentException ex) {
            // ok
        }
    }

    @Test
    public void testGetMontantTotalBloque() throws Exception {
        EnTeteBlocage blocage = new EnTeteBlocage();

        blocage.ajouterUnBlocagePourLeMois("01.2014", BigDecimal.ONE);

        Assert.assertEquals(BigDecimal.ONE, blocage.getMontantTotalBloque());

        blocage.ajouterUnBlocagePourLeMois("03.2014", BigDecimal.ONE);

        Assert.assertEquals(BigDecimal.valueOf(2), blocage.getMontantTotalBloque());
    }

    @Test
    public void testSetMontantDebloque() throws Exception {
        EnTeteBlocage blocage = new EnTeteBlocage();

        try {
            blocage.setMontantDebloque(null);
            Assert.fail("doit lever une exception");
        } catch (NullPointerException ex) {
            // ok
        }

        blocage.setMontantDebloque(BigDecimal.ONE);
    }

    @Test
    public void testUnBlocageEstEnCours() throws Exception {
        EnTeteBlocage blocage = new EnTeteBlocage();

        blocage.ajouterUnBlocagePourLeMois("01.2000", new BigDecimal("10.00"));
        blocage.ajouterUnBlocagePourLeMois("02.2000", new BigDecimal("10.00"));

        blocage.setMontantDebloque(new BigDecimal("10.00"));

        Assert.assertTrue(blocage.unBlocageEstEnCours());

        blocage.setMontantDebloque(new BigDecimal("20.00"));

        Assert.assertFalse(blocage.unBlocageEstEnCours());
    }
}
