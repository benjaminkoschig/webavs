package globaz.naos.db.suiviAssurance;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.naos.db.assurance.AFAssurance;

/**
 * Le listViewBean de l'entité SuiviAssurance.
 * 
 * @author administrator
 */
public class AFSuiviAssuranceListViewBean extends AFSuiviAssuranceManager implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AFAssurance getAssurance(int index) {

        return ((AFSuiviAssurance) getEntity(index)).getAssurance();
    }

    public String getAssuranceId(int index) {

        return ((AFSuiviAssurance) getEntity(index)).getAssuranceId();
    }

    public String getDateEffective(int index) {

        return ((AFSuiviAssurance) getEntity(index)).getDateEffective();
    }

    public String getDateFin(int index) {

        return ((AFSuiviAssurance) getEntity(index)).getDateFin();
    }

    public String getDatePrevue(int index) {

        return ((AFSuiviAssurance) getEntity(index)).getDatePrevue();
    }

    public String getStatut(int index) {

        return ((AFSuiviAssurance) getEntity(index)).getStatut();
    }

    public String getSuiviAssuranceId(int index) {

        return ((AFSuiviAssurance) getEntity(index)).getSuiviAssuranceId();
    }
}
