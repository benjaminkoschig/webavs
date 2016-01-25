package globaz.naos.db.parametreAssurance;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Le listViewBean de l'entité ParametreAssurance.
 * 
 * @author sau
 */
public class AFParametreAssuranceListViewBean extends AFParametreAssuranceManager implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getAssuranceId(int index) {

        return ((AFParametreAssurance) getEntity(index)).getAssuranceId();
    }

    public String getDateDebut(int index) {

        return ((AFParametreAssurance) getEntity(index)).getDateDebut();
    }

    public String getGenre(int index) {

        return ((AFParametreAssurance) getEntity(index)).getGenre();
    }

    public String getParametreAssuranceId(int index) {

        return ((AFParametreAssurance) getEntity(index)).getParametreAssuranceId();
    }

    public String getSexe(int index) {

        return ((AFParametreAssurance) getEntity(index)).getSexe();
    }

    public String getValeur(int index) {

        return ((AFParametreAssurance) getEntity(index)).getValeur();
    }
}
