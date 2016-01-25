package globaz.al.helpers.adi;

import globaz.al.helpers.ALAbstractHelper;
import globaz.al.vb.adi.ALDecompteAdiViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.adi.DecompteAdiModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Helper dédié à l'écran décompte ADI et aux actions liées
 * 
 * @author GMO
 * 
 */
public class ALDecompteAdiHelper extends ALAbstractHelper {

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        try {
            if (viewBean instanceof ALDecompteAdiViewBean) {

                // On initialise le droit avec les valeurs par défaut
                DecompteAdiModel decompteAdiModel = ALServiceLocator.getDecompteAdiModelService().initModel(
                        ((ALDecompteAdiViewBean) viewBean).getDecompteAdiModel());
                ((ALDecompteAdiViewBean) viewBean).setDecompteAdiModel(decompteAdiModel);

            }
        } catch (JadeApplicationException e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        } catch (JadePersistenceException e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        super._init(viewBean, action, session);
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if ("supprimerDecompte".equals(action.getActionPart()) && (viewBean instanceof ALDecompteAdiViewBean)) {
            try {
                if (((ALDecompteAdiViewBean) viewBean).getDecompteAdiModel().isNew()) {
                    ((ALDecompteAdiViewBean) viewBean).retrieve();
                }
                ((ALDecompteAdiViewBean) viewBean).delete();
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            return viewBean;
        }
        if ("calculer".equals(action.getActionPart()) && (viewBean instanceof ALDecompteAdiViewBean)) {
            try {
                if (((ALDecompteAdiViewBean) viewBean).getDecompteAdiModel().isNew()) {
                    ((ALDecompteAdiViewBean) viewBean).retrieve();
                }
                ALServiceLocator.getCalculAdiBusinessService().calculForDecompte(
                        ((ALDecompteAdiViewBean) viewBean).getDecompteAdiModel());
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

            return viewBean;
        }

        else {
            return super.execute(viewBean, action, session);
        }
    }
}
