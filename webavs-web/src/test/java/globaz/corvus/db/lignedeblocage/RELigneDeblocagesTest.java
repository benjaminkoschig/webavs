package globaz.corvus.db.lignedeblocage;

import static org.fest.assertions.api.Assertions.*;
import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageEtat;
import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageType;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class RELigneDeblocagesTest {

    private List<RELigneDeblocage> createListLigneDeblocage() {
        List<RELigneDeblocage> lignesDeblocage = new ArrayList<RELigneDeblocage>();

        // Comptabilisé (4)
        lignesDeblocage.add(createLigneDeblocage(RELigneDeblocageEtat.COMPTABILISE, RELigneDeblocageType.CREANCIER));
        lignesDeblocage.add(createLigneDeblocage(RELigneDeblocageEtat.COMPTABILISE, RELigneDeblocageType.CREANCIER));
        lignesDeblocage
                .add(createLigneDeblocage(RELigneDeblocageEtat.COMPTABILISE, RELigneDeblocageType.IMPOTS_SOURCE));
        lignesDeblocage
                .add(createLigneDeblocage(RELigneDeblocageEtat.COMPTABILISE, RELigneDeblocageType.IMPOTS_SOURCE));

        // Enregistré (3)
        lignesDeblocage.add(createLigneDeblocage(RELigneDeblocageEtat.ENREGISTRE, RELigneDeblocageType.IMPOTS_SOURCE));
        lignesDeblocage.add(createLigneDeblocage(RELigneDeblocageEtat.ENREGISTRE, RELigneDeblocageType.IMPOTS_SOURCE));
        lignesDeblocage.add(createLigneDeblocage(RELigneDeblocageEtat.ENREGISTRE,
                RELigneDeblocageType.VERSEMENT_BENEFICIAIRE));

        // Validé (1)
        lignesDeblocage.add(createLigneDeblocage(RELigneDeblocageEtat.VALIDE, RELigneDeblocageType.CREANCIER));

        return lignesDeblocage;
    }

    private RELigneDeblocage createLigneDeblocage(RELigneDeblocageEtat etat, RELigneDeblocageType type) {
        RELigneDeblocage ligne = new RELigneDeblocage();

        ligne.setEtat(etat);
        ligne.setType(type);

        return ligne;
    }

    @Test
    public void testGetLigneDeblocageByEtat() throws Exception {
        RELigneDeblocages ld = new RELigneDeblocages();
        ld.addAll(createListLigneDeblocage());

        assertThat(ld.getLigneDeblocageByEtat(RELigneDeblocageEtat.COMPTABILISE).size()).isEqualTo(4);
        assertThat(ld.getLigneDeblocageByEtat(RELigneDeblocageEtat.ENREGISTRE).size()).isEqualTo(3);
        assertThat(ld.getLigneDeblocageByEtat(RELigneDeblocageEtat.VALIDE).size()).isEqualTo(1);

    }

    @Test
    public void testGetLigneDeblocageByType() throws Exception {
        RELigneDeblocages ld = new RELigneDeblocages();
        ld.addAll(createListLigneDeblocage());

        assertThat(ld.getLigneDeblocageByType(RELigneDeblocageType.CREANCIER).size()).isEqualTo(3);
        assertThat(ld.getLigneDeblocageByType(RELigneDeblocageType.DETTE_EN_COMPTA).size()).isEqualTo(0);
        assertThat(ld.getLigneDeblocageByType(RELigneDeblocageType.IMPOTS_SOURCE).size()).isEqualTo(4);
        assertThat(ld.getLigneDeblocageByType(RELigneDeblocageType.VERSEMENT_BENEFICIAIRE).size()).isEqualTo(1);
    }

    @Test
    public void testGetLigneDeblocageComptabilise() throws Exception {
        RELigneDeblocages ld = new RELigneDeblocages();
        ld.addAll(createListLigneDeblocage());

        assertThat(ld.getLigneDeblocageComptabilise().size()).isEqualTo(4);
    }

    @Test
    public void testGetLigneDeblocageEnregistre() throws Exception {
        RELigneDeblocages ld = new RELigneDeblocages();
        ld.addAll(createListLigneDeblocage());

        assertThat(ld.getLigneDeblocageEnregistre().size()).isEqualTo(3);
    }

    @Test
    public void testGetLigneDeblocageValide() throws Exception {
        RELigneDeblocages ld = new RELigneDeblocages();
        ld.addAll(createListLigneDeblocage());

        assertThat(ld.getLigneDeblocageValide().size()).isEqualTo(1);
    }

    @Test
    public void testGetLigneDeblocageCreancier() throws Exception {
        RELigneDeblocages ld = new RELigneDeblocages();
        ld.addAll(createListLigneDeblocage());

        assertThat(ld.getLigneDeblocageCreancier().size()).isEqualTo(3);
    }

    @Test
    public void testGetLigneDeblocageDetteEnCompta() throws Exception {
        RELigneDeblocages ld = new RELigneDeblocages();
        ld.addAll(createListLigneDeblocage());

        assertThat(ld.getLigneDeblocageDetteEnCompta().size()).isEqualTo(0);
    }

    @Test
    public void testGetLigneDeblocageImpotsSource() throws Exception {
        RELigneDeblocages ld = new RELigneDeblocages();
        ld.addAll(createListLigneDeblocage());

        assertThat(ld.getLigneDeblocageImpotsSource().size()).isEqualTo(4);
    }

    @Test
    public void testGetLigneDeblocageVersementBeneficaire() throws Exception {
        RELigneDeblocages ld = new RELigneDeblocages();
        ld.addAll(createListLigneDeblocage());

        assertThat(ld.getLigneDeblocageVersementBeneficaire().size()).isEqualTo(1);
    }
}
