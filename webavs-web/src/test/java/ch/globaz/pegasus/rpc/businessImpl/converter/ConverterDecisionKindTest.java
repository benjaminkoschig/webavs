package ch.globaz.pegasus.rpc.businessImpl.converter;

import static org.fest.assertions.api.Assertions.*;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import ch.globaz.pegasus.business.domaine.decision.MotifDecision;
import ch.globaz.pegasus.business.domaine.decision.TypeDecision;

@RunWith(Parameterized.class)
public class ConverterDecisionKindTest {
    @Parameters(name = " TypeDecision: {0}, : {1}, Expected : {2} ")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { TypeDecision.ADAPTATION_APRES_CALCUL, MotifDecision.INDEFINIT, 6 },
                { TypeDecision.PARTIEL_APRES_CALCUL, MotifDecision.INDEFINIT, 6 },
                { TypeDecision.OCTROI_APRES_CALCUL, MotifDecision.INDEFINIT, 6 },
                { TypeDecision.REFUS_APRES_CALCUL, MotifDecision.INDEFINIT, 2 },
                { TypeDecision.SUPPRESSION_SANS_CALCUL, MotifDecision.AUTRE, 3 },
                { TypeDecision.REFUS_SANS_CALCUL, MotifDecision.DOMICILLE_PAR_RECONNU, 1 },
                { TypeDecision.REFUS_SANS_CALCUL, MotifDecision.AUCUN_DROIT_DOMICILLE, 1 },
                { TypeDecision.REFUS_SANS_CALCUL, MotifDecision.AUCUN_DROIT_EMS, 1 },
                { TypeDecision.REFUS_SANS_CALCUL, MotifDecision.DROIT_ENTRETIEN, 1 },
                { TypeDecision.REFUS_SANS_CALCUL, MotifDecision.DROIT_INDEMNITE_AI, 1 },
                { TypeDecision.REFUS_SANS_CALCUL, MotifDecision.ENFANT_PARENT_RENTIER, 1 },
                { TypeDecision.REFUS_SANS_CALCUL, MotifDecision.DELAI_CARENCE, 1 },
                { TypeDecision.REFUS_SANS_CALCUL, MotifDecision.JUSTIFICATIFS_DEMANDES_INITIAL, 5 },
                { TypeDecision.REFUS_SANS_CALCUL, MotifDecision.PARTIS_SANS_ADRESSE_INITIAL, 5 },
                { TypeDecision.REFUS_SANS_CALCUL, MotifDecision.RENCONTRE_IMPOSSIBLE_INITIAL, 5 },
                { TypeDecision.REFUS_SANS_CALCUL, MotifDecision.RENONCIATION, 4 } });
    }

    private final TypeDecision typeDecision;
    private final MotifDecision motifDecision;
    private final int expected;

    public ConverterDecisionKindTest(TypeDecision typeDecision, MotifDecision motifDecision, int expected) {
        this.typeDecision = typeDecision;
        this.motifDecision = motifDecision;
        this.expected = expected;
    }

    @Test
    public void test() {
        assertThat(convert(typeDecision, motifDecision)).isEqualTo(BigInteger.valueOf(expected));
    }

    private BigInteger convert(TypeDecision typeDecision, MotifDecision motifDecisio) {
        return ConverterDecisionKind.convert(typeDecision, motifDecisio);
    }
}
