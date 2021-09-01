package globaz.prestation.acor.acor2020.ws;

import ch.globaz.common.util.Slashs;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SlashsTest {
    @Test
    public void addFirstSlash() {
        assertThat(Slashs.addFirstSlash("tests/test")).isEqualTo("/tests/test");
        assertThat(Slashs.addFirstSlash("/tests/test")).isEqualTo("/tests/test");
    }

    @Test
    public void deleteLastSlash() {
        assertThat(Slashs.deleteLastSlash("tests/test/")).isEqualTo("tests/test");
        assertThat(Slashs.deleteLastSlash("tests/test")).isEqualTo("tests/test");
    }
}
