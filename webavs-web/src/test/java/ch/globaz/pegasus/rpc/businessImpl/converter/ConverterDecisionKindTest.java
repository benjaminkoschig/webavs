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
import ch.globaz.pegasus.business.domaine.pca.PcaEtatCalcul;

@RunWith(Parameterized.class)
public class ConverterDecisionKindTest {
    @Parameters(name = " TypeDecision: {0}, MotifDecision: {1}, etatCalculFederal: {2}, Expected : {3} ")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { TypeDecision.ADAPTATION_APRES_CALCUL, MotifDecision.INDEFINIT, null, 6 },
                { TypeDecision.ADAPTATION_APRES_CALCUL, MotifDecision.INDEFINIT, PcaEtatCalcul.REFUSE, 2 },
                { TypeDecision.PARTIEL_APRES_CALCUL, MotifDecision.INDEFINIT, null, 6 },
                { TypeDecision.PARTIEL_APRES_CALCUL, MotifDecision.INDEFINIT, PcaEtatCalcul.REFUSE, 2 },
                { TypeDecision.OCTROI_APRES_CALCUL, MotifDecision.INDEFINIT, null, 6 },
                { TypeDecision.OCTROI_APRES_CALCUL, MotifDecision.INDEFINIT, PcaEtatCalcul.REFUSE, 2 },
                { TypeDecision.REFUS_APRES_CALCUL, MotifDecision.INDEFINIT, null, 2 },
                { TypeDecision.REFUS_APRES_CALCUL, MotifDecision.INDEFINIT, PcaEtatCalcul.REFUSE, 2 },
                { TypeDecision.SUPPRESSION_SANS_CALCUL, MotifDecision.AUTRE, null, 3 },
                { TypeDecision.REFUS_SANS_CALCUL, MotifDecision.DOMICILLE_PAR_RECONNU, null, 1 },
                { TypeDecision.REFUS_SANS_CALCUL, MotifDecision.AUCUN_DROIT_DOMICILLE, null, 1 },
                { TypeDecision.REFUS_SANS_CALCUL, MotifDecision.AUCUN_DROIT_EMS, null, 1 },
                { TypeDecision.REFUS_SANS_CALCUL, MotifDecision.DROIT_ENTRETIEN, null, 1 },
                { TypeDecision.REFUS_SANS_CALCUL, MotifDecision.DROIT_INDEMNITE_AI, null, 1 },
                { TypeDecision.REFUS_SANS_CALCUL, MotifDecision.ENFANT_PARENT_RENTIER, null, 1 },
                { TypeDecision.REFUS_SANS_CALCUL, MotifDecision.DELAI_CARENCE, null, 1 },
                { TypeDecision.REFUS_SANS_CALCUL, MotifDecision.JUSTIFICATIFS_DEMANDES_INITIAL, null, 5 },
                { TypeDecision.REFUS_SANS_CALCUL, MotifDecision.PARTIS_SANS_ADRESSE_INITIAL, null, 5 },
                { TypeDecision.REFUS_SANS_CALCUL, MotifDecision.RENCONTRE_IMPOSSIBLE_INITIAL, null, 5 },
                { TypeDecision.REFUS_SANS_CALCUL, MotifDecision.RENONCIATION, null, 4 } });
    }

    private final TypeDecision typeDecision;
    private final MotifDecision motifDecision;
    private final PcaEtatCalcul etatCalculFederal;
    private final int expected;

    public ConverterDecisionKindTest(TypeDecision typeDecision, MotifDecision motifDecision,
            PcaEtatCalcul etatCalculFederal, int expected) {
        this.typeDecision = typeDecision;
        this.motifDecision = motifDecision;
        this.etatCalculFederal = etatCalculFederal;
        this.expected = expected;
    }

    @Test
    public void test() {
        assertThat(convert(typeDecision, motifDecision, etatCalculFederal)).isEqualTo(BigInteger.valueOf(expected));
    }

    private BigInteger convert(TypeDecision typeDecision, MotifDecision motifDecisio, PcaEtatCalcul etatCalculFederal) {
        return ConverterDecisionKind.convert(typeDecision, motifDecisio, etatCalculFederal);
    }
}
