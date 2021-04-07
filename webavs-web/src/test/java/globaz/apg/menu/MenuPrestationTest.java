package globaz.apg.menu;

import globaz.prestation.api.IPRDemande;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuPrestationTest {

    @Test
    public void of_avecTypeDePresation_ok() {
        MenuPrestation menuPrestation = MenuPrestation.of(IPRDemande.CS_TYPE_APG);
        assertThat(menuPrestation).isNotNull();
        assertThat(menuPrestation.getCsTypePrestation()).isEqualTo(IPRDemande.CS_TYPE_APG);
    }
}