package globaz.corvus.db.deblocage;

import globaz.corvus.db.lignedeblocage.RELigneDeblocage;
import globaz.corvus.db.lignedeblocage.RELigneDeblocages;
import globaz.corvus.db.lignedeblocageventilation.RELigneDeblocageVentilation;
import globaz.osiris.db.comptes.CASectionJoinCompteAnnexeJoinTiers;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ch.globaz.common.domaine.Montant;

class RELigneDeblocageVentilator {
    private final RELigneDeblocages lignesDeblocages;
    private final List<CASectionJoinCompteAnnexeJoinTiers> sections;
    private final Montant sumDeblocage;

    public RELigneDeblocageVentilator(RELigneDeblocages lignesDeblocages,
            List<CASectionJoinCompteAnnexeJoinTiers> sections) {

        sumDeblocage = sumDeblocage(lignesDeblocages).abs();
        Montant sumSection = sumSection(sections).abs();

        if (sumDeblocage.greater(sumSection)) {
            throw new REDeblocageException("Not enough amount in the all sections");
        }

        Comparator<CASectionJoinCompteAnnexeJoinTiers> comparator = new Comparator<CASectionJoinCompteAnnexeJoinTiers>() {
            @Override
            public int compare(CASectionJoinCompteAnnexeJoinTiers o1, CASectionJoinCompteAnnexeJoinTiers o2) {
                return Integer.valueOf(o1.getIdExterne()).compareTo(Integer.valueOf(o2.getIdExterne()));
            }
        };
        Collections.sort(sections, comparator);
        this.sections = sections;
        this.lignesDeblocages = lignesDeblocages;
    }

    public List<RELigneDeblocageVentilation> ventil() {
        Montant totalMontantVentile = Montant.ZERO;

        List<RELigneDeblocageVentilation> ventilations = new ArrayList<RELigneDeblocageVentilation>();

        for (RELigneDeblocage ligne : lignesDeblocages) {
            ventilations.addAll(ventilBySection(ligne));
            totalMontantVentile = totalMontantVentile.add(ligne.getMontant());
        }

        if (!sumVentilation(ventilations).equals(sumDeblocage)) {
            throw new REDeblocageException(
                    "Le montant total des ventilations n'es pas egale au montant des lignes de déblocages !");
        }

        return ventilations;
    }

    private List<RELigneDeblocageVentilation> ventilBySection(RELigneDeblocage ligne) {
        List<RELigneDeblocageVentilation> ventiliations = new ArrayList<RELigneDeblocageVentilation>();
        Montant montantAVantiler = ligne.getMontant();
        Collections.sort(sections, new SectionsComparator());
        List<CASectionJoinCompteAnnexeJoinTiers> orderSections = getSectionsCompteAnnexeFirst(sections,
                ligne.getIdCompteAnnexe(), montantAVantiler);

        for (CASectionJoinCompteAnnexeJoinTiers section : orderSections) {
            Montant resteInSection = new Montant(section.getSolde()).abs();
            if (Montant.ZERO.less(resteInSection)) {
                if (!resteInSection.greaterOrEquals(montantAVantiler)) {
                    montantAVantiler = montantAVantiler.substract(resteInSection);
                    ventiliations.add(newVentilation(section, ligne.getIdEntity(), resteInSection));
                } else {
                    resteInSection = resteInSection.substract(montantAVantiler);
                    ventiliations.add(newVentilation(section, ligne.getIdEntity(), montantAVantiler));
                    montantAVantiler = Montant.ZERO;
                }
            }
            section.setSolde(resteInSection.getValue());
            if (montantAVantiler.isZero()) {
                break;
            }
        }
        return ventiliations;
    }

    /**
     * Retourne la liste des sections avec en premier : les sections correspondantes au compte annexe puis les sections
     * avec le montant correspondant
     * 
     * @param sections
     * @param idCompteAnnexe
     * @param montant
     * @return
     */
    private List<CASectionJoinCompteAnnexeJoinTiers> getSectionsCompteAnnexeFirst(
            List<CASectionJoinCompteAnnexeJoinTiers> sections, String idCompteAnnexe, Montant montant) {
        List<CASectionJoinCompteAnnexeJoinTiers> sectionsTiers = new ArrayList<CASectionJoinCompteAnnexeJoinTiers>();
        List<CASectionJoinCompteAnnexeJoinTiers> sectionsFamily = new ArrayList<CASectionJoinCompteAnnexeJoinTiers>();
        for (CASectionJoinCompteAnnexeJoinTiers section : sections) {
            if (idCompteAnnexe.equals(section.getIdCompteAnnexe())) {
                if (section.getSolde().equals(montant.negate().toStringValue())) {
                    sectionsTiers.add(0, section);
                } else {
                    sectionsTiers.add(section);
                }
            } else {
                if (section.getSolde().equals(montant.negate().toStringValue())) {
                    sectionsFamily.add(0, section);
                } else {
                    sectionsFamily.add(section);
                }
            }
        }
        sectionsTiers.addAll(sectionsFamily);
        return sectionsTiers;
    }

    class SectionsComparator implements Comparator<CASectionJoinCompteAnnexeJoinTiers> {
        @Override
        public int compare(CASectionJoinCompteAnnexeJoinTiers a, CASectionJoinCompteAnnexeJoinTiers b) {
            return Integer.valueOf(a.getIdSection()) < Integer.valueOf(b.getIdSection()) ? -1 : Integer.valueOf(a
                    .getIdSection()) == Integer.valueOf(b.getIdSection()) ? 0 : 1;
        }
    }

    private RELigneDeblocageVentilation newVentilation(CASectionJoinCompteAnnexeJoinTiers section,
            String idLigneDeblocate, Montant montant) {
        RELigneDeblocageVentilation ventilation = new RELigneDeblocageVentilation();
        ventilation.setMontant(montant);
        ventilation.setIdLigneDeblocage(Long.valueOf(idLigneDeblocate));
        ventilation.setIdSectionSource(Long.valueOf(section.getIdSection()));
        return ventilation;
    }

    private Montant sumSection(List<CASectionJoinCompteAnnexeJoinTiers> sections) {
        Montant sum = Montant.ZERO;
        for (CASectionJoinCompteAnnexeJoinTiers section : sections) {
            sum = sum.add(section.getSolde());
        }
        return sum;
    }

    private Montant sumDeblocage(Collection<RELigneDeblocage> deblocages) {
        Montant sum = Montant.ZERO;
        for (RELigneDeblocage deblocage : deblocages) {
            sum = sum.add(deblocage.getMontant());
        }
        return sum;
    }

    private Montant sumVentilation(Collection<RELigneDeblocageVentilation> ventilations) {
        Montant sum = Montant.ZERO;
        for (RELigneDeblocageVentilation ventilation : ventilations) {
            sum = sum.add(ventilation.getMontant());
        }
        return sum;
    }
}