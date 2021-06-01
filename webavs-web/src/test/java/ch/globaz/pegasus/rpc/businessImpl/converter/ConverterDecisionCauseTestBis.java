package ch.globaz.pegasus.rpc.businessImpl.converter;

import static org.assertj.core.api.Assertions.*;
import java.math.BigInteger;
import org.junit.Test;
import ch.globaz.pegasus.business.domaine.decision.MotifDecision;
import ch.globaz.pegasus.business.domaine.droit.VersionDroit;

public class ConverterDecisionCauseTestBis {

    /**
     * @see ConverterDecisionCauseTest
     */
    @Test
    public void testConvert() throws Exception {
        VersionDroit versionDroit = new VersionDroit("000001");
        versionDroit.setNumero(1);
        assertThat(ConverterDecisionCause.convert(versionDroit, MotifDecision.AUTRE, false)).isEqualTo(BigInteger.valueOf(1));
        versionDroit.setNumero(2);
        try {
            assertThat(ConverterDecisionCause.convert(versionDroit, MotifDecision.AUTRE, false)).isEqualTo(BigInteger.valueOf(1));
        } catch (Exception e) {
            assertThat(true);
        }
    }
}
