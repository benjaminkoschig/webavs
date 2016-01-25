package globaz.pavo.db.splitting;

import globaz.framework.bean.FWListViewBeanInterface;

/**
 * Insérez la description du type ici. Date de création : (21.10.2002 13:43:06)
 * 
 * @author: Administrator
 */
public class CIDomicileSplittingListViewBean extends CIDomicileSplittingManager implements FWListViewBeanInterface {
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

    public String getDateDebut(int pos) {
        CIDomicileSplitting entity = (CIDomicileSplitting) getEntity(pos);
        return entity.getDateDebut();
    }

    public String getDateFin(int pos) {
        CIDomicileSplitting entity = (CIDomicileSplitting) getEntity(pos);
        return entity.getDateFin();
    }

    public String getIdDomicileSplitting(int pos) {
        CIDomicileSplitting entity = (CIDomicileSplitting) getEntity(pos);
        return entity.getIdDomicileSplitting();
    }

    public String getLibelle(int pos) {
        CIDomicileSplitting entity = (CIDomicileSplitting) getEntity(pos);
        return entity.getLibelle();

    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }
}
