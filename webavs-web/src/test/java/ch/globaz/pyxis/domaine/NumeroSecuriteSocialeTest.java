package ch.globaz.pyxis.domaine;

import static org.assertj.core.api.Assertions.*;
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

    @Test
    public void testNSSinconnue() {
        try {
            new NumeroSecuriteSociale("000.0000.0000.00");
        } catch (Exception ex) {
            Assert.fail();
        }
    }

    @Test
    public void testFormatNssInLong() throws Exception {
        NumeroSecuriteSociale nss = new NumeroSecuriteSociale("756.1234.5678.97");
        assertThat(nss.formatInLong()).isEqualTo(7561234567897L);
    }

}
