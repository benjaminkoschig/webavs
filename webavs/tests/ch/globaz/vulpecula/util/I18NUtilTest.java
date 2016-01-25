package ch.globaz.vulpecula.util;

import static org.junit.Assert.*;
import java.util.Locale;
import org.junit.Test;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import ch.globaz.vulpecula.util.I18NUtil;

public class I18NUtilTest {
    @Test
    public void getUserLocaleOf_GivenNoCodeLangue_ShouldReturnNullPointerException() {
        try {
            I18NUtil.getLocaleOf(null);
            fail("I18NUtil.getLocaleOf a reçu un null en paramètre et aucune exception n'a été remontée");
        } catch (NullPointerException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void getUserLocaleOf_GivenCodeLangueFr_ShouldReturnLocaleFrench() {
        Locale locale = I18NUtil.getLocaleOf(CodeLangue.FR);
        assertEquals(Locale.FRENCH, locale);
    }

    @Test
    public void getUserLocaleOf_GivenCodeLangueDe_ShouldReturnLocaleGerman() {
        Locale locale = I18NUtil.getLocaleOf(CodeLangue.DE);
        assertEquals(Locale.GERMAN, locale);
    }

    @Test
    public void getUserLocaleOf_GivenCodeLangueIt_ShouldReturnLocaleItalien() {
        Locale locale = I18NUtil.getLocaleOf(CodeLangue.IT);
        assertEquals(Locale.ITALIAN, locale);
    }

    @Test
    public void getUserLocaleOf_GivenCodeLangueEn_ShouldReturnLocaleEnglish() {
        Locale locale = I18NUtil.getLocaleOf(CodeLangue.EN);
        assertEquals(Locale.ENGLISH, locale);
    }

    @Test
    public void getLanguesOf_GivenCodeSystemeOf503002_ShouldReturnAllemand() {
        assertEquals(Langues.Allemand, I18NUtil.getLanguesOf("503002"));
    }

    @Test
    public void getLanguesOf_GivenCodeSystemeOf503001_ShouldReturnFrancais() {
        assertEquals(Langues.Francais, I18NUtil.getLanguesOf("503001"));
    }

    @Test
    public void getLanguesOf_GivenCodeSystemeOf5030_ShouldReturnNull() {
        assertEquals(Langues.Francais, I18NUtil.getLanguesOf("5030"));
    }
}
