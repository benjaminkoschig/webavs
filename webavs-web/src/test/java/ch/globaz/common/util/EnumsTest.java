package ch.globaz.common.util;

import globaz.prestation.api.PRTypeDemande;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EnumsTest {

    @Test
    public void toEnum_avecBonCode_retourneEnum() {
        assertThat(Enums.toEnum(PRTypeDemande.PROCHE_AIDANT.getCsType(), PRTypeDemande.class, PRTypeDemande::getCsType))
                .isEqualTo(PRTypeDemande.PROCHE_AIDANT);
    }

    @Test
    public void toEnum_avecMauvaisCode_exception() {
        Assertions.assertThatThrownBy(() -> Enums.toEnum("22222", PRTypeDemande.class, PRTypeDemande::getCsType)).hasMessageContaining("22222");
    }

    @Test
    public void toEnumWithoutException_withNull_returnNull() {
        assertThat(Enums.toEnumWithoutException(null, PRTypeDemande.class, PRTypeDemande::getCsType)).isNotPresent();
        assertThat(Enums.toEnumWithoutException(" ", PRTypeDemande.class, PRTypeDemande::getCsType)).isNotPresent();
    }

}
