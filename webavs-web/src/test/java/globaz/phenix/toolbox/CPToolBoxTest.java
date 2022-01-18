package globaz.phenix.toolbox;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CPToolBoxTest {

    @Test
    public void testFormatRemoveNumSeparators() {
        assertThat("a12\\$").isEqualTo(CPToolBox.formatRemoveNumSeparators("a1.2/-\\$_"));
        assertThat("").isEqualTo(CPToolBox.formatRemoveNumSeparators(null));
    }
}