package globaz.osiris.db.ordres.sepa.utils;

import static org.junit.Assert.*;
import org.junit.Test;

public class CASepaCommonUtilsTest {

    @Test
    public void testIsValidIban() throws Exception {
        assertFalse(CASepaCommonUtils.isValidIban("2-01477-2"));
        assertTrue(CASepaCommonUtils.isValidIban("CH6809000000177593409"));
        assertTrue(CASepaCommonUtils.isValidIban("CH68 0900 0000 1775 9340 9"));

        // certaine données sont incorrectement mise en DB
        assertFalse(CASepaCommonUtils.isValidIban("CH68.0900.0000.1775.9340.9"));
        // certains iban sont hors suisse
        assertTrue(CASepaCommonUtils.isValidIban("FR68 0900 0000 1775 9340 9"));
        // magic number non respecté
        assertFalse(CASepaCommonUtils.isValidIban("FR68 0900 0000 1775 9340 0"));

    }

    @Test
    public void testEscapeInvalidBasicTextCH() throws Exception {
        assertTrue("immeuble".equals(CASepaCommonUtils.escapeInvalidBasicTextCH("immeuble")));
        assertFalse("immeuble n°63".equals(CASepaCommonUtils.escapeInvalidBasicTextCH("immeuble n°63")));
        assertTrue("immeuble n 63".equals(CASepaCommonUtils.escapeInvalidBasicTextCH("immeuble n°63")));

    }

}
