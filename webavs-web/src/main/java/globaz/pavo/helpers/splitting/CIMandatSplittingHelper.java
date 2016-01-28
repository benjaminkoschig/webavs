package globaz.pavo.helpers.splitting;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.pavo.db.splitting.CIDossierSplitting;
import globaz.pavo.db.splitting.CIMandatSplitting;
import globaz.pavo.db.splitting.CIMandatSplittingRCViewBean;
import globaz.pavo.db.splitting.CIMandatSplittingViewBean;

/**
 * Controlleur g�rant les fonctions sp�ciales du mandat de splitting. Date de cr�ation : (16.10.2002 08:36:43)
 * 
 * @author: dgi
 */
public class CIMandatSplittingHelper extends FWHelper {
    /**
     * Constructeur.
     * 
     * @param action
     *            java.lang.String
     */
    public CIMandatSplittingHelper() {
        super();
    }

    /**
     * Ex�cution des m�thodes sp�cifiques au mandat de splitting. Date de cr�ation : (03.05.2002 16:23:10)
     * 
     * @return le viewBean en question
     * @param viewBean
     *            bean associ� � l'action en court
     * @param action
     *            action en court
     * @param session
     *            session de l'utilisateur
     */
    @Override
    public FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action,
            globaz.globall.api.BISession session) {

        try {

            if ("chercherMandat".equals(action.getActionPart())) {
                // charge l'en-t�te
                ((CIMandatSplittingRCViewBean) viewBean).chargeEntete();
            } else if ("revoquer".equals(action.getActionPart())) {
                // revoquer le mandat de splitting
                CIMandatSplitting mandat = (CIMandatSplitting) viewBean;
                if (mandat.isNew()) {
                    mandat.retrieve();
                }
                mandat.revoquerFromUserAction();
            } else if ("apercuRCI".equals(action.getActionPart())) {
                // recherche de l'annonce
                CIMandatSplitting mandat = (CIMandatSplitting) viewBean;
                if (mandat.isNew()) {
                    mandat.retrieve();
                }
                CIDossierSplitting dossier = mandat.loadDossier(null);
                String[] ids = null;
                if (mandat.isAssure(dossier)) {
                    ids = dossier.rechercheAnnonceRCI(CIDossierSplitting.ASSURE);
                } else {
                    ids = dossier.rechercheAnnonceRCI(CIDossierSplitting.CONJOINT);
                }
                if (ids != null && ids.length == 2) {
                    ((CIMandatSplittingViewBean) viewBean).setIdAnnonceRCI(ids[0]);
                    ((CIMandatSplittingViewBean) viewBean).setRefUniqueRCI(ids[1]);
                }
            }
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        return viewBean;
    }
}
