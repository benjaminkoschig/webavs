package globaz.pavo.db.splitting;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.application.CIApplication;

/**
 * ViewBean pour l'en-tête de la gestion des mandats. Date de création : (29.10.2002 09:14:22)
 * 
 * @author: dgi
 */
public class CIMandatSplittingRCViewBean extends CIDossierSplitting implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String action = null;
    private String message = null;
    private String msgType = null;

    public java.lang.String getAction() {
        return action;
    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }

    public boolean hasLienGedActif() throws Exception {
        CIApplication application = (CIApplication) getSession().getApplication();
        return application.isSplittingWantLienGed() && !JadeStringUtil.isEmpty(getIdTiersInterne());
    }
    public String getGedServiceName() throws Exception {
        return ((CIApplication) getSession().getApplication()).getSplittingServiceGed();
    }

    /**
     * @return l'id Tiers interne selon le type (Assuré ou Conjoint) choisi dans le menu déroulant
     */
    public String getIdTiersInterne() {
        return ASSURE.equals(getTypePersonne()) ? getIdTiersInterneAssure() : getIdTiersInterneConjoint();
    }

    /**
     * @return l'id Tiers selon le type (Assuré ou Conjoint) choisi dans le menu déroulant
     */
    public String getIdTiers() {
        return ASSURE.equals(getTypePersonne()) ? getIdTiersAssure() : getIdTiersConjoint();
    }
}
