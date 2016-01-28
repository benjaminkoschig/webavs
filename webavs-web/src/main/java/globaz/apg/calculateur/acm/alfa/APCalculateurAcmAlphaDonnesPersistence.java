package globaz.apg.calculateur.acm.alfa;

import globaz.apg.db.droits.APSitProJointEmployeur;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Container d'entités représentant la prestation apg standard et ses situations professionnelles
 * 
 * @author jje
 */
public class APCalculateurAcmAlphaDonnesPersistence {

    private String idDroit = "";
    private List<APRepartitionJointPrestation> prestationJointRepartitions = null;
    private List<APSitProJointEmployeur> situationProfessionnelleEmployeur = null;

    // Map<idSituationProfessionnelle, {taux AVS par, taux AC par,taux FNE par}>
    private Map<String, BigDecimal[]> taux = new HashMap<String, BigDecimal[]>();

    public String getIdDroit() {
        return idDroit;
    }

    public List<APRepartitionJointPrestation> getPrestationJointRepartitions() {
        return prestationJointRepartitions;
    }

    public List<APSitProJointEmployeur> getSituationProfessionnelleEmployeur() {
        return situationProfessionnelleEmployeur;
    }

    /**
     * Map<idSituationProfessionnelle, {taux AVS par, taux AC par,taux FNE par}>
     * 
     * @return Map<String, BigDecimal[]>
     */
    public Map<String, BigDecimal[]> getTaux() {
        return taux;
    }

    public void setIdDroit(final String idDroit) {
        this.idDroit = idDroit;
    }

    public void setPrestations(final List<APRepartitionJointPrestation> prestationJointRepartitions) {
        this.prestationJointRepartitions = prestationJointRepartitions;
    }

    public void setSituationProfessionnelleEmployeur(
            final List<APSitProJointEmployeur> situationProfessionnelleEmployeur) {
        this.situationProfessionnelleEmployeur = situationProfessionnelleEmployeur;
    }

    public void setTaux(final Map<String, BigDecimal[]> taux) {
        this.taux = taux;
    }

}
