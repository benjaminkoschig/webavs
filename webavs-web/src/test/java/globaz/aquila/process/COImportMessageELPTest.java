package globaz.aquila.process;

import junit.framework.TestCase;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class COImportMessageELPTest {

    @Test
    public void loadSchema_true(){
        COImportMessageELP newInstance = new COImportMessageELP();
        assertThat(newInstance.loadXsdSchema()).isNotEmpty();
    }
}