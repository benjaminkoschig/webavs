package ch.globaz.orion.business.domaine.pucs;

import static org.junit.Assert.*;
import org.junit.Test;

public class DeclarationSalaireProvenanceTest {

    @Test(expected = IllegalArgumentException.class)
    public void testFromValueString() throws Exception {
        DeclarationSalaireProvenance.fromValue("");
    }

    @Test
    public void testFromValue1() throws Exception {
        assertEquals(DeclarationSalaireProvenance.PUCS, DeclarationSalaireProvenance.fromValue("1"));
    }

    @Test
    public void testFromValue2() throws Exception {
        assertEquals(DeclarationSalaireProvenance.DAN, DeclarationSalaireProvenance.fromValue("2"));
    }

    @Test
    public void testFromValue3() throws Exception {
        assertEquals(DeclarationSalaireProvenance.SWISS_DEC, DeclarationSalaireProvenance.fromValue("4"));
    }

    @Test
    public void testFromValueWithOutException() throws Exception {
        assertEquals(DeclarationSalaireProvenance.UNDEFINDED,
                DeclarationSalaireProvenance.fromValueWithOutException(""));
    }

    @Test
    public void testIsSwissDec() throws Exception {
        assertTrue(DeclarationSalaireProvenance.SWISS_DEC.isSwissDec());
        assertFalse(DeclarationSalaireProvenance.DAN.isSwissDec());
        assertFalse(DeclarationSalaireProvenance.PUCS.isSwissDec());
    }

    @Test
    public void testIsDan() throws Exception {
        assertTrue(DeclarationSalaireProvenance.DAN.isDan());
        assertFalse(DeclarationSalaireProvenance.SWISS_DEC.isDan());
        assertFalse(DeclarationSalaireProvenance.PUCS.isDan());
    }

    @Test
    public void testIsPucs() throws Exception {
        assertTrue(DeclarationSalaireProvenance.PUCS.isPucs());
        assertFalse(DeclarationSalaireProvenance.DAN.isPucs());
        assertFalse(DeclarationSalaireProvenance.SWISS_DEC.isPucs());
    }
}
