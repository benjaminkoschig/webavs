package globaz.corvus.db.deblocage;

import globaz.corvus.db.lignedeblocage.RELigneDeblocages;
import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDues;
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
    private final Montant montantEnComptat;

    public REDeblocage(RELigneDeblocages lignesDeblocages, PRTiersWrapper beneficiaire, REEnteteBlocage enteteBlocage,
            RERenteAccordeeJoinInfoComptaJoinPrstDues pracc, String descriptonTier, List<ReRetour> retours,
            Montant montantEnComptat) {
        this.lignesDeblocages = lignesDeblocages;
        this.beneficiaire = beneficiaire;
        this.enteteBlocage = enteteBlocage;
        this.pracc = pracc;
        this.descriptonTier = descriptonTier;
        this.retours = retours;
        this.montantEnComptat = montantEnComptat;
    }

    public Montant computeMontantAdebloquer() {
        return new Montant(enteteBlocage.getMontantBloque()).substract(new Montant(enteteBlocage.getMontantDebloque()));
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

    public Montant getMontantEnComptat() {
        return montantEnComptat;
    }

}
