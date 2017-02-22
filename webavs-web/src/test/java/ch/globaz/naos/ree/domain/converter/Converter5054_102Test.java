package ch.globaz.naos.ree.domain.converter;

import static org.junit.Assert.*;
import java.math.BigInteger;
import org.junit.Test;
import ch.globaz.naos.ree.domain.converter.Converter5054_102;

public class Converter5054_102Test {

    @Test
    public void testDefineActivity() throws Exception {
        assertEquals(BigInteger.ONE, Converter5054_102.defineActivity(""));
        assertEquals(BigInteger.ONE, Converter5054_102.defineActivity(null));
        assertEquals(BigInteger.ONE, Converter5054_102.defineActivity("0"));

        assertEquals(BigInteger.valueOf(2), Converter5054_102.defineActivity("1355"));

    }

    @Test
    public void testDefineTypeDecision() throws Exception {
        // décision définitive D
        assertEquals("D", Converter5054_102.defineTypeDecision("605002")); // définitive
        assertEquals("D", Converter5054_102.defineTypeDecision("605004")); // rectification
        assertEquals("D", Converter5054_102.defineTypeDecision("605008")); // remise
        assertEquals("D", Converter5054_102.defineTypeDecision("605009")); // reduction

        // Décision provisoire P
        assertEquals("P", Converter5054_102.defineTypeDecision(""));
        assertEquals("P", Converter5054_102.defineTypeDecision("0"));
        assertEquals("P", Converter5054_102.defineTypeDecision(null));
        assertEquals("P", Converter5054_102.defineTypeDecision("605005")); // mise en compte
        assertEquals("P", Converter5054_102.defineTypeDecision("605001")); // provisoire
        assertEquals("P", Converter5054_102.defineTypeDecision("605007")); // acompte
        assertEquals("P", Converter5054_102.defineTypeDecision("605003")); // correction
    }

}
