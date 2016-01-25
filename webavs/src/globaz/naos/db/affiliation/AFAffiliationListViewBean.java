package globaz.naos.db.affiliation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * Le listViewBean de l'entité Affiliation.
 * 
 * @author: Administrator
 */
public class AFAffiliationListViewBean extends AFAffiliationManager implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getAffiliationId(int index) {
        return ((AFAffiliation) getEntity(index)).getAffiliationId();
    }

    public String getAffilieNumero(int index) {
        return ((AFAffiliation) getEntity(index)).getAffilieNumero();
    }

    public String getDateDebut(int index) {
        return ((AFAffiliation) getEntity(index)).getDateDebut();
    }

    public String getDateFin(int index) {
        return ((AFAffiliation) getEntity(index)).getDateFin();
    }

    public String getIdTiers(int index) {
        return ((AFAffiliation) getEntity(index)).getIdTiers();
    }

    public String getMotifFin(int index) {
        return ((AFAffiliation) getEntity(index)).getMotifFin();
    }

    public String getPersonnaliteJuridique(int index) {
        return ((AFAffiliation) getEntity(index)).getPersonnaliteJuridique();
    }

    public TITiersViewBean getTiers(int index) {
        return ((AFAffiliation) getEntity(index)).getTiers();
    }

    public Boolean getTraitement(int index) {
        return ((AFAffiliation) getEntity(index)).isTraitement();
    }

    public String getTypeAffiliation(int index) {
        return ((AFAffiliation) getEntity(index)).getTypeAffiliation();
    }

    public String getTypeAssocie(int index) {
        return ((AFAffiliation) getEntity(index)).getTypeAssocie();
    }

    public boolean isTaxationPrincipale(int index) {
        try {
            return AFAffiliationUtil.isTaxationPrincipale(((AFAffiliation) getEntity(index)), null);
        } catch (Exception ex) {
            return false;
        }
    }
}
