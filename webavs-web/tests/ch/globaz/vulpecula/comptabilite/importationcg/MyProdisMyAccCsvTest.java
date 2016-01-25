package ch.globaz.vulpecula.comptabilite.importationcg;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Description de la classe
 * 
 * @since WebBMS 1.0
 */
public class MyProdisMyAccCsvTest {

    @Test
    public void testMandatBMS() {
        // Param
        String param = "900";
        // Result
        String result = "BMS";

        // Appel
        assertTrue(result.equals(MyProdisMyAccCsv.getMandat(param)));
    }

    @Test
    public void testMandatAVEN() {
        // Param
        String param = "210";
        // Result
        String result = "AVEN";

        // Appel
        assertTrue(result.equals(MyProdisMyAccCsv.getMandat(param)));
    }

    @Test
    public void testMandatMetalRomandie() {
        // Param
        String param = "67";
        // Result
        String result = "Métal Romandie";

        // Appel
        assertTrue(result.equals(MyProdisMyAccCsv.getMandat(param)));
    }

    @Test
    public void testMandatCIFCAssociation() {
        // Param
        String param = "106";
        // Result
        String result = "CIFC - Association";

        // Appel
        assertTrue(result.equals(MyProdisMyAccCsv.getMandat(param)));
    }
}
