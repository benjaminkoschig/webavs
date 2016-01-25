package ch.globaz.pyxis.domaine.constantes;

import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class CodeIsoPaysTest {

    @Test
    @Ignore
    public void testParse() throws Exception {

        Assert.assertEquals(CodeIsoPays.SUISSE, CodeIsoPays.parse("CH"));
        Assert.assertEquals(CodeIsoPays.SUISSE, CodeIsoPays.parse("ch"));

        try {
            Assert.assertEquals(CodeIsoPays.SUISSE, CodeIsoPays.parse("che"));
            Assert.fail();
        } catch (IllegalArgumentException ex) {
            // ok
        }

        try {
            Assert.assertEquals(CodeIsoPays.SUISSE, CodeIsoPays.parse(null));
            Assert.fail();
        } catch (IllegalArgumentException ex) {
            // ok
        }

        try {
            Assert.assertEquals(CodeIsoPays.SUISSE, CodeIsoPays.parse("12"));
            Assert.fail();
        } catch (IllegalArgumentException ex) {
            // ok
        }
    }

}
