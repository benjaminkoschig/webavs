package globaz.corvus.db.lignedeblocage.constantes;

import static org.fest.assertions.api.Assertions.*;
import org.junit.Test;

public class RELigneDeblocageTypeTest {

    @Test
    public void testIsCreancier() throws Exception {
        assertThat(RELigneDeblocageType.fromValue("52863001").isCreancier()).isTrue();
        assertThat(RELigneDeblocageType.fromValue("52863002").isCreancier()).isFalse();
        assertThat(RELigneDeblocageType.fromValue("52863003").isCreancier()).isFalse();
        assertThat(RELigneDeblocageType.fromValue("52863004").isCreancier()).isFalse();
    }

    @Test
    public void testIsDetteEnCompta() throws Exception {
        assertThat(RELigneDeblocageType.fromValue("52863001").isDetteEnCompta()).isFalse();
        assertThat(RELigneDeblocageType.fromValue("52863002").isDetteEnCompta()).isTrue();
        assertThat(RELigneDeblocageType.fromValue("52863003").isDetteEnCompta()).isFalse();
        assertThat(RELigneDeblocageType.fromValue("52863004").isDetteEnCompta()).isFalse();
    }

    @Test
    public void testIsVersementBeneficaire() throws Exception {
        assertThat(RELigneDeblocageType.fromValue("52863001").isVersementBeneficaire()).isFalse();
        assertThat(RELigneDeblocageType.fromValue("52863002").isVersementBeneficaire()).isFalse();
        assertThat(RELigneDeblocageType.fromValue("52863003").isVersementBeneficaire()).isTrue();
        assertThat(RELigneDeblocageType.fromValue("52863004").isVersementBeneficaire()).isFalse();
    }

    @Test
    public void testIsImpotsSource() throws Exception {
        assertThat(RELigneDeblocageType.fromValue("52863001").isImpotsSource()).isFalse();
        assertThat(RELigneDeblocageType.fromValue("52863002").isImpotsSource()).isFalse();
        assertThat(RELigneDeblocageType.fromValue("52863003").isImpotsSource()).isFalse();
        assertThat(RELigneDeblocageType.fromValue("52863004").isImpotsSource()).isTrue();
    }

}
