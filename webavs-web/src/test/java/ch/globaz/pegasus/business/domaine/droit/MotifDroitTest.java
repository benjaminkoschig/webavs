package ch.globaz.pegasus.business.domaine.droit;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

public class MotifDroitTest {

    @Test
    public void testIsDeces() throws Exception {
        assertThat(MotifDroit.DECES.isDeces()).isTrue();
    }

}
