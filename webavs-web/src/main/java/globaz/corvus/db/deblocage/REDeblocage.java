package globaz.corvus.db.deblocage;

import globaz.corvus.db.lignedeblocage.RELigneDeblocages;
import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDues;
import globaz.osiris.db.comptes.CASectionJoinCompteAnnexeJoinTiers;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.List;
import ch.globaz.common.domaine.Montant;

public class REDeblocage {
    private final RELigneDeblocages lignesDeblocages;
    private final PRTiersWrapper beneficiaire;
    private final REEnteteBlocage enteteBlocage;
    private final RERenteAccordeeJoinInfoComptaJoinPrstDues pracc;
    private final String descriptonTier;
    private final List<ReRetour> retours;
    private final List<CASectionJoinCompteAnnexeJoinTiers> sections;
    private final Montant montantEnComptat;

    private final Montant montantDebloquer;

    public REDeblocage(RELigneDeblocages lignesDeblocages, PRTiersWrapper beneficiaire, REEnteteBlocage enteteBlocage,
            RERenteAccordeeJoinInfoComptaJoinPrstDues pracc, String descriptonTier, List<ReRetour> retours,
            List<CASectionJoinCompteAnnexeJoinTiers> sections) {
        this.lignesDeblocages = lignesDeblocages;
        this.beneficiaire = beneficiaire;
        this.enteteBlocage = enteteBlocage;
        this.pracc = pracc;
        this.descriptonTier = descriptonTier;
        this.retours = retours;
        this.sections = sections;
        montantEnComptat = sum(sections).abs();
        montantDebloquer = new Montant(enteteBlocage.getMontantDebloque());
    }

    public Montant computeMontantAdebloquerEntete() {
        return new Montant(enteteBlocage.getMontantBloque()).substract(montantDebloquer);
    }

    public Montant computeMontantToUseForDeblocage() {
        return computeMontantAdebloquerEntete().min(montantEnComptat);
    }

    public RELigneDeblocages getLignesDeblocages() {
        return lignesDeblocages;
    }

    public PRTiersWrapper getBeneficiaire() {
        return beneficiaire;
    }

    public REEnteteBlocage getEnteteBlocage() {
        return enteteBlocage;
    }

    public RERenteAccordeeJoinInfoComptaJoinPrstDues getPracc() {
        return pracc;
    }

    public String getDescriptonTier() {
        return descriptonTier;
    }

    public List<ReRetour> getRetours() {
        return retours;
    }

    public List<CASectionJoinCompteAnnexeJoinTiers> getSections() {
        return sections;
    }

    public Montant getMontantEnComptat() {
        return montantEnComptat;
    }

    public Montant getMontantDebloquer() {
        return montantDebloquer;
    }

    public Montant computTotalMontantDebloquer() {
        return getMontantDebloquer().add(getLignesDeblocages().filtreEnregistres().sumMontants());
    }

    private Montant sum(List<CASectionJoinCompteAnnexeJoinTiers> sections) {
        Montant montant = Montant.ZERO;
        for (CASectionJoinCompteAnnexeJoinTiers section : sections) {
            montant = montant.add(section.getSolde());
        }
        return montant;
    }
}
