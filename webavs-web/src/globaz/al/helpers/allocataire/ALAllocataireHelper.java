package globaz.al.helpers.allocataire;

import globaz.al.helpers.ALAbstractHelper;
import globaz.al.vb.allocataire.ALAllocataireViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Helper d�di� au viewBean ALAllocataireViewBean
 * 
 * @author GMO
 */
public class ALAllocataireHelper extends ALAbstractHelper {
    /**
     * Initialise le mod�le allocataireComplex, c'est-�-dire qu'il le charge si il existe ou d�finit les valeurs par
     * d�faut sinon.
     * 
     * @see globaz.framework.controller.FWHelper#_init(globaz.framework.bean. FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        if (viewBean instanceof ALAllocataireViewBean) {
            ((ALAllocataireViewBean) viewBean).setAllocataireComplexModel(ALServiceLocator
                    .getAllocataireComplexModelService().initModel(
                            ((ALAllocataireViewBean) viewBean).getAllocataireComplexModel()));
        }
        super._init(viewBean, action, session);
    }
}
