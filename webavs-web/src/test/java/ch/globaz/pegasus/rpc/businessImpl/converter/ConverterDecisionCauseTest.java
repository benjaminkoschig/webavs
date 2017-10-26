package ch.globaz.pegasus.rpc.businessImpl.converter;

import static org.fest.assertions.api.Assertions.*;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import ch.globaz.pegasus.business.domaine.droit.EtatDroit;
import ch.globaz.pegasus.business.domaine.droit.MotifDroit;
import ch.globaz.pegasus.business.domaine.droit.VersionDroit;

@RunWith(Parameterized.class)
public class ConverterDecisionCauseTest {

    @Parameters(name = " MotifDroit: {0}, Expected : {1}")
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] { { MotifDroit.ADAPTATION, 3 }, { MotifDroit.ADAPTATION_HOME, 3 },
                        { MotifDroit.ARRIVEE_ETRANGER, 1 }, { MotifDroit.CORRECTION_REPRISE, 2 },
                        { MotifDroit.DECES, 4 }, { MotifDroit.DEPART_ETRANGER, 6 }, { MotifDroit.DIVORCE, 2 },
                        { MotifDroit.MARRIAGE, 2 }, { MotifDroit.MODIFICATIONS_DIVERSES, 2 },
                        { MotifDroit.NOUVEAU_DROIT, 1 }, { MotifDroit.REPRISE, 2 },
                        { MotifDroit.REPRISE_ADAPTATION_ERRONE, 6 }, { MotifDroit.REVISION_QUADRIENNALE, 5 },
                        { MotifDroit.TRANSFERT_CLARENS, 6 }, { MotifDroit.TRANSFERT_HORS_CANTON, 1 },
                        { MotifDroit.VEUVAGE, 4 } });
    }

    private final MotifDroit motifDroit;
    private final int expected;

    public ConverterDecisionCauseTest(MotifDroit motifDroit, int expected) {
        this.motifDroit = motifDroit;
        this.expected = expected;
    }

    @Test
    public void test() {
        assertThat(convert(motifDroit)).isEqualTo(BigInteger.valueOf(expected));
    }

    private BigInteger convert(MotifDroit motifDroit) {
        return ConverterDecisionCause.convert(new VersionDroit("0001", 2, EtatDroit.VALIDE, motifDroit));
    }

}
