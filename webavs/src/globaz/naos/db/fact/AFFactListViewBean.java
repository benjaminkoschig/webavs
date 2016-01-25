package globaz.naos.db.fact;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;

/**
 * Le listViewBean de l'entité facturation.
 * 
 * @author: Administrator
 */
public class AFFactListViewBean extends AFFactManager implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AFAffiliation getAffiliation(int index) throws Exception {

        return ((AFFact) getEntity(index)).getAffiliation();
    }

    public String getAffiliationId(int index) {

        return ((AFFact) getEntity(index)).getAffiliationId();
    }

    public AFCotisation getCotisation(int index) throws Exception {

        return ((AFFact) getEntity(index)).getCotisation();
    }

    public String getCotisationId(int index) {

        return ((AFFact) getEntity(index)).getCotisationId();
    }

    public String getDateDebut(int index) {

        return ((AFFact) getEntity(index)).getDateDebut();
    }

    public String getDateFin(int index) {

        return ((AFFact) getEntity(index)).getDateFin();
    }

    public Boolean getExtourner(int index) {

        return ((AFFact) getEntity(index)).isExtourner();
    }

    public String getFacturationId(int index) {

        return ((AFFact) getEntity(index)).getFacturationId();
    }

    public String getMassePeriodiciteNouveau(int index) {

        return ((AFFact) getEntity(index)).getMassePeriodiciteNouveau();
    }

    public String getMontant(int index) {

        return ((AFFact) getEntity(index)).getMontant();
    }

    public String getPassageId(int index) {

        return ((AFFact) getEntity(index)).getPassageId();
    }

    public String getTypeFacturation(int index) {

        return ((AFFact) getEntity(index)).getTypeFacturation();
    }
}
