/*
 * Created on 28-Jan-05
 */
package globaz.naos.db.nombreAssures;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.naos.db.assurance.AFAssurance;

/**
 * Le listViewBean de l'entité NombreAssures.
 * 
 * @author sau
 */
public class AFNombreAssuresListViewBean extends AFNombreAssuresManager implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getAffiliationId(int index) {

        return ((AFNombreAssures) getEntity(index)).getAffiliationId();
    }

    public String getAnnee(int index) {

        return ((AFNombreAssures) getEntity(index)).getAnnee();
    }

    public AFAssurance getAssurance(int index) {

        return ((AFNombreAssures) getEntity(index)).getAssurance();
    }

    public String getAssuranceId(int index) {

        return ((AFNombreAssures) getEntity(index)).getAssuranceId();
    }

    public String getNbrAssures(int index) {

        return ((AFNombreAssures) getEntity(index)).getNbrAssures();
    }

    public String getNbrAssuresId(int index) {

        return ((AFNombreAssures) getEntity(index)).getNbrAssuresId();
    }
}
