/*
 * Created on 28-Jan-05
 */
package globaz.naos.db.couverture;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.naos.db.assurance.AFAssurance;

/**
 * Le listViewBean de l'entité Couverture.
 * 
 * @author sau
 */
public class AFCouvertureListViewBean extends AFCouvertureManager implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AFAssurance getAssurance(int index) {
        return ((AFCouverture) getEntity(index)).getAssurance();
    }

    public String getAssuranceId(int index) {

        return ((AFCouverture) getEntity(index)).getAssuranceId();
    }

    public String getCouvertureId(int index) {

        return ((AFCouverture) getEntity(index)).getCouvertureId();
    }

    public String getDateDebut(int index) {

        return ((AFCouverture) getEntity(index)).getDateDebut();
    }

    public String getDateFin(int index) {
        return ((AFCouverture) getEntity(index)).getDateFin();
    }

    public String getPlanCaisseId(int index) {

        return ((AFCouverture) getEntity(index)).getPlanCaisseId();
    }
}
