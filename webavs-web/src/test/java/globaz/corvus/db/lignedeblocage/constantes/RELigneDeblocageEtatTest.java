package globaz.corvus.db.lignedeblocage.constantes;

import static org.fest.assertions.api.Assertions.*;
import org.junit.Test;

public class RELigneDeblocageEtatTest {

    @Test
    public void testIsComptabilise() throws Exception {
        assertThat(RELigneDeblocageEtat.fromValue("52862001").isComptabilise()).isFalse();
        assertThat(RELigneDeblocageEtat.fromValue("52862002").isComptabilise()).isFalse();
        assertThat(RELigneDeblocageEtat.fromValue("52862003").isComptabilise()).isTrue();
    }

    @Test
    public void testIsEnregistre() throws Exception {
        assertThat(RELigneDeblocageEtat.fromValue("52862001").isEnregistre()).isTrue();
        assertThat(RELigneDeblocageEtat.fromValue("52862002").isEnregistre()).isFalse();
        assertThat(RELigneDeblocageEtat.fromValue("52862003").isEnregistre()).isFalse();
    }

    @Test
    public void testIsValide() throws Exception {
        assertThat(RELigneDeblocageEtat.fromValue("52862001").isValide()).isFalse();
        assertThat(RELigneDeblocageEtat.fromValue("52862002").isValide()).isTrue();
        assertThat(RELigneDeblocageEtat.fromValue("52862003").isValide()).isFalse();
    }

}
