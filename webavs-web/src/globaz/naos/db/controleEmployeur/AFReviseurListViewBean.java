package globaz.naos.db.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;

public class AFReviseurListViewBean extends AFReviseurManager implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getIdReviseur(int index) {
        return ((AFReviseur) getEntity(index)).getIdReviseur();
    }

    public String getNomReviseur(int index) {
        return ((AFReviseur) getEntity(index)).getNomReviseur();
    }

    public String getVisa(int index) {
        return ((AFReviseur) getEntity(index)).getVisa();
    }

}
