package ch.globaz.al.business.models.prestation.paiement;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

/**
 * Modèle de recherche permettant d'effectuer des recherches sur le modèle {@link CheckAffiliationComplexModel}
 * 
 * @author jts
 * 
 */
public class CheckAffiliationSearchComplexModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Critère de recherche sur une liste de numéro d'affilié
     */
    Collection<String> inIdRecap = null;

    /**
     * @return the inIdRecap
     */
    public Collection<String> getInIdRecap() {
        return inIdRecap;
    }

    /**
     * @param inIdRecap
     *            the inIdRecap to set
     */
    public void setInIdRecap(Collection<String> inIdRecap) {
        this.inIdRecap = inIdRecap;
    }

    @Override
    public Class<CheckAffiliationComplexModel> whichModelClass() {
        return CheckAffiliationComplexModel.class;
    }
}
