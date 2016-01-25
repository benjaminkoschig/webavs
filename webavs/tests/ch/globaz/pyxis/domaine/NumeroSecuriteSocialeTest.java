package ch.globaz.pyxis.domaine;

import org.junit.Assert;
import org.junit.Test;

public class NumeroSecuriteSocialeTest {

    @Test
    public void testNSSavecLettres() {
        try {
            new NumeroSecuriteSociale("756a1234a5678a97");
            Assert.fail();
        } catch (Exception ex) {
            // ok
        }
    }

    @Test
    public void testNSSmalforme() {
        try {
            new NumeroSecuriteSociale("7561234567897");
            Assert.fail();
        } catch (Exception ex) {
            // ok
        }
        try {
            new NumeroSecuriteSociale("756.1234.567897");
            Assert.fail();
        } catch (Exception ex) {
            // ok
        }
        try {
            new NumeroSecuriteSociale("756.1234.56789.7");
            Assert.fail();
        } catch (Exception ex) {
            // ok
        }
        try {
            new NumeroSecuriteSociale(".756.1234.5678.97");
            Assert.fail();
        } catch (Exception ex) {
            // ok
        }
    }

    @Test
    public void testNSSmauvaisChiffresDepart() {
        try {
            new NumeroSecuriteSociale("755.1234.5678.97");
            Assert.fail();
        } catch (Exception ex) {
            // ok
        }
        try {
            new NumeroSecuriteSociale("757.1234.5678.97");
            Assert.fail();
        } catch (Exception ex) {
            // ok
        }
    }

    @Test
    public void testNSSmauvaiseSommeControle() {
        try {
            new NumeroSecuriteSociale("756.1234.5678.96");
            Assert.fail();
        } catch (Exception ex) {
            // ok
        }
        try {
            new NumeroSecuriteSociale("756.1234.5678.98");
            Assert.fail();
        } catch (Exception ex) {
            // ok
        }
    }

    @Test
    public void testNSSnull() {
        try {
            new NumeroSecuriteSociale(null);
            Assert.fail();
        } catch (Exception ex) {
            // ok
        }
    }

    @Test
    public void testNSSvalide() {
        try {
            new NumeroSecuriteSociale("756.1234.5678.97");
        } catch (Exception ex) {
            Assert.fail();
        }
    }

    @Test
    public void testNSSvide() {
        try {
            new NumeroSecuriteSociale("");
            Assert.fail();
        } catch (Exception ex) {
            // ok
        }
    }
}
