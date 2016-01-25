package globaz.musca.helpers.facturation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.musca.process.FAImpressionFactureProcess;
import globaz.musca.process.FAImpressionFactureProcess.ModeFonctionnementEnum;

/**
 * @author MMO
 * @since 10 août 2011
 */
public class FAPassageFacturationImprimerDecomptesHelper extends FWHelper {

    @Override
    public void beforeExecute(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super.beforeExecute(viewBean, action, session);
        ((FAImpressionFactureProcess) viewBean).setModeFonctionnement(ModeFonctionnementEnum.STANDARD);
    }
}
