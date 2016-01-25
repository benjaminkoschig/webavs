package globaz.osiris.helpers.comptes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.db.BSession;
import globaz.osiris.db.comptes.CAExtournerSectionViewBean;

/**
 * Classe : type_conteneur Description : Date de création: 2 sept. 04
 * 
 * @author scr
 */
public class CAExtournerSectionHelper extends FWHelper {

    /**
     * Constructor for CAExtournerOperationHelper.
     */
    public CAExtournerSectionHelper() {
        super();
    }

    @Override
    public FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action,
            globaz.globall.api.BISession session) {

        try {
            if ("afficherSection".equals(action.getActionPart())) {
                CAExtournerSectionViewBean sect = (CAExtournerSectionViewBean) viewBean;
                sect.setSession((BSession) session);
                // Retrieve the operation
                sect.getSection();
            } else if ("executerExtourne".equals(action.getActionPart())) {
                CAExtournerSectionViewBean sect = (CAExtournerSectionViewBean) viewBean;
                sect.setSession((BSession) session);
                sect.getSection().extournerEcritures(null, null, sect.getComment());
            } else {
                return viewBean;
            }

        } catch (Exception e) {
            e.printStackTrace();
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        return viewBean;
    }

}
