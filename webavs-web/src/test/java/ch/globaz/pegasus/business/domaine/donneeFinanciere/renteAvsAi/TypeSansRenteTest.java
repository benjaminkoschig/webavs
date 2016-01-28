package ch.globaz.pegasus.business.domaine.donneeFinanciere.renteAvsAi;

import static org.junit.Assert.*;
import org.junit.Test;

public class TypeSansRenteTest {

    @Test
    public void testFromValueEmpty() throws Exception {
        assertEquals(TypeSansRente.INDEFINIT, TypeSansRente.fromValue(""));
    }

    @Test
    public void testFromValueNull() throws Exception {
        assertEquals(TypeSansRente.INDEFINIT, TypeSansRente.fromValue(null));
    }

    @Test
    public void testFromValueZero() throws Exception {
        assertEquals(TypeSansRente.INDEFINIT, TypeSansRente.fromValue("0"));
    }

}
