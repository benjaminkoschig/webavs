package globaz.prestation.api;

import ch.globaz.common.util.TestEnums;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PRTypeDemandeTest {

    @Test
    public void isProcheAidant_correct() {
        TestEnums.assertFunctionEqualEnum(PRTypeDemande.class, PRTypeDemande::isProcheAidant, PRTypeDemande.PROCHE_AIDANT);
    }

    @Test
    public void toEnumByCs_avecBonCode_retourneEnum() {
        assertThat(PRTypeDemande.toEnumByCs(PRTypeDemande.PROCHE_AIDANT.getCsType())).isEqualTo(PRTypeDemande.PROCHE_AIDANT);
    }

    @Test
    public void toEnumByCs_avecMauvaisCode_exception() {
        Assertions.assertThatThrownBy(() -> PRTypeDemande.toEnumByCs("22222")).hasMessageContaining("22222");
    }
}
