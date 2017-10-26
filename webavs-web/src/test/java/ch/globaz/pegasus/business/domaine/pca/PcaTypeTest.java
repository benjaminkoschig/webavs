package ch.globaz.pegasus.business.domaine.pca;

import static org.fest.assertions.api.Assertions.*;
import org.junit.Test;

public class PcaTypeTest {

    @Test
    public void testIsAi() throws Exception {
        assertThat(PcaType.INVALIDITE.isAi()).isTrue();
        assertThat(PcaType.VIELLESSE.isAi()).isFalse();
        assertThat(PcaType.SURVIVANT.isAi()).isFalse();
    }

    @Test
    public void testIsAvs() throws Exception {
        assertThat(PcaType.INVALIDITE.isAvs()).isFalse();
        assertThat(PcaType.VIELLESSE.isAvs()).isTrue();
        assertThat(PcaType.SURVIVANT.isAvs()).isTrue();
    }

}
