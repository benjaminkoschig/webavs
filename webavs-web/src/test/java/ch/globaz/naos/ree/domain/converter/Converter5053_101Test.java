package ch.globaz.naos.ree.domain.converter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import ch.globaz.naos.ree.domain.SuiviCaisseLoader;
import ch.globaz.naos.ree.domain.converter.Converter5053_101;

public class Converter5053_101Test {
    private static final String MOCK_ID_AFF = "123";

    @Test
    public void testTranslateMotifFinToLeavingreason() {

        SuiviCaisseLoader mockSuivi = mock(SuiviCaisseLoader.class);
        when(mockSuivi.hasAfter(MOCK_ID_AFF)).thenReturn(true);
        assertEquals("D", Converter5053_101.translateMotifFinToLeavingreason("803006", null, null));
        assertEquals("E", Converter5053_101.translateMotifFinToLeavingreason("803004", null, null));
        assertEquals("F", Converter5053_101.translateMotifFinToLeavingreason("803001", null, null));
        assertEquals("H", Converter5053_101.translateMotifFinToLeavingreason("803008", null, null));
        // dummy test suiviLoader
        assertEquals("X",
                Converter5053_101.translateMotifFinToLeavingreason("803002", "123", new SuiviCaisseLoader(null)));
        assertEquals("K", Converter5053_101.translateMotifFinToLeavingreason("803002", MOCK_ID_AFF, mockSuivi));
        assertEquals("P", Converter5053_101.translateMotifFinToLeavingreason("803038", null, null));
        assertEquals("Q", Converter5053_101.translateMotifFinToLeavingreason("803040", null, null));
        assertEquals("R", Converter5053_101.translateMotifFinToLeavingreason("803042", null, null));
        assertEquals("T", Converter5053_101.translateMotifFinToLeavingreason("803041", null, null));
        assertEquals("X", Converter5053_101.translateMotifFinToLeavingreason("000000", null, null));
        assertEquals("X", Converter5053_101.translateMotifFinToLeavingreason("", null, null));

    }

    @Test
    public void testTranslateFormeJuriToLegalForm() throws Exception {

        assertEquals("A", Converter5053_101.translateFormeJuriToLegalForm("806001", null));
        assertEquals("D", Converter5053_101.translateFormeJuriToLegalForm("806002", null));
        assertEquals("E", Converter5053_101.translateFormeJuriToLegalForm("806003", null));
        assertEquals("G", Converter5053_101.translateFormeJuriToLegalForm("806004", null));
        assertEquals("H", Converter5053_101.translateFormeJuriToLegalForm("806005", null));
        assertEquals("I", Converter5053_101.translateFormeJuriToLegalForm("806006", null));
        assertEquals("C", Converter5053_101.translateFormeJuriToLegalForm("806007", null));
        assertEquals("K", Converter5053_101.translateFormeJuriToLegalForm("806008", null));
        assertEquals("L", Converter5053_101.translateFormeJuriToLegalForm("806009", null));
        assertEquals("N", Converter5053_101.translateFormeJuriToLegalForm("806012", null));
        assertEquals("M", Converter5053_101.translateFormeJuriToLegalForm("806013", null));
        assertEquals("F", Converter5053_101.translateFormeJuriToLegalForm("806014", null));
        assertEquals("B", Converter5053_101.translateFormeJuriToLegalForm("806018", null));
        assertEquals("Z", Converter5053_101.translateFormeJuriToLegalForm("806019", null));
        assertEquals("X", Converter5053_101.translateFormeJuriToLegalForm("", null));
        assertEquals("X", Converter5053_101.translateFormeJuriToLegalForm(null, null));

        // TODO si code utilisateur correspond à la valeur P(immeuble)
        // assertEquals("P", Converter5053_101.translateFormeJuriToLegalForm("19000001", null));
    }

    @Test
    public void testTranslateTypeEntrepriseToFirmForm() {
        assertEquals(3, Converter5053_101.translateTypeEntrepriseToFirmForm("804001"));
        assertEquals(1, Converter5053_101.translateTypeEntrepriseToFirmForm("804002"));
        assertEquals(1, Converter5053_101.translateTypeEntrepriseToFirmForm("804010"));
        assertEquals(6, Converter5053_101.translateTypeEntrepriseToFirmForm("804004"));
        assertEquals(6, Converter5053_101.translateTypeEntrepriseToFirmForm("804006"));
        assertEquals(2, Converter5053_101.translateTypeEntrepriseToFirmForm("804005"));
        assertEquals(4, Converter5053_101.translateTypeEntrepriseToFirmForm("804008"));
        assertEquals(4, Converter5053_101.translateTypeEntrepriseToFirmForm("804011"));
        assertEquals(5, Converter5053_101.translateTypeEntrepriseToFirmForm("804012"));
        assertEquals(99, Converter5053_101.translateTypeEntrepriseToFirmForm(""));
        assertEquals(99, Converter5053_101.translateTypeEntrepriseToFirmForm(null));
    }

