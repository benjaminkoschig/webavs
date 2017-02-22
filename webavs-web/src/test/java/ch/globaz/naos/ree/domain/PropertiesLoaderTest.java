package ch.globaz.naos.ree.domain;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.naos.ree.domain.PropertiesLoader;
import ch.globaz.naos.ree.domain.pojo.ProcessProperties;

public class PropertiesLoaderTest {

    @Test
    public void testValidate() throws Exception {
        PropertiesLoader pl = new PropertiesLoader();
        ProcessProperties pojo = new ProcessProperties();
        try {
            pl.validate(pojo);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
            System.out.println(e.getMessage());
        }
        pojo.setName("Name not empty");
        try {
            pl.validate(pojo);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
            System.out.println(e.getMessage());
        }
        pojo.setPhone("55100100100");
        try {
            pl.validate(pojo);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
            System.out.println(e.getMessage());
        }
        pojo.setEmail("valid@globaz.ch");
        try {
            pl.validate(pojo);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
            System.out.println(e.getMessage());
        }
        pojo.setTailleLot(10000);
        try {
            pl.validate(pojo);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
            System.out.println(e.getMessage());
        }
        pojo.setRecipientId("T0-2222-11");
        try {
            pl.validate(pojo);
            assertTrue(true);
        } catch (IllegalArgumentException e) {
            assertTrue(false);
        }
        pojo.setPhone("not digit");
        try {
            pl.validate(pojo);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
            System.out.println(e.getMessage());
        }
        // not enouth digit
        pojo.setPhone("123456");
        try {
            pl.validate(pojo);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
            System.out.println(e.getMessage());
        }
        pojo.setPhone("55100100100");
        pojo.setTailleLot(0);
        try {
            pl.validate(pojo);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
            System.out.println(e.getMessage());
        }
        pojo.setTailleLot(10000);
        pojo.setEmail("this is not email");
        try {
            pl.validate(pojo);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testPropertieBooleanTrueFalse() throws Exception {
        PropertiesLoader pl = new PropertiesLoader();
        assertTrue(pl.propertieBooleanTrueFalse("true"));
        assertFalse(pl.propertieBooleanTrueFalse("false"));
        assertFalse(pl.propertieBooleanTrueFalse("FALSE"));
        assertTrue(pl.propertieBooleanTrueFalse("TRUE"));
        try {
            pl.propertieBooleanTrueFalse("VRAI");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
            System.out.println(e.getMessage());
        }
        try {
            pl.propertieBooleanTrueFalse("oui");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
            System.out.println(e.getMessage());
        }
        try {
            pl.propertieBooleanTrueFalse("");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
            System.out.println(e.getMessage());
        }
        try {
            pl.propertieBooleanTrueFalse(null);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
            System.out.println(e.getMessage());
        }

    }
}
