package ch.globaz.pegasus.business.domaine.droit;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

public class EtatDroitTest {

    @Test
    public void testIsValideTrue() throws Exception {
        assertThat(EtatDroit.VALIDE.isValide()).isTrue();
    }

    @Test
    public void testIsValideFalse() throws Exception {
        assertThat(EtatDroit.ENREGISTRE.isValide()).isFalse();
    }

    @Test
    public void testIsEnregistreTrue() throws Exception {
        assertThat(EtatDroit.ENREGISTRE.isEnregistre()).isTrue();
    }

    @Test
    public void testIsEnregistrefalse() throws Exception {
        assertThat(EtatDroit.VALIDE.isEnregistre()).isFalse();
    }

    @Test
    public void testIsAuCalculTrue() throws Exception {
        assertThat(EtatDroit.AU_CALCUL.isAuCalcul()).isTrue();
    }

    @Test
    public void testIsAuCalculFalse() throws Exception {
        assertThat(EtatDroit.ENREGISTRE.isAuCalcul()).isFalse();
    }

    @Test
    public void testIsCalculeTrue() throws Exception {
        assertThat(EtatDroit.CALCULE.isCalcule()).isTrue();
    }

    @Test
    public void testIsCalculeFalse() throws Exception {
        assertThat(EtatDroit.ENREGISTRE.isCalcule()).isFalse();
    }

    @Test
    public void testIsHistoriseTrue() throws Exception {
        assertThat(EtatDroit.HISTORISE.isHistorise()).isTrue();
    }

    @Test
    public void testIsHistoriseFalse() throws Exception {
        assertThat(EtatDroit.ENREGISTRE.isHistorise()).isFalse();
    }

}
