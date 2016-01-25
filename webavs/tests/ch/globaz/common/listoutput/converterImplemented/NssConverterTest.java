package ch.globaz.common.listoutput.converterImplemented;

import static org.junit.Assert.*;
import org.junit.Test;

public class NssConverterTest {

    @Test
    public void testGetValue() throws Exception {
        NssConverter converter = new NssConverter();
        assertEquals("756.1000.2000.12", converter.getValue("7561000200012", null));
    }
}
