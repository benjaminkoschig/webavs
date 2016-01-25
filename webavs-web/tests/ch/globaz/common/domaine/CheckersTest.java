package ch.globaz.common.domaine;

import org.junit.Assert;
import org.junit.Test;

public class CheckersTest {

    @Test
    public void testCheckDateMonthYear() throws Exception {
        try {
            Checkers.checkDateMonthYear("", "test", false);
            Assert.fail("doit renvoyer une exception si on empêche que le mois puisse être vide");
        } catch (IllegalArgumentException ex) {
            // ok
        }
        try {
            Checkers.checkDateMonthYear("", "test", true);
        } catch (IllegalArgumentException ex) {
            Assert.fail("si on permet un mois vide, ne doit pas renvoyer d'exception");
        }
        try {
            Checkers.checkDateMonthYear(null, "test", false);
            Assert.fail();
        } catch (NullPointerException ex) {
            // ok
        }
        try {
            Checkers.checkDateMonthYear("012000", "test", false);
            Assert.fail();
        } catch (IllegalArgumentException ex) {
            // ok
        }
        try {
            Checkers.checkDateMonthYear("200010", "test", false);
            Assert.fail();
        } catch (IllegalArgumentException ex) {
            // ok
        }
        try {
            Checkers.checkDateMonthYear("01.200", "test", false);
            Assert.fail();
        } catch (IllegalArgumentException ex) {
            // ok
        }
        try {
            Checkers.checkDateMonthYear("01.00", "test", false);
            Assert.fail();
        } catch (IllegalArgumentException ex) {
            // ok
        }
        Checkers.checkDateMonthYear("01.2000", "test", false);
    }
}
