package globaz.pavo.helpers.splitting;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.pavo.db.splitting.CIRevenuSplittingRCViewBean;

/**
 * Controlleur gérant les fonctions spéciales des revenus de splitting. Date de création : (16.10.2002 08:36:43)
 * 
 * @author: dgi
 */
public class CIRevenuSplittingHelper extends FWHelper {
    /**
     * Constructeur.
     * 
     * @param action
     *            java.lang.String
     */
    public CIRevenuSplittingHelper() {
        super();
    }

    /**
     * Exécution des méthodes spécifiques aux revenus de splitting. Date de création : (03.05.2002 16:23:10)
     * 
     * @return le viewBean en question
     * @param viewBean
     *            bean associé à l'action en cout
     * @param action
     *            action en court
     * @param session
     *            session de l'utilisateur
     */
    @Override
    public FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action,
            globaz.globall.api.BISession session) {

        try {
            if ("chercherRevenu".equals(action.getActionPart())) {
                // charge l'en-tête
                ((CIRevenuSplittingRCViewBean) viewBean).chargeEntete();
            }
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        return viewBean;
    }
}
