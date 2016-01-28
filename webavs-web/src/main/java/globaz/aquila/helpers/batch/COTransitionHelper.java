/**
 *
 */
package globaz.aquila.helpers.batch;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.batch.COTransitionViewBean;
import globaz.aquila.db.poursuite.COContentieuxViewBean;
import globaz.aquila.helpers.batch.process.COProcessAnnulerTransition;
import globaz.aquila.helpers.batch.process.COProcessEffectuerTransition;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;

/**
 * @author SEL
 */
public class COTransitionHelper extends FWHelper {

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        FWViewBeanInterface vb = viewBean;

        if ("annulerdernieretransition".equals(action.getActionPart())) {
            if (viewBean instanceof COContentieuxViewBean) {
                COProcessAnnulerTransition process = new COProcessAnnulerTransition();
                process.setSession((BSession) session);
                process.setContentieux((COContentieux) viewBean);
                process.setIdExtourne(((COContentieuxViewBean) viewBean).isIdExtourne());

                try {
                    process.executeProcess();
                } catch (Exception e) {
                    vb.setMessage("Unable to start process!!! Reason : " + e.toString());
                    vb.setMsgType(FWViewBeanInterface.ERROR);
                }
            }
        } else if ("effectuertransition".equals(action.getActionPart())) {
            if (viewBean instanceof COTransitionViewBean) {
                COProcessEffectuerTransition process = new COProcessEffectuerTransition();
                process.setSession((BSession) session);
                process.setContentieux(((COTransitionViewBean) vb).getContentieux());
                process.setIdContentieux(((COTransitionViewBean) vb).getIdContentieux());
                process.setLibSequence(((COTransitionViewBean) vb).getLibSequence());
                process.setRefresh(((COTransitionViewBean) vb).getRefresh());
                process.setSelectedId(((COTransitionViewBean) vb).getSelectedId());
                process.setTaxes(((COTransitionViewBean) vb).getTaxes());
                process.setFraisEtInterets(((COTransitionViewBean) vb).getFraisEtInterets());
                process.setInteretCalcule(((COTransitionViewBean) vb).getInteretCalcule());
                process.setIdEtapeSuivante(((COTransitionViewBean) vb).getIdEtapeSuivante());
                process.setAction(((COTransitionViewBean) vb).getAction());
                process.setViewBean(((COTransitionViewBean) vb));

                try {
                    process.executeProcess();
                } catch (Exception e) {
                    vb.setMessage("Unable to start process!!! Reason : " + e.toString());
                    vb.setMsgType(FWViewBeanInterface.ERROR);
                }
            } else {
                super._start(viewBean, action, session);
            }
        }

        return vb;
    }

}
