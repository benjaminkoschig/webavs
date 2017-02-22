package ch.globaz.naos.ree.domain.converter;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.naos.ree.domain.converter.Converter5053_102;

public class Converter5053_102Test {
    @Test
    public void testTranslateToCombinationCode() throws Exception {

        assertTrue(Converter5053_102.translateToCombinationCode("819006").equals("05"));
        assertTrue(Converter5053_102.translateToCombinationCode("819001").equals("06"));
        assertTrue(Converter5053_102.translateToCombinationCode("819007").equals("04"));
        assertTrue(Converter5053_102.translateToCombinationCode(null).equals("99"));
        assertTrue(Converter5053_102.translateToCombinationCode("000000").equals("99"));

    }
}
