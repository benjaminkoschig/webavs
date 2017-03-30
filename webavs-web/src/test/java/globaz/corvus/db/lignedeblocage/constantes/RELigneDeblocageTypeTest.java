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
    public void testIsVersementBeneficiaire() throws Exception {
        assertThat(RELigneDeblocageType.fromValue("52863001").isVersementBeneficiaire()).isFalse();
        assertThat(RELigneDeblocageType.fromValue("52863002").isVersementBeneficiaire()).isFalse();
        assertThat(RELigneDeblocageType.fromValue("52863003").isVersementBeneficiaire()).isTrue();
        assertThat(RELigneDeblocageType.fromValue("52863004").isVersementBeneficiaire()).isFalse();
    }

    @Test
    public void testIsImpotsSource() throws Exception {
        assertThat(RELigneDeblocageType.fromValue("52863001").isImpotsSource()).isFalse();
        assertThat(RELigneDeblocageType.fromValue("52863002").isImpotsSource()).isFalse();
        assertThat(RELigneDeblocageType.fromValue("52863003").isImpotsSource()).isFalse();
        assertThat(RELigneDeblocageType.fromValue("52863004").isImpotsSource()).isTrue();
    }

    @Test
    public void testIsCompensation() throws Exception {
        // Dette en compta
        assertThat(RELigneDeblocageType.fromValue("52863002").isCompensation()).isTrue();
        // impot source
        assertThat(RELigneDeblocageType.fromValue("52863004").isCompensation()).isTrue();

        assertThat(RELigneDeblocageType.fromValue("52863001").isCompensation()).isFalse();
        assertThat(RELigneDeblocageType.fromValue("52863003").isCompensation()).isFalse();

    }

    @Test
    public void testIsNotCompensation() throws Exception {
        assertThat(RELigneDeblocageType.fromValue("52863001").isNotCompensation()).isTrue();
        assertThat(RELigneDeblocageType.fromValue("52863003").isNotCompensation()).isTrue();
        assertThat(RELigneDeblocageType.fromValue("52863002").isNotCompensation()).isFalse();
        assertThat(RELigneDeblocageType.fromValue("52863004").isNotCompensation()).isFalse();
    }

}
