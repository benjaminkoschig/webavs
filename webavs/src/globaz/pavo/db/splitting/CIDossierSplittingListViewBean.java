package globaz.pavo.db.splitting;

import globaz.framework.bean.FWListViewBeanInterface;

/**
 * Bean de l'aper�u des dossiers. Date de cr�ation : (16.10.2002 08:19:58)
 * 
 * @author: dgi
 */
public class CIDossierSplittingListViewBean extends CIDossierSplittingManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String action = null;
    private java.lang.String message = null;
    private java.lang.String msgType = null;

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.10.2002 10:35:21)
     */
    public CIDossierSplittingListViewBean() {

    }

    public String getAction() {
        return action;
    }

    public String getDateDivorce(int pos) {
        CIDossierSplitting entity = (CIDossierSplitting) getEntity(pos);
        return entity.getDateDivorce();
        // return "DateDivorce " + pos;

    }

    public String getDateOuvertureDossier(int pos) {
        CIDossierSplitting entity = (CIDossierSplitting) getEntity(pos);
        return entity.getDateOuvertureDossier();
        // return "DateOuvertureDossier " + pos;

    }

    public String getIdDossierSplitting(int pos) {
        CIDossierSplitting entity = (CIDossierSplitting) getEntity(pos);
        return entity.getIdDossierSplitting();
        // return pos + "";

    }

    public String getIdEtat(int pos) {
        CIDossierSplitting entity = (CIDossierSplitting) getEntity(pos);
        return entity.getIdEtat();
        // return "IdEtat " + pos;

    }

    public String getIdTiersAssure(int pos) {
        CIDossierSplitting entity = (CIDossierSplitting) getEntity(pos);
        return entity.getIdTiersAssure();
        // return "IdTiersAssure " + pos;

    }

    public String getIdTiersConjoint(int pos) {
        CIDossierSplitting entity = (CIDossierSplitting) getEntity(pos);
        return entity.getIdTiersConjoint();
        // return "IdTiersConjoint " + pos;

    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }
}
