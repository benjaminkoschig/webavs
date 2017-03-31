package globaz.corvus.db.deblocage;

import static org.fest.assertions.api.Assertions.*;
import globaz.corvus.db.lignedeblocage.RELigneDeblocage;
import globaz.corvus.db.lignedeblocage.RELigneDeblocages;
import globaz.corvus.db.lignedeblocageventilation.RELigneDeblocageVentilation;
import globaz.osiris.db.comptes.CASectionJoinCompteAnnexeJoinTiers;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;

public class RELigneDeblocageVentilatorTest {

    @Test
    public void testVentilSoldeSuffisant() throws Exception {
        RELigneDeblocages deblocages = new RELigneDeblocages();
        RELigneDeblocage deblocage = new RELigneDeblocage();
        deblocage.setIdEntity("1");
        deblocage.setMontant(new Montant(10));
        deblocages.add(deblocage);

        List<CASectionJoinCompteAnnexeJoinTiers> sections = new ArrayList<CASectionJoinCompteAnnexeJoinTiers>();
        sections.add(createSection(100, 2));

        RELigneDeblocageVentilator ventilator = new RELigneDeblocageVentilator(deblocages, sections);
        List<RELigneDeblocageVentilation> ventilations = ventilator.ventil();
        assertThat(ventilations).hasSize(1);
        assertThat(ventilations).contains(newVentialtion(1, 2, 10));
    }

    private RELigneDeblocageVentilation newVentialtion(int i, int j, int montant) {
        RELigneDeblocageVentilation ventilation = new RELigneDeblocageVentilation();
        ventilation.setIdLigneDeblocage(Long.valueOf(i));
        ventilation.setIdSectionSource(Long.valueOf(j));
        ventilation.setMontant(new Montant(montant));
        return ventilation;
    }

    @Test
    public void testVentilPermierSoldeInsufisanteDeblocage() throws Exception {
        RELigneDeblocages deblocages = new RELigneDeblocages();
        RELigneDeblocage deblocage = new RELigneDeblocage();
        deblocage.setIdEntity("1");
        deblocage.setMontant(new Montant(15));
        deblocages.add(deblocage);

        List<CASectionJoinCompteAnnexeJoinTiers> sections = new ArrayList<CASectionJoinCompteAnnexeJoinTiers>();
        sections.add(createSection(10, 1));
        sections.add(createSection(100, 3));

        RELigneDeblocageVentilator ventilator = new RELigneDeblocageVentilator(deblocages, sections);
        List<RELigneDeblocageVentilation> ventilations = ventilator.ventil();
        assertThat(ventilations).hasSize(2);
        assertThat(ventilations).contains(newVentialtion(1, 2, 10), newVentialtion(1, 3, 5));
    }

    @Test
    public void testVentilSoldeEgaleDeblocage() throws Exception {
        RELigneDeblocages deblocages = new RELigneDeblocages();
        RELigneDeblocage deblocage = new RELigneDeblocage();
        deblocage.setIdEntity("1");
        deblocage.setMontant(new Montant(10));
        deblocages.add(deblocage);

        List<CASectionJoinCompteAnnexeJoinTiers> sections = new ArrayList<CASectionJoinCompteAnnexeJoinTiers>();
        sections.add(createSection(10, 1));

        RELigneDeblocageVentilator ventilator = new RELigneDeblocageVentilator(deblocages, sections);
        assertThat(ventilator.ventil()).hasSize(1);
    }

    @Test
    public void testVentilMontantSurTroisSection() throws Exception {
        RELigneDeblocages deblocages = new RELigneDeblocages();
        RELigneDeblocage deblocage = new RELigneDeblocage();
        deblocage.setIdEntity("1");
        deblocage.setMontant(new Montant(30));
        deblocages.add(deblocage);

        List<CASectionJoinCompteAnnexeJoinTiers> sections = new ArrayList<CASectionJoinCompteAnnexeJoinTiers>();
        sections.add(createSection(10, 1));
        sections.add(createSection(15, 2));
        sections.add(createSection(10, 3));
        sections.add(createSection(16, 4));

        RELigneDeblocageVentilator ventilator = new RELigneDeblocageVentilator(deblocages, sections);
        assertThat(ventilator.ventil()).hasSize(3);
    }

    @Test(expected = REDeblocageException.class)
    public void testVentilManqueSection() throws Exception {
        RELigneDeblocages deblocages = new RELigneDeblocages();
        RELigneDeblocage deblocage = new RELigneDeblocage();
        deblocage.setIdEntity("1");
        deblocage.setMontant(new Montant(15));
        deblocages.add(deblocage);

        List<CASectionJoinCompteAnnexeJoinTiers> sections = new ArrayList<CASectionJoinCompteAnnexeJoinTiers>();
        sections.add(createSection(10, 1));

        RELigneDeblocageVentilator ventilator = new RELigneDeblocageVentilator(deblocages, sections);
        assertThat(ventilator.ventil()).hasSize(1);
    }

    private CASectionJoinCompteAnnexeJoinTiers createSection(Integer solde, Integer idSection) {
        CASectionJoinCompteAnnexeJoinTiers section = new CASectionJoinCompteAnnexeJoinTiers();
        section.setSolde(solde.toString());
        section.setIdSection(idSection.toString());
        section.setIdExterne(idSection + "10");
        return section;
    }
}
