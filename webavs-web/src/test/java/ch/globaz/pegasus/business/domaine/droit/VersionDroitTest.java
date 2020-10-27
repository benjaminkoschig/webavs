package ch.globaz.pegasus.business.domaine.droit;

import static org.fest.assertions.api.Assertions.*;
import org.junit.Test;

public class VersionDroitTest {

    @Test
    public void testIsInitialTrue() throws Exception {
        VersionDroit droit = new VersionDroit("1", 1, EtatDroit.AU_CALCUL, MotifDroit.MODIFICATIONS_DIVERSES, false);
        assertThat(droit.isInitial()).isTrue();
    }

    @Test
    public void testIsInitialFalse() throws Exception {
        VersionDroit droit = new VersionDroit("1", 2, EtatDroit.AU_CALCUL, MotifDroit.MODIFICATIONS_DIVERSES, false);
        assertThat(droit.isInitial()).isFalse();
    }

}
