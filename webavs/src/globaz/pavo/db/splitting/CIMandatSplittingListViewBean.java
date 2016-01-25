package globaz.pavo.db.splitting;

import globaz.framework.bean.FWListViewBeanInterface;

/**
 * Bean de la liste de mandats de splitting. Date de création : (29.10.2002 13:43:14)
 * 
 * @author: dgi
 */
public class CIMandatSplittingListViewBean extends CIMandatSplittingManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String action = null;
    private java.lang.String message = null;
    private java.lang.String msgType = null;

    public java.lang.String getAction() {
        return action;
    }

    public String getAnneeDebut(int pos) {
        CIMandatSplitting entity = (CIMandatSplitting) getEntity(pos);
        return entity.getAnneeDebut();
    }

    public String getAnneeFin(int pos) {
        CIMandatSplitting entity = (CIMandatSplitting) getEntity(pos);
        return entity.getAnneeFin();
    }

    public String getIdEtat(int pos) {
        CIMandatSplitting entity = (CIMandatSplitting) getEntity(pos);
        return entity.getIdEtat();
    }

    public String getIdGenreSplitting(int pos) {
        CIMandatSplitting entity = (CIMandatSplitting) getEntity(pos);
        return entity.getIdGenreSplitting();
    }

    public String getIdMandatSplitting(int pos) {
        CIMandatSplitting entity = (CIMandatSplitting) getEntity(pos);
        return entity.getIdMandatSplitting();
    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }
}