    @Test
    public void testDeterminerFirmForm() throws Exception {
        assertEquals(5, Converter5053_101.determinerFirmForm("J", "804001"));
        assertEquals(5, Converter5053_101.determinerFirmForm("J", "804002"));
        assertEquals(5, Converter5053_101.determinerFirmForm("J", "804010"));
        assertEquals(5, Converter5053_101.determinerFirmForm("J", "804004"));
        assertEquals(5, Converter5053_101.determinerFirmForm("J", "804006"));
        assertEquals(5, Converter5053_101.determinerFirmForm("J", "804005"));
        assertEquals(5, Converter5053_101.determinerFirmForm("J", "804008"));
        assertEquals(5, Converter5053_101.determinerFirmForm("J", "804011"));
        assertEquals(5, Converter5053_101.determinerFirmForm("J", "804012"));
        assertEquals(5, Converter5053_101.determinerFirmForm("J", ""));
        assertEquals(5, Converter5053_101.determinerFirmForm("J", null));

        assertEquals(7, Converter5053_101.determinerFirmForm("P", "804001"));
        assertEquals(7, Converter5053_101.determinerFirmForm("P", "804002"));
        assertEquals(7, Converter5053_101.determinerFirmForm("P", "804010"));
        assertEquals(7, Converter5053_101.determinerFirmForm("P", "804004"));
        assertEquals(7, Converter5053_101.determinerFirmForm("P", "804006"));
        assertEquals(7, Converter5053_101.determinerFirmForm("P", "804005"));
        assertEquals(7, Converter5053_101.determinerFirmForm("P", "804008"));
        assertEquals(7, Converter5053_101.determinerFirmForm("P", "804011"));
        assertEquals(7, Converter5053_101.determinerFirmForm("P", "804012"));
        assertEquals(7, Converter5053_101.determinerFirmForm("P", ""));
        assertEquals(7, Converter5053_101.determinerFirmForm("P", null));

        assertEquals(3, Converter5053_101.determinerFirmForm("X", "804001"));
        assertEquals(1, Converter5053_101.determinerFirmForm("X", "804002"));
        assertEquals(1, Converter5053_101.determinerFirmForm("X", "804010"));
        assertEquals(6, Converter5053_101.determinerFirmForm("X", "804004"));
        assertEquals(6, Converter5053_101.determinerFirmForm("X", "804006"));
        assertEquals(2, Converter5053_101.determinerFirmForm("X", "804005"));
        assertEquals(4, Converter5053_101.determinerFirmForm("X", "804008"));
        assertEquals(4, Converter5053_101.determinerFirmForm("X", "804011"));
        assertEquals(5, Converter5053_101.determinerFirmForm("X", "804012"));
        assertEquals(99, Converter5053_101.determinerFirmForm("X", ""));
        assertEquals(99, Converter5053_101.determinerFirmForm("X", null));

    }

    @Test
    public void testDeterminerLegalForm() throws Exception {

        assertEquals("J", Converter5053_101.determinerLegalForm("806001", null, "805038"));
        assertEquals("J", Converter5053_101.determinerLegalForm("806002", null, "805038"));
        assertEquals("J", Converter5053_101.determinerLegalForm("806003", null, "805038"));
        assertEquals("J", Converter5053_101.determinerLegalForm("806004", null, "805038"));
        assertEquals("J", Converter5053_101.determinerLegalForm("806005", null, "805038"));
        assertEquals("J", Converter5053_101.determinerLegalForm("806006", null, "805038"));
        assertEquals("J", Converter5053_101.determinerLegalForm("806007", null, "805038"));
        assertEquals("J", Converter5053_101.determinerLegalForm("806008", null, "805038"));
        assertEquals("J", Converter5053_101.determinerLegalForm("806009", null, "805038"));
        assertEquals("J", Converter5053_101.determinerLegalForm("806012", null, "805038"));
        assertEquals("J", Converter5053_101.determinerLegalForm("806013", null, "805038"));
        assertEquals("J", Converter5053_101.determinerLegalForm("806014", null, "805038"));
        assertEquals("J", Converter5053_101.determinerLegalForm("806018", null, "805038"));
        assertEquals("J", Converter5053_101.determinerLegalForm("806019", null, "805038"));
        assertEquals("J", Converter5053_101.determinerLegalForm("", null, "805038"));
        assertEquals("J", Converter5053_101.determinerLegalForm(null, null, "805038"));

        assertEquals("A", Converter5053_101.determinerLegalForm("806001", null, null));
        assertEquals("D", Converter5053_101.determinerLegalForm("806002", null, null));
        assertEquals("E", Converter5053_101.determinerLegalForm("806003", null, null));
        assertEquals("G", Converter5053_101.determinerLegalForm("806004", null, null));
        assertEquals("H", Converter5053_101.determinerLegalForm("806005", null, null));
        assertEquals("I", Converter5053_101.determinerLegalForm("806006", null, null));
        assertEquals("C", Converter5053_101.determinerLegalForm("806007", null, null));
        assertEquals("K", Converter5053_101.determinerLegalForm("806008", null, null));
        assertEquals("L", Converter5053_101.determinerLegalForm("806009", null, null));
        assertEquals("N", Converter5053_101.determinerLegalForm("806012", null, null));
        assertEquals("M", Converter5053_101.determinerLegalForm("806013", null, null));
        assertEquals("F", Converter5053_101.determinerLegalForm("806014", null, null));
        assertEquals("B", Converter5053_101.determinerLegalForm("806018", null, null));
        assertEquals("Z", Converter5053_101.determinerLegalForm("806019", null, null));
        assertEquals("X", Converter5053_101.determinerLegalForm("", null, null));
        assertEquals("X", Converter5053_101.determinerLegalForm(null, null, null));
    }

}
