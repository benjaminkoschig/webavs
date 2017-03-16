package globaz.corvus.db.deblocage;

import globaz.corvus.db.lignedeblocage.RELigneDeblocages;
import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.List;
import ch.globaz.common.domaine.Montant;
import ch.globaz.osiris.business.model.SoldeCompteCourant;

public class REDeblocage {
    private final RELigneDeblocages lignesDeblocages;
    private final PRTiersWrapper beneficiaire;
    private final REEnteteBlocage enteteBlocage;
    private final REPrestationsAccordees pracc;
    private final String descriptonTier;
    private final List<ReRetour> retours;
    private final List<SoldeCompteCourant> soldeCompteCourants;

    public REDeblocage(RELigneDeblocages lignesDeblocages, PRTiersWrapper beneficiaire, REEnteteBlocage enteteBlocage,
            REPrestationsAccordees pracc, String descriptonTier, List<ReRetour> retours,
            List<SoldeCompteCourant> soldeCompteCourants) {
        this.lignesDeblocages = lignesDeblocages;
        this.beneficiaire = beneficiaire;
        this.enteteBlocage = enteteBlocage;
        this.pracc = pracc;
        this.descriptonTier = descriptonTier;
        this.retours = retours;
        this.soldeCompteCourants = soldeCompteCourants;
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

    public REPrestationsAccordees getPracc() {
        return pracc;
    }

    public String getDescriptonTier() {
        return descriptonTier;
    }

    public List<ReRetour> getRetours() {
        return retours;
    }

    public List<SoldeCompteCourant> getSoldeCompteCourants() {
        return soldeCompteCourants;
    }

}
