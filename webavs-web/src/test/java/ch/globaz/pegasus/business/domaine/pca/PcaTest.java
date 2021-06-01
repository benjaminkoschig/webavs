package ch.globaz.pegasus.business.domaine.pca;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import ch.globaz.common.domaine.Date;

public class PcaTest {

    @Test
        public void testHasCurrent() throws Exception {
            Pca pca = new Pca();
            assertThat(pca.hasCurrent()).isTrue();
            pca.setDateFin(new Date());
            assertThat(pca.hasCurrent()).isFalse();
        }

}
