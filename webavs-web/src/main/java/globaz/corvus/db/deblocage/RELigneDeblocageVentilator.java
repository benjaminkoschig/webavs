package globaz.corvus.db.deblocage;

import globaz.corvus.db.lignedeblocage.RELigneDeblocage;
import globaz.corvus.db.lignedeblocage.RELigneDeblocages;
import globaz.corvus.db.lignedeblocageventilation.RELigneDeblocageVentilation;
import globaz.osiris.db.comptes.CASectionJoinCompteAnnexeJoinTiers;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Montant;

class RELigneDeblocageVentilator {
    private final RELigneDeblocages lignesDeblocages;
    private final List<CASectionJoinCompteAnnexeJoinTiers> sections;
    private final Montant totalMontantVentile = Montant.ZERO;

    public RELigneDeblocageVentilator(RELigneDeblocages lignesDeblocages,
            List<CASectionJoinCompteAnnexeJoinTiers> sections) {
        this.lignesDeblocages = lignesDeblocages;
        this.sections = sections;
    }

    public List<RELigneDeblocageVentilation> ventil() {
        CASectionJoinCompteAnnexeJoinTiers section = sections.remove(0);
        Montant resteInSection = new Montant(section.getSolde()).abs();
        List<RELigneDeblocageVentilation> ventiliations = new ArrayList<RELigneDeblocageVentilation>();
        for (RELigneDeblocage ligne : lignesDeblocages) {
            Montant montantVentialition = ligne.getMontant();
            if (!resteInSection.greaterOrEquals(montantVentialition)) {

                ventiliations.add(newVentilation(section, ligne.getIdEntity(), resteInSection));
                if (resteInSection.less(montantVentialition)) {
                    if (sections.isEmpty()) {
                        throw new REDeblocageException("not enough section to ventilate this idLigneDeblocage"
                                + ligne.getIdEntity());
                    }

                    section = sections.remove(0);
                    Montant montantSection = new Montant(section.getSolde()).abs();
                    montantVentialition = montantSection.substract(resteInSection);
                }
            }
            ventiliations.add(newVentilation(section, ligne.getIdEntity(), montantVentialition));
            resteInSection = resteInSection.substract(ligne.getMontant());
        }
        return ventiliations;
    }

    private RELigneDeblocageVentilation newVentilation(CASectionJoinCompteAnnexeJoinTiers section,
            String idLigneDeblocate, Montant montant) {
        RELigneDeblocageVentilation ventilation = new RELigneDeblocageVentilation();
        ventilation.setMontant(montant);
        ventilation.setIdLigneDeblocage(Long.valueOf(idLigneDeblocate));
        ventilation.setIdSectionSource(Long.valueOf(section.getIdSection()));
        return ventilation;
    }
}
