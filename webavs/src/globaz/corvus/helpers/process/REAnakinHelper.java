/*
 * Créé le 05 sept. 07
 */
package globaz.corvus.helpers.process;

import globaz.corvus.application.REApplication;
import globaz.corvus.vb.process.REAnakinViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersUserValue;
import globaz.prestation.helpers.PRAbstractHelper;
import java.util.Vector;

/**
 * @author SCR
 * 
 */
public class REAnakinHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
    }

    /**
     * (non javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REAnakinViewBean vb = (REAnakinViewBean) viewBean;

        FWParametersUserValue valUtili = new globaz.globall.parameters.FWParametersUserValue();
        valUtili.setSession((BSession) session);

        Vector<String> param = new Vector<String>(1);

        if (REAnakinViewBean.TRAITEMENT_ACTIVER_ANAKIN_VALIDATOR.equals(vb.getTraitement())) {
            param.add(0, REAnakinViewBean.PARAM_VAL_ANAKIN_VALIDATOR_ACTIF);
        } else {
            param.add(0, REAnakinViewBean.PARAM_VAL_ANAKIN_VALIDATOR_INACTIF);
        }
        valUtili.addValeur(REApplication.DEFAULT_APPLICATION_CORVUS, REAnakinViewBean.PARAM_KEY_ANAKIN_VALIDATOR, param);
    }
}
