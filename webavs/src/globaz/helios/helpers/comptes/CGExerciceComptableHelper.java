package globaz.helios.helpers.comptes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.helios.db.comptes.CGExerciceComptableViewBean;

/**
 * pour demonstartion seulement, ne cette class n'est pas necessaire Date de création : (24.10.2002 15:30:40)
 * 
 * @author: Administrator
 */
public class CGExerciceComptableHelper extends FWHelper {
    /**
     * Commentaire relatif au constructeur CGExerciceComptableHelper.
     */
    public CGExerciceComptableHelper() {
        super();
    }

    @Override
    public FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action,
            globaz.globall.api.BISession session) {
        if (action.getActionPart().equals("choisir")) {

            viewBean.setMsgType(FWViewBeanInterface.OK);
            // charge l'exercice comptable
            CGExerciceComptableViewBean vBean = (CGExerciceComptableViewBean) viewBean;
            try {
                vBean.retrieve();
            } catch (Exception e) {
                e.printStackTrace();
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.getMessage());
            }
        } else {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        viewBean.setMessage("");
        return viewBean;
    }
}
