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
        CASectionJoinCompteAnnexeJoinTiers section = new CASectionJoinCompteAnnexeJoinTiers();
        section.setSolde("100");
        section.setIdSection("2");
        sections.add(section);

        RELigneDeblocageVentilator ventilator = new RELigneDeblocageVentilator(deblocages, sections);
        List<RELigneDeblocageVentilation> ventilations = ventilator.ventil();
        assertThat(ventilations).hasSize(1);
        assertThat(ventilations).contains(newVentialtion(1, 2, 10));
    }

    private RELigneDeblocageVentilation newVentialtion(int i, int j, int montant) {
        RELigneDeblocageVentilation ventilation = new RELigneDeblocageVentilation();
        ventilation.setIdLigneDeblocage(i);
        ventilation.setIdSectionSource(j);
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
        CASectionJoinCompteAnnexeJoinTiers section = new CASectionJoinCompteAnnexeJoinTiers();
        section.setSolde("10");
        section.setIdSection("2");
        sections.add(section);

        section = new CASectionJoinCompteAnnexeJoinTiers();
        section.setSolde("100");
        section.setIdSection("3");
        sections.add(section);

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
        CASectionJoinCompteAnnexeJoinTiers section = new CASectionJoinCompteAnnexeJoinTiers();
        section.setSolde("10");
        section.setIdSection("2");
        sections.add(section);

        RELigneDeblocageVentilator ventilator = new RELigneDeblocageVentilator(deblocages, sections);
        assertThat(ventilator.ventil()).hasSize(1);
    }

    @Test(expected = REDeblocageException.class)
    public void testVentilManqueSection() throws Exception {
        RELigneDeblocages deblocages = new RELigneDeblocages();
        RELigneDeblocage deblocage = new RELigneDeblocage();
        deblocage.setIdEntity("1");
        deblocage.setMontant(new Montant(15));
        deblocages.add(deblocage);

        List<CASectionJoinCompteAnnexeJoinTiers> sections = new ArrayList<CASectionJoinCompteAnnexeJoinTiers>();
        CASectionJoinCompteAnnexeJoinTiers section = new CASectionJoinCompteAnnexeJoinTiers();
        section.setSolde("10");
        section.setIdSection("2");
        sections.add(section);

        RELigneDeblocageVentilator ventilator = new RELigneDeblocageVentilator(deblocages, sections);
        assertThat(ventilator.ventil()).hasSize(1);
    }
}
