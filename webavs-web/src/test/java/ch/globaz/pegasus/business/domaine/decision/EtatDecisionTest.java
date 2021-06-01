package ch.globaz.pegasus.business.domaine.decision;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

public class EtatDecisionTest {

    @Test
    public void testIsPreValideTrue() throws Exception {
        assertThat(EtatDecision.PRE_VALIDE.isPreValide()).isTrue();
    }

    @Test
    public void testIsPreValideFalse() throws Exception {
        assertThat(EtatDecision.ENREGISTRE.isPreValide()).isFalse();
    }

    @Test
    public void testIsEnregistreTrue() throws Exception {
        assertThat(EtatDecision.ENREGISTRE.isEnregistre()).isTrue();
    }

    @Test
    public void testIsEnregistreFalse() throws Exception {
        assertThat(EtatDecision.ENREGISTRE.isPreValide()).isFalse();
    }

    @Test
    public void testIsValideTrue() throws Exception {
        assertThat(EtatDecision.VALIDE.isValide()).isTrue();
    }

    @Test
    public void testIsValideFalse() throws Exception {
        assertThat(EtatDecision.VALIDE.isPreValide()).isFalse();
    }
}
