package ch.globaz.pegasus.business.domaine.decision;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

public class TypeDecisionTest {

    @Test
    public void testIsSuppressionTrue() throws Exception {
        assertThat(TypeDecision.SUPPRESSION_SANS_CALCUL.isSuppression()).isTrue();
    }

    @Test
    public void testIsSuppressionFalse() throws Exception {
        assertThat(TypeDecision.ADAPTATION_APRES_CALCUL.isSuppression()).isFalse();
    }

    @Test
    public void testIsAdaptationTrue() throws Exception {
        assertThat(TypeDecision.ADAPTATION_APRES_CALCUL.isAdaptation()).isTrue();
    }

    @Test
    public void testIsAdaptationFalse() throws Exception {
        assertThat(TypeDecision.SUPPRESSION_SANS_CALCUL.isAdaptation()).isFalse();
    }

    @Test
    public void testIsOctroiTrue() throws Exception {
        assertThat(TypeDecision.OCTROI_APRES_CALCUL.isOctroi()).isTrue();
    }

    @Test
    public void testIsOctroiFalse() throws Exception {
        assertThat(TypeDecision.SUPPRESSION_SANS_CALCUL.isOctroi()).isFalse();
    }

    @Test
    public void testIsPartielTrue() throws Exception {
        assertThat(TypeDecision.PARTIEL_APRES_CALCUL.isPartiel()).isTrue();
    }

    @Test
    public void testIsPartielFalse() throws Exception {
        assertThat(TypeDecision.SUPPRESSION_SANS_CALCUL.isPartiel()).isFalse();
    }

    @Test
    public void testIsRefusApresCalculTrue() throws Exception {
        assertThat(TypeDecision.REFUS_APRES_CALCUL.isRefusApresCalcul()).isTrue();
    }

    @Test
    public void testIsRefusApresCalculFalse() throws Exception {
        assertThat(TypeDecision.SUPPRESSION_SANS_CALCUL.isRefusApresCalcul()).isFalse();
    }

    @Test
    public void testIsTypeApresCalculTrue() throws Exception {
        assertThat(TypeDecision.ADAPTATION_APRES_CALCUL.isTypeApresCalcul()).isTrue();
        assertThat(TypeDecision.OCTROI_APRES_CALCUL.isTypeApresCalcul()).isTrue();
        assertThat(TypeDecision.PARTIEL_APRES_CALCUL.isTypeApresCalcul()).isTrue();
        assertThat(TypeDecision.REFUS_APRES_CALCUL.isTypeApresCalcul()).isTrue();
    }

    @Test
    public void testIsTypeApresCalculFalse() throws Exception {
        assertThat(TypeDecision.SUPPRESSION_SANS_CALCUL.isTypeApresCalcul()).isFalse();
    }

    @Test
    public void testIsRefusSansCalculTrue() throws Exception {
        assertThat(TypeDecision.REFUS_SANS_CALCUL.isRefusSansCalcul()).isTrue();
    }

    @Test
    public void testIsRefusSansCalculFalse() throws Exception {
        assertThat(TypeDecision.SUPPRESSION_SANS_CALCUL.isRefusSansCalcul()).isFalse();
    }

    @Test
    public void testIsRefusOrSuppression() throws Exception {
        assertThat(TypeDecision.OCTROI_APRES_CALCUL.isRefus()).isFalse();
        assertThat(TypeDecision.PARTIEL_APRES_CALCUL.isRefus()).isFalse();
        assertThat(TypeDecision.ADAPTATION_APRES_CALCUL.isRefus()).isFalse();
        assertThat(TypeDecision.REFUS_APRES_CALCUL.isRefus()).isTrue();
        assertThat(TypeDecision.REFUS_SANS_CALCUL.isRefus()).isTrue();
        assertThat(TypeDecision.SUPPRESSION_SANS_CALCUL.isRefus()).isFalse();
    }

    @Test
    public void testIsOctroiOrPartiel() throws Exception {
        assertThat(TypeDecision.OCTROI_APRES_CALCUL.isOctroiOrPartiel()).isTrue();
        assertThat(TypeDecision.PARTIEL_APRES_CALCUL.isOctroiOrPartiel()).isTrue();
    }
}
