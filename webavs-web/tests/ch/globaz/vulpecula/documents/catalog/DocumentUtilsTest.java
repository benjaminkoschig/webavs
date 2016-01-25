package ch.globaz.vulpecula.documents.catalog;

import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class DocumentUtilsTest {
    @Test
    public void givenNulls_ShouldReturnNull() {
        String message = DocumentUtils.formatMessage(null, null);
        assertNull(message);
    }

    @Test
    public void givenEmptyStringAndNull_ShouldReturnEmpty() {
        String message = DocumentUtils.formatMessage("", null);
        assertEquals("", message);
    }

    @Test
    public void givenBonjourStringAndNull_ShouldReturnBonjour() {
        String message = DocumentUtils.formatMessage("Bonjour", null);
        assertEquals("Bonjour", message);
    }

    @Test
    public void givenBonjourAndParameterStringOfArnaud_ShouldReturnBonjour_Arnaud() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("prenom", "Arnaud");

        String message = DocumentUtils.formatMessage("Bonjour {prenom}", map);
        assertEquals("Bonjour Arnaud", message);
    }

    @Test
    public void givenBonjourAndParameterStringOfArnaudAndGeiser_ShouldReturnBonjour_Arnaud_Geiser() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("prenom", "Arnaud");
        map.put("nom", "Geiser");

        String message = DocumentUtils.formatMessage("Bonjour {prenom} {nom}", map);
        assertEquals("Bonjour Arnaud Geiser", message);
    }

    @Test
    public void givenBonjourAndParameterStringOfArnaudAndGeiser_ShortAndLongStrings_ShouldReturnBonjour_Arnaud_Geiser() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("p", "Arnaud");
        map.put("nom", "G");

        String message = DocumentUtils.formatMessage("Bonjour {p} {nom}", map);
        assertEquals("Bonjour Arnaud G", message);
    }
}
